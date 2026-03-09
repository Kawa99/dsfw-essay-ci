//package com.team_proj.dsfw_team_proj.selfassessment;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@DisplayName("SkillRepository Integration Tests")
//class SkillRepositoryTest {
//
//    @Autowired
//    private SkillRepository skillRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    @DisplayName("Should find active skills with active categories and load category eagerly")
//    void testFindByIsActiveTrueAndCategory_IsActiveTrueOrderByCategoryIdAsc_LoadsRelationship() {
//        // Arrange; create multiple categories with skills
//        List<CategoryAndSkillTestDataFactory.CategoryWithSkills> testData =
//                CategoryAndSkillTestDataFactory.createMultipleCategoriesWithSkills();
//
//        for (CategoryAndSkillTestDataFactory.CategoryWithSkills data : testData) {
//            Category persistedCategory = entityManager.persist(data.getCategory());
//            for (SkillsEntity skill : data.getSkills()) {
//                skill.setCategory(persistedCategory);
//                entityManager.persist(skill);
//            }
//        }
//        entityManager.flush();
//        entityManager.clear();
//
//        // Act
//        List<SkillsEntity> skills = skillRepository
//                .findByIsActiveTrueAndCategory_IsActiveTrueOrderByCategoryIdAsc();
//
//        // Assert
//        assertThat(skills).hasSize(3);
//        assertThat(skills).allMatch(SkillsEntity::isActive);
//        assertThat(skills).allMatch(skill -> skill.getCategory().isActive());
//
//        // Verify category is loaded (no lazy loading exception)
//        assertThat(skills.get(0).getCategory().getName()).isNotNull();
//
//        // Verify ordering by category ID
//        assertThat(skills.get(0).getCategory().getName()).isEqualTo("Communication");
//        assertThat(skills.get(1).getCategory().getName()).isEqualTo("Communication");
//        assertThat(skills.get(2).getCategory().getName()).isEqualTo("Programming");
//    }
//
//    @Test
//    @DisplayName("Should check if skill exists by name")
//    void testExistsByName_WhenSkillExists_ReturnsTrue() {
//        // Arrange
//        Category category = CategoryAndSkillTestDataFactory.createActiveCategory("Test Category");
//        entityManager.persist(category);
//
//        SkillsEntity skill = CategoryAndSkillTestDataFactory.createActiveSkill("Email Management", category);
//        entityManager.persist(skill);
//        entityManager.flush();
//
//        // Act
//        boolean exists = skillRepository.existsByName("Email Management");
//
//        // Assert
//        assertThat(exists).isTrue();
//    }
//
//    @Test
//    @DisplayName("Should enforce unique constraint on skill name")
//    void testUniqueSkillName_WhenDuplicate_ThrowsException() {
//        // Arrange
//        Category category = CategoryAndSkillTestDataFactory.createActiveCategory("Test Category");
//        entityManager.persistAndFlush(category);
//
//        SkillsEntity skill1 = CategoryAndSkillTestDataFactory.createActiveSkill("Unique Skill", category);
//        entityManager.persistAndFlush(skill1);
//
//        SkillsEntity skill2 = CategoryAndSkillTestDataFactory.createActiveSkill("Unique Skill", category);
//
//        // Act & Assert
//        assertThatThrownBy(() -> entityManager.persistAndFlush(skill2))
//                .isInstanceOf(Exception.class);
//    }
//
//    @Test
//    @DisplayName("Should maintain relationship between skill and category")
//    void testSkillCategoryRelationship_IsProperlyMaintained() {
//        // Arrange
//        Category category = CategoryAndSkillTestDataFactory.createActiveCategory("Communication");
//        entityManager.persist(category);
//
//        SkillsEntity skill = CategoryAndSkillTestDataFactory.createActiveSkill("Email", category);
//        entityManager.persist(skill);
//        entityManager.flush();
//        entityManager.clear();
//
//        // Act
//        SkillsEntity foundSkill = skillRepository.findById(skill.getId()).orElseThrow();
//
//        // Assert
//        assertThat(foundSkill.getCategory()).isNotNull();
//        assertThat(foundSkill.getCategory().getId()).isEqualTo(category.getId());
//        assertThat(foundSkill.getCategory().getName()).isEqualTo("Communication");
//    }
//}