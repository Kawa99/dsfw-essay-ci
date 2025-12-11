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
    private TagService tagService;
    private RecommendationService recommendationService;

    private AdminServiceImpl service;

    @BeforeEach
    void setup() {
        categoryRepository = mock(CategoryRepository.class);
        skillRepository = mock(SkillRepository.class);
        tagService = mock(TagService.class);
        recommendationService = mock(RecommendationService.class);

        service = new AdminServiceImpl(
                categoryRepository,
                skillRepository,
                tagService,
                recommendationService
        );
    }

    @Test
    @DisplayName("Should throw exception when category name is blank")
    void addCategory_ShouldThrow_WhenBlank() {
        assertThatThrownBy(() -> service.addCategory("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be empty");
    }

    @Test
    @DisplayName("Should update category name successfully")
    void updateCategory_ShouldUpdateName() {
        Category existing = new Category();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setActive(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);

        Category updated = service.updateCategory(1L, "New Name");

        assertThat(updated.getName()).isEqualTo("New Name");
        verify(categoryRepository).save(existing);
    }

    @Test
    @DisplayName("Should deactivate skill successfully")
    void deactivateSkill_ShouldSetInactive() {
        SkillsEntity skill = new SkillsEntity();
        skill.setId(3L);
        skill.setActive(true);

        when(skillRepository.findById(3L)).thenReturn(Optional.of(skill));

        service.deactivateSkill(3L);

        assertThat(skill.isActive()).isFalse();
        verify(skillRepository).save(skill);
    }

    @Test
    @DisplayName("Should throw exception when adding skill to non-existent category")
    void addSkill_ShouldThrow_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addSkill("Test Skill", 999L, QuestionType.RATING_SCALE, null))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    @DisplayName("Should reactivate inactive category instead of creating duplicate")
    void addCategory_ShouldReactivateInactiveCategory() {
        Category inactiveCategory = new Category();
        inactiveCategory.setId(1L);
        inactiveCategory.setName("Design");
        inactiveCategory.setActive(false);

        when(categoryRepository.findByName("Design")).thenReturn(Optional.of(inactiveCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(inactiveCategory);

        Category result = service.addCategory("Design");

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
        Category activeCategory = new Category();
        activeCategory.setId(1L);
        activeCategory.setName("Programming");
        activeCategory.setActive(true);

        when(categoryRepository.findByName("Programming")).thenReturn(Optional.of(activeCategory));

        assertThatThrownBy(() -> service.addCategory("Programming"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should throw exception when adding duplicate skill name")
    void addSkill_ShouldThrow_WhenSkillNameExists() {
        when(skillRepository.existsByName("Duplicate Skill")).thenReturn(true);

        assertThatThrownBy(() -> service.addSkill("Duplicate Skill", 1L, QuestionType.RATING_SCALE, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should throw exception when deactivating non-existent skill")
    void deactivateSkill_ShouldThrow_WhenNotFound() {
        when(skillRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deactivateSkill(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }
}