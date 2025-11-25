package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.Category;
import com.team_proj.dsfw_team_proj.selfassessment.CategoryRepository;
import com.team_proj.dsfw_team_proj.selfassessment.SkillRepository;
import com.team_proj.dsfw_team_proj.selfassessment.Skills;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    // Adding a Category
    @Test
    void addCategory_ShouldSaveCategory() {
        Category saved = new Category();
        saved.setId(1L);
        saved.setName("Content Design");
        saved.setActive(true);

        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        Category result = service.addCategory("Content Design");

        assertEquals("Content Design", result.getName());
        assertTrue(result.isActive());
        verify(categoryRepository).save(any(Category.class));
    }

    // Adding a Category with wrong input
    @Test
    void addCategory_ShouldThrow_WhenBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addCategory("   ");
        });
    }

    // Updating a Category
    @Test
    void updateCategory_ShouldUpdateName() {
        Category existing = new Category();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setActive(true);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);

        Category updated = service.updateCategory(1L, "New Name");

        assertEquals("New Name", updated.getName());
    }

    // Adding a Skill
    @Test
    void addSkill_ShouldSaveSkill() {
        Category category = new Category();
        category.setId(5L);

        when(categoryRepository.findById(5L)).thenReturn(Optional.of(category));

        Skills saved = new Skills();
        saved.setId(10L);
        saved.setName("User Research Experience");
        saved.setCategory(category);

        when(skillRepository.save(any(Skills.class))).thenReturn(saved);

        Skills result = service.addSkill("User Research Experience", 5L);

        assertEquals("User Research Experience", result.getName());
        verify(skillRepository).save(any(Skills.class));
    }

    // Deactivating a skills
    @Test
    void deactivateSkill_ShouldSetInactive() {
        Skills skill = new Skills();
        skill.setId(3L);
        skill.setActive(true);

        when(skillRepository.findById(3L)).thenReturn(Optional.of(skill));

        service.deactivateSkill(3L);

        assertFalse(skill.isActive());
        verify(skillRepository).save(skill);
    }

    @Test
    void addSkill_ShouldThrow_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.addSkill("Test Skill", 999L);
        });
    }

}
