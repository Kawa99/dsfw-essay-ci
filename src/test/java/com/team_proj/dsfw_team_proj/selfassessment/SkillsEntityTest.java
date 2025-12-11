package com.team_proj.dsfw_team_proj.selfassessment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("SkillsEntity Relationship and Constraint Tests")
class SkillsEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should create valid skill with category relationship")
    void testValidSkill_WithCategory_CanBePersisted() {
        // Arrange
        Category category = CategoryAndSkillTestDataFactory.createActiveCategory("Test Category");
        entityManager.persist(category);

        SkillsEntity skill = CategoryAndSkillTestDataFactory.createActiveSkill("Test Skill", category);

        // Act
        SkillsEntity savedSkill = entityManager.persistAndFlush(skill);

        // Assert
        assertThat(savedSkill.getId()).isNotNull();
        assertThat(savedSkill.getName()).isEqualTo("Test Skill");
        assertThat(savedSkill.isActive()).isTrue();
        assertThat(savedSkill.getCategory()).isNotNull();
        assertThat(savedSkill.getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    @DisplayName("Should allow multiple skills to reference same category")
    void testMultipleSkills_CanShareSameCategory() {
        // Arrange
        Category sharedCategory = CategoryAndSkillTestDataFactory.createActiveCategory("Shared Category");
        entityManager.persist(sharedCategory);

        SkillsEntity skill1 = CategoryAndSkillTestDataFactory.createActiveSkill("Skill 1", sharedCategory);
        SkillsEntity skill2 = CategoryAndSkillTestDataFactory.createActiveSkill("Skill 2", sharedCategory);
        SkillsEntity skill3 = CategoryAndSkillTestDataFactory.createActiveSkill("Skill 3", sharedCategory);

        // Act
        entityManager.persist(skill1);
        entityManager.persist(skill2);
        entityManager.persist(skill3);
        entityManager.flush();

        // Assert
        assertThat(List.of(skill1, skill2, skill3))
                .extracting(s -> s.getCategory().getId())
                .containsOnly(sharedCategory.getId());
    }

    @Test
    @DisplayName("Should allow reassigning skill to different category")
    void testCategory_CanBeReassigned() {
        // Arrange
        Category category1 = CategoryAndSkillTestDataFactory.createActiveCategory("Category 1");
        Category category2 = CategoryAndSkillTestDataFactory.createActiveCategory("Category 2");
        entityManager.persist(category1);
        entityManager.persist(category2);

        SkillsEntity skill = CategoryAndSkillTestDataFactory.createActiveSkill("Movable Skill", category1);
        entityManager.persistAndFlush(skill);
        Long skillId = skill.getId();

        // Act; reassign to different category
        skill.setCategory(category2);
        entityManager.flush();
        entityManager.clear();

        // Assert
        SkillsEntity reloadedSkill = entityManager.find(SkillsEntity.class, skillId);
        assertThat(reloadedSkill.getCategory().getId()).isEqualTo(category2.getId());
        assertThat(reloadedSkill.getCategory().getName()).isEqualTo("Category 2");
    }
}