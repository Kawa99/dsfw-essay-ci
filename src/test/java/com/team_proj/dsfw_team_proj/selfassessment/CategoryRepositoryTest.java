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
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@DisplayName("CategoryRepository Unit Tests")
//class CategoryRepositoryTest {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    @DisplayName("Should find all active categories")
//    void testFindByIsActiveTrue_ReturnsOnlyActiveCategories() {
//        // Arrange - create test data
//        Category activeCategory1 = CategoryAndSkillTestDataFactory.createActiveCategory("Active Category 1");
//        Category activeCategory2 = CategoryAndSkillTestDataFactory.createActiveCategory("Active Category 2");
//        Category inactiveCategory = CategoryAndSkillTestDataFactory.createInactiveCategory("Inactive Category");
//
//        entityManager.persist(activeCategory1);
//        entityManager.persist(activeCategory2);
//        entityManager.persist(inactiveCategory);
//        entityManager.flush();
//
//        // Act
//        List<Category> activeCategories = categoryRepository.findByIsActiveTrue();
//
//        // Assert
//        assertThat(activeCategories).hasSize(2);
//        assertThat(activeCategories).extracting(Category::getName)
//                .containsExactlyInAnyOrder("Active Category 1", "Active Category 2");
//        assertThat(activeCategories).allMatch(Category::isActive);
//    }
//
//    @Test
//    @DisplayName("Should find category by exact name")
//    void testFindByName_WhenNameExists_ReturnsCategory() {
//        // Arrange
//        String categoryName = "Digital Literacy";
//        Category category = CategoryAndSkillTestDataFactory.createActiveCategory(categoryName);
//        entityManager.persist(category);
//        entityManager.flush();
//
//        // Act
//        Optional<Category> foundCategory = categoryRepository.findByName(categoryName);
//
//        // Assert
//        assertThat(foundCategory).isPresent();
//        assertThat(foundCategory.get().getName()).isEqualTo(categoryName);
//        assertThat(foundCategory.get().isActive()).isTrue();
//    }
//
//    @Test
//    @DisplayName("Should enforce unique constraint on category name")
//    void testUniqueCategoryName_WhenDuplicate_ThrowsException() {
//        // Arrange
//        Category category1 = CategoryAndSkillTestDataFactory.createActiveCategory("Unique Category");
//        entityManager.persistAndFlush(category1);
//
//        Category category2 = CategoryAndSkillTestDataFactory.createActiveCategory("Unique Category");
//
//        // Act & Assert
//        assertThatThrownBy(() -> entityManager.persistAndFlush(category2))
//                .isInstanceOf(Exception.class);
//    }
//}