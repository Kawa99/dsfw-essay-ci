package com.team_proj.dsfw_team_proj.selfassessment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SelfAssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SelfAssessmentService saService;

    @MockBean
    private SelfAssessmentValidator validator;

    @Test
    @DisplayName("GET /self-assessment should render assessment page with data")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldRenderAssessmentPage() throws Exception {
        // Given: mock service returns assessment data
        Map<Category, List<SkillsEntity>> testData = TestDataFactory.createSingleCategoryData();
        when(saService.getAssessmentData()).thenReturn(testData);

        // When and Then: Get request succeeds and model contains data
        mockMvc.perform(get("/self-assessment"))
                .andExpect(status().isOk())
                .andExpect(view().name("self-assessment/self-assessment"))
                .andExpect(model().attributeExists("assessmentData"))
                .andExpect(model().attributeExists("assessmentForm"));

        verify(saService, times(1)).getAssessmentData();
    }

    @Test
    @DisplayName("POST /self-assessment/submit with valid data should redirect to results")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldRedirectToResultsOnValidSubmission() throws Exception {
        // Given: mock service and validator for valid submission
        Map<Category, List<SkillsEntity>> testData = TestDataFactory.createSingleCategoryData();
        when(saService.getAssessmentData()).thenReturn(testData);
        when(validator.validate(anyMap(), anyMap())).thenReturn(Collections.emptyMap());

        // When and Then: POSt with valid answers redirects
        mockMvc.perform(post("/self-assessment/submit")
                        .with(csrf())
                        .param("answers[1]", "3")
                        .param("answers[2]", "4")
                        .param("answers[3]", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/self-assessment/results"));

        verify(saService, times(1)).saveSubmission(anyMap());
    }

    @Test
    @DisplayName("POST /self-assessment/submit with invalid data should return to form with errors")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldReturnFormWithValidationErrors() throws Exception {
        // Given: validator returns errors for partial category
        Map<Category, List<SkillsEntity>> testData = TestDataFactory.createSingleCategoryData();
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("partialCategories", Arrays.asList("Digital Skills"));
        errors.put("noCompleteCategories", Arrays.asList("Must complete at least one"));
        errors.put("incompleteQuestions", new ArrayList<>());

        when(saService.getAssessmentData()).thenReturn(testData);
        when(validator.validate(anyMap(), anyMap())).thenReturn(errors);

        // When & Then
        mockMvc.perform(post("/self-assessment/submit")
                        .with(csrf())
                        .param("answers[1]", "3")
                        .param("answers[2]", "4"))
                .andExpect(status().isOk());

        verify(saService, never()).saveSubmission(anyMap());
    }
}