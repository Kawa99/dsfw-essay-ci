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
class SelfAssessmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SelfAssessmentService saService;

    @Test
    @DisplayName("Integration: Valid submission flow with mocked service but real validator")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldHandleValidSubmissionEndToEnd() throws Exception {
        // Given: mock service returns realistic assessment data
        Map<Category, List<SkillsEntity>> testData = TestDataFactory.createSingleCategoryData();
        when(saService.getAssessmentData()).thenReturn(testData);
        doNothing().when(saService).saveSubmission(anyMap());

        // When: user submits complete answers for one category
        mockMvc.perform(post("/self-assessment/submit")
                        .with(csrf())  // Add CSRF token
                        .param("answers[1]", "5")
                        .param("answers[2]", "4")
                        .param("answers[3]", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/self-assessment/results"));

        // Then: service saveSubmission was called
        verify(saService, times(1)).saveSubmission(anyMap());
    }

    @Test
    @DisplayName("Integration: Partial category submission rejected by real validator")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldRejectPartialCategoryWithRealValidator() throws Exception {
        // Given: mock service returns data, but use real validator
        Map<Category, List<SkillsEntity>> testData = TestDataFactory.createMultiCategoryData();
        when(saService.getAssessmentData()).thenReturn(testData);

        // When: user submits partial answers (only 2 of 3 questions)
        mockMvc.perform(post("/self-assessment/submit")
                        .with(csrf())  // Add CSRF token
                        .param("answers[1]", "5")
                        .param("answers[2]", "4"))
                // answers[3] is missing - real validator catches this
                .andExpect(status().isOk())
                .andExpect(view().name("self-assessment/self-assessment"))
                .andExpect(model().attributeExists("validationErrors"));

        // Then: service saveSubmission was never called
        verify(saService, never()).saveSubmission(anyMap());
    }

    @Test
    @DisplayName("Integration: One complete with one empty category is valid")
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldAcceptOneCompleteWithOneEmpty() throws Exception {
        // Given: mock service with multiple categories
        Map<Category, List<SkillsEntity>> testData = TestDataFactory.createMultiCategoryData();
        when(saService.getAssessmentData()).thenReturn(testData);
        doNothing().when(saService).saveSubmission(anyMap());

        // When: user completes only first category, leaves second empty
        mockMvc.perform(post("/self-assessment/submit")
                        .with(csrf())  // Add CSRF token
                        .param("answers[1]", "5")
                        .param("answers[2]", "4")
                        .param("answers[3]", "3"))
                // Category 2 (questions 4 & 5) left empty - should be valid
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/self-assessment/results"));

        verify(saService, times(1)).saveSubmission(anyMap());
    }
}