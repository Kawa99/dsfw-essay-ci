package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.Category;
import com.team_proj.dsfw_team_proj.selfassessment.SkillsEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    // the controller will load a page with categories + skills
    @Test
    @WithMockUser
    @DisplayName("Should return admin view with categories and skills")
    void showConfigPage_ShouldReturnAdminView() throws Exception {
        // Arrange
        Category category = new Category();
        category.setId(1L);
        category.setName("Content Design");

        SkillsEntity skill = new SkillsEntity();
        skill.setId(2L);
        skill.setName("Writes accessible content");

        when(adminService.getActiveCategories()).thenReturn(List.of(category));
        when(adminService.getActiveSkillsGroupedByCategory())
                .thenReturn(Map.of(1L, List.of(skill)));

        // Act & Assert
        mockMvc.perform(get("/admin/self-assessment"))
                .andExpect(status().isOk())
                .andExpect(view().name("self-assessment/self-assessment-admin"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("skillsByCategory"))
                .andExpect(model().attribute("categories", List.of(category)))
                .andExpect(model().attribute("skillsByCategory", Map.of(1L, List.of(skill))));
    }

    @Test
    @WithMockUser
    @DisplayName("Should redirect with success message when adding category")
    void addCategory_POST_RedirectsWithSuccess() throws Exception {
        // Arrange
        Category newCategory = new Category();
        newCategory.setId(1L);
        newCategory.setName("New Category");
        newCategory.setActive(true);

        when(adminService.addCategory("New Category")).thenReturn(newCategory);

        // Act & Assert
        mockMvc.perform(post("/admin/self-assessment/categories/add")
                        .with(csrf())
                        .param("name", "New Category"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/self-assessment"))
                .andExpect(flash().attribute("success", "Category added successfully"));
    }

    @Test
    @WithMockUser
    @DisplayName("Should redirect with error when adding duplicate category")
    void addCategory_POST_RedirectsWithError_WhenDuplicate() throws Exception {
        // Arrange
        when(adminService.addCategory("Duplicate"))
                .thenThrow(new IllegalArgumentException("Category 'Duplicate' already exists."));

        // Act & Assert
        mockMvc.perform(post("/admin/self-assessment/categories/add")
                        .with(csrf())
                        .param("name", "Duplicate"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/self-assessment"))
                .andExpect(flash().attribute("error", "Category 'Duplicate' already exists."));
    }

    @Test
    @WithMockUser
    @DisplayName("Should redirect with success when deactivating category")
    void deactivateCategory_POST_RedirectsWithSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/admin/self-assessment/categories/1/deactivate")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/self-assessment"))
                .andExpect(flash().attribute("success", "Category deactivated."));
    }

    @Test
    @WithMockUser
    @DisplayName("Should redirect with success when adding skill")
    void addSkill_POST_RedirectsWithSuccess() throws Exception {
        // Arrange
        Category category = new Category();
        category.setId(1L);

        SkillsEntity skill = new SkillsEntity();
        skill.setId(2L);
        skill.setName("New Skill");
        skill.setCategory(category);

        when(adminService.addSkill("New Skill", 1L)).thenReturn(skill);

        // Act & Assert
        mockMvc.perform(post("/admin/self-assessment/skills/add")
                        .with(csrf())
                        .param("categoryId", "1")
                        .param("name", "New Skill"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/self-assessment"))
                .andExpect(flash().attribute("success", "Skill added successfully"));
    }

    @Test
    @WithMockUser
    @DisplayName("Should redirect with error when adding skill to non-existent category")
    void addSkill_POST_RedirectsWithError_WhenCategoryNotFound() throws Exception {
        // Arrange
        when(adminService.addSkill("Test Skill", 999L))
                .thenThrow(new IllegalArgumentException("Category not found"));

        // Act & Assert
        mockMvc.perform(post("/admin/self-assessment/skills/add")
                        .with(csrf())
                        .param("categoryId", "999")
                        .param("name", "Test Skill"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/self-assessment"))
                .andExpect(flash().attribute("error", "Category not found"));
    }

    @Test
    @WithMockUser
    @DisplayName("Should redirect with success when deactivating skill")
    void deactivateSkill_POST_RedirectsWithSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/admin/self-assessment/skills/3/deactivate")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/self-assessment"))
                .andExpect(flash().attribute("success", "Skill deactivated."));
    }
}