package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.Category;
import com.team_proj.dsfw_team_proj.selfassessment.CategoryRepository;
import com.team_proj.dsfw_team_proj.selfassessment.SkillRepository;
import com.team_proj.dsfw_team_proj.selfassessment.SkillsEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@DisplayName("AdminService Integration Tests")
class AdminServiceIntegrationTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Test
    @DisplayName("Should add category and skill with real database")
    void addCategoryAndSkill_Integration_WithRealDatabase() {
        // Arrange n Act: add category
        Category category = adminService.addCategory("Digital Skills");

        // Assert category was saved
        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo("Digital Skills");
        assertThat(category.isActive()).isTrue();

        // Act: add skill to category
        SkillsEntity skill = adminService.addSkill("Email Management", category.getId());

        // Assert skill was saved with correct relationship
        assertThat(skill.getId()).isNotNull();
        assertThat(skill.getName()).isEqualTo("Email Management");
        assertThat(skill.getCategory().getId()).isEqualTo(category.getId());
        assertThat(skill.isActive()).isTrue();

        // Verify data persisted in database
        Category foundCategory = categoryRepository.findById(category.getId()).orElseThrow();
        assertThat(foundCategory.getName()).isEqualTo("Digital Skills");

        SkillsEntity foundSkill = skillRepository.findById(skill.getId()).orElseThrow();
        assertThat(foundSkill.getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("Should reactivate inactive category in real database")
    void addCategory_ShouldReactivateInactive_Integration() {
        // Arrange: create and deactivate category
        Category category = adminService.addCategory("Legacy Systems");
        Long categoryId = category.getId();
        adminService.deactivateCategory(categoryId);

        // Verify it's inactive
        Category deactivated = categoryRepository.findById(categoryId).orElseThrow();
        assertThat(deactivated.isActive()).isFalse();

        // Act: try to add same category again (should reactivate)
        Category reactivated = adminService.addCategory("Legacy Systems");

        // Assert: same ID, now active
        assertThat(reactivated.getId()).isEqualTo(categoryId);
        assertThat(reactivated.isActive()).isTrue();

        // Verify in database
        Category fromDb = categoryRepository.findById(categoryId).orElseThrow();
        assertThat(fromDb.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should group active skills by category correctly")
    void getActiveSkillsGroupedByCategory_Integration() {
        // Arrange: create categories and skills
        Category cat1 = adminService.addCategory("Programming");
        Category cat2 = adminService.addCategory("Design");

        adminService.addSkill("Java", cat1.getId());
        adminService.addSkill("Python", cat1.getId());
        adminService.addSkill("Figma", cat2.getId());

        // Act
        Map<Long, List<SkillsEntity>> grouped = adminService.getActiveSkillsGroupedByCategory();

        // Assert
        assertThat(grouped).hasSize(2);
        assertThat(grouped.get(cat1.getId())).hasSize(2);
        assertThat(grouped.get(cat2.getId())).hasSize(1);

        assertThat(grouped.get(cat1.getId()))
                .extracting(SkillsEntity::getName)
                .containsExactlyInAnyOrder("Java", "Python");

        assertThat(grouped.get(cat2.getId()))
                .extracting(SkillsEntity::getName)
                .containsExactly("Figma");
    }

    @Test
    @DisplayName("Should exclude deactivated category from grouped skills")
    void getActiveSkillsGroupedByCategory_ExcludesInactiveCategories_Integration() {
        // Arrange: create category with skills
        Category category = adminService.addCategory("Archived");
        adminService.addSkill("Old Skill 1", category.getId());
        adminService.addSkill("Old Skill 2", category.getId());

        // Verify skills are in result before deactivation
        Map<Long, List<SkillsEntity>> beforeDeactivation = adminService.getActiveSkillsGroupedByCategory();
        assertThat(beforeDeactivation).containsKey(category.getId());

        // Act: deactivate category
        adminService.deactivateCategory(category.getId());

        // Assert: skills no longer appear in grouped result
        Map<Long, List<SkillsEntity>> afterDeactivation = adminService.getActiveSkillsGroupedByCategory();
        assertThat(afterDeactivation).doesNotContainKey(category.getId());
    }
}