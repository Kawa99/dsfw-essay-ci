//package com.team_proj.dsfw_team_proj.admin;
//
//import com.team_proj.dsfw_team_proj.selfassessment.Category;
//import com.team_proj.dsfw_team_proj.selfassessment.Skills;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AdminController.class)
//public class AdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AdminService service;
//
//    // the controller will load a page with categories + skills
//    @Test
//    void showConfigPage_ShouldReturnAdminView() throws Exception {
//        Category category = new Category();
//        category.setId(1L);
//        category.setName("Content Design");
//
//        Skills skill = new Skills();
//        skill.setId(2L);
//        skill.setName("Writes accessible content");
//
//        when(service.getActiveCategories()).thenReturn(List.of(category));
//        when(service.getActiveSkillsGroupedByCategory())
//                .thenReturn(Map.of(1L, List.of(skill)));
//
//        mockMvc.perform(get("/admin/self-assessment"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("self_assessment_admin"))
//                .andExpect(model().attributeExists("categories"))
//                .andExpect(model().attributeExists("skillsByCategory"));
//    }
//}
