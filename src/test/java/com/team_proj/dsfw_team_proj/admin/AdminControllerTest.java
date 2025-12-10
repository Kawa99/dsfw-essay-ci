package com.team_proj.dsfw_team_proj.admin;//package com.team_proj.dsfw_team_proj.admin;
//
//import com.team_proj.dsfw_team_proj.selfassessment.Category;
//import com.team_proj.dsfw_team_proj.selfassessment.SkillsEntity;
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
//        SkillsEntity skill = new SkillsEntity();
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

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        UserEntity admin = new UserEntity();
        admin.setFirstName("Test");
        admin.setLastName("Admin");
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("Password@123"));
        admin.setRole("ADMIN");
        userRepository.save(admin);
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    public void givenAdminRole_whenAccessAdmin_thenOk() throws Exception {
        mvc.perform(get("/admin/self-assessment"))
                .andExpect(status().isOk());
    }
}
