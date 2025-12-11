package com.team_proj.dsfw_team_proj.selfassessment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(MockitoExtension.class)
class AssessmentModelTest {

    private MockMvc mvc;

    @Mock
    private SelfAssessmentService saService;

    @InjectMocks
    private SelfAssessmentController assessmentController;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(assessmentController).build();
    }



    @Test
    @WithMockUser
    @DisplayName("GET /results returns 200 OK and the correct view template")
    void showAssessmentResults_ReturnsView() throws Exception {

        given(saService.getAllSubmissions()).willReturn(Collections.emptyList());
        mvc.perform(get("/self-assessment/results"))
                .andExpect(status().isOk())
                .andExpect(view().name("self-assessment/self-assessment-results"))
                .andExpect(model().attributeExists("assessmentData"));
    }



    @Test
    void showAssessmentResults_PopulatesModelCorrectly() throws Exception {
        AssessmentSubmission sub1 = new AssessmentSubmission();
        sub1.setId(100L);

        AssessmentSubmission sub2 = new AssessmentSubmission();
        sub2.setId(200L);

        List<AssessmentSubmission> mockList = Arrays.asList(sub1, sub2);


        when(saService.getAllSubmissions()).thenReturn(mockList);


        mvc.perform(get("/self-assessment/results"))
                .andExpect(status().isOk())
                .andExpect(view().name("self-assessment/self-assessment-results"))
                .andExpect(model().attributeExists("assessmentData"))
                .andExpect(model().attribute("assessmentData", hasSize(2)))
                .andExpect(model().attribute("assessmentData", hasItem(
                        hasProperty("id", is(100L))
                )));
    }
}