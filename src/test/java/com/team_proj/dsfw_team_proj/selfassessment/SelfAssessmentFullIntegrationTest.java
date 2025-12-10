package com.team_proj.dsfw_team_proj.selfassessment;

import org.junit.jupiter.api.BeforeEach;
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
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SelfAssessmentFullIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SelfAssessmentService saService;

    @Autowired
    private SelfAssessmentValidator validator;

    @Test
    @DisplayName("Full integration: GET assessment page loads with application context")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldLoadAssessmentPageWithFullContext() throws Exception {
        // Given: mock service returns assessment data
        Map<Category, List<SkillsEntity>> testData = TestDataFactory.createSingleCategoryData();
        when(saService.getAssessmentData()).thenReturn(testData);

        // When and Then: Get request loads page with full Spring context
        mockMvc.perform(get("/self-assessment"))
                .andExpect(status().isOk())
                .andExpect(view().name("self-assessment/self-assessment"))
                .andExpect(model().attributeExists("assessmentData"))
                .andExpect(model().attribute("assessmentData", not(nullValue())));
    }

    @Test
    @DisplayName("Full integration: Valid submission redirects with full security context")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldSaveValidSubmissionWithSecurityContext() throws Exception {
        // Given: mock service
        Map<Category, List<SkillsEntity>> testData = TestDataFactory.createSingleCategoryData();
        when(saService.getAssessmentData()).thenReturn(testData);
        doNothing().when(saService).saveSubmission(anyMap());

        // When: submit the complete answers for one category with CSRF and authentication
        mockMvc.perform(post("/self-assessment/submit")
                        .with(csrf())
                        .param("answers[1]", "5")
                        .param("answers[2]", "4")
                        .param("answers[3]", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/self-assessment/results"));

        // Then: the service was called
        verify(saService, times(1)).saveSubmission(anyMap());
    }

    @Test
    @DisplayName("Full integration: Partial submission rejected with real validator")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldRejectPartialSubmissionWithRealValidator() throws Exception {
        // Given: mock service returns data
        Map<Category, List<SkillsEntity>> testData = TestDataFactory.createSingleCategoryData();
        when(saService.getAssessmentData()).thenReturn(testData);

        // When: submit partial answers (missing third question from same category)
        mockMvc.perform(post("/self-assessment/submit")
                        .with(csrf())
                        .param("answers[1]", "5")
                        .param("answers[2]", "4"))
                // Missing third skill - real validator catches this
                .andExpect(status().isOk())
                .andExpect(view().name("self-assessment/self-assessment"))
                .andExpect(model().attributeExists("validationErrors"));

        // Then: service was never called
        verify(saService, never()).saveSubmission(anyMap());
    }
}