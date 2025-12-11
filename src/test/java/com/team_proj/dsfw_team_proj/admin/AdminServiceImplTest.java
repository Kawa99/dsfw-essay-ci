package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class AdminServiceImplTest {

    private CategoryRepository categoryRepository;
    private SkillRepository skillRepository;
    private AdminServiceImpl service;

    @BeforeEach
    void setup() {
        categoryRepository = mock(CategoryRepository.class);
        skillRepository = mock(SkillRepository.class);
        service = new AdminServiceImpl(categoryRepository, skillRepository);
    }

    // Adding a Category with wrong input
    @Test
    @DisplayName("Should throw exception when category name is blank")
    void addCategory_ShouldThrow_WhenBlank() {
        // Act n Assert
        assertThatThrownBy(() -> service.addCategory("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be empty");
    }

    // Updating a Category
    @Test
    @DisplayName("Should update category name successfully")
    void updateCategory_ShouldUpdateName() {
        // Arrange
        Category existing = new Category();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setActive(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);

        // Act
        Category updated = service.updateCategory(1L, "New Name");

        // Assert
        assertThat(updated.getName()).isEqualTo("New Name");
        verify(categoryRepository).save(existing);
    }

    // Deactivating a skills
    @Test
    @DisplayName("Should deactivate skill successfully")
    void deactivateSkill_ShouldSetInactive() {
        // Arrange
        SkillsEntity skill = new SkillsEntity();
        skill.setId(3L);
        skill.setActive(true);

        when(skillRepository.findById(3L)).thenReturn(Optional.of(skill));

        // Act
        service.deactivateSkill(3L);

        // Assert
        assertThat(skill.isActive()).isFalse();
        verify(skillRepository).save(skill);
    }

    @Test
    @DisplayName("Should throw exception when adding skill to non-existent category")
    void addSkill_ShouldThrow_WhenCategoryDoesNotExist() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act n Assert
        assertThatThrownBy(() -> service.addSkill("Test Skill", 999L, QuestionType.RATING_SCALE, null))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    @DisplayName("Should reactivate inactive category instead of creating duplicate")
    void addCategory_ShouldReactivateInactiveCategory() {
        // Arrange: inactive category exists
        Category inactiveCategory = new Category();
        inactiveCategory.setId(1L);
        inactiveCategory.setName("Design");
        inactiveCategory.setActive(false);

        when(categoryRepository.findByName("Design")).thenReturn(Optional.of(inactiveCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(inactiveCategory);

        // Act
        Category result = service.addCategory("Design");

        // Assert
        assertThat(result.isActive()).isTrue();
        assertThat(result.getName()).isEqualTo("Design");
        verify(categoryRepository).save(inactiveCategory);
        verify(categoryRepository, never()).save(argThat(cat ->
                cat != inactiveCategory && "Design".equals(cat.getName())
        ));
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate active category")
    void addCategory_ShouldThrow_WhenAlreadyActive() {
        // Arrange; active category exists
        Category activeCategory = new Category();
        activeCategory.setId(1L);
        activeCategory.setName("Programming");
        activeCategory.setActive(true);

        when(categoryRepository.findByName("Programming")).thenReturn(Optional.of(activeCategory));

        // Act and Assert
        assertThatThrownBy(() -> service.addCategory("Programming"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate skill name")
    void addSkill_ShouldThrow_WhenSkillNameExists() {
        // Arrange
        when(skillRepository.existsByName("Duplicate Skill")).thenReturn(true);

        // Act n Assert
        assertThatThrownBy(() -> service.addSkill("Duplicate Skill", 1L, QuestionType.RATING_SCALE, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent skill")
    void deactivateSkill_ShouldThrow_WhenNotFound() {
        // Arrange
        when(skillRepository.findById(999L)).thenReturn(Optional.empty());

        // Act n Assert
        assertThatThrownBy(() -> service.deactivateSkill(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }
}
