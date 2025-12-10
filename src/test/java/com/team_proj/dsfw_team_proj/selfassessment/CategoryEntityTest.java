package com.team_proj.dsfw_team_proj.selfassessment;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Category Entity Validation Tests")
class CategoryEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should create valid category with all required fields")
    void testValidCategory_CanBePersisted() {
        // Arrange
        Category category = CategoryAndSkillTestDataFactory.createActiveCategory("Valid Category");

        // Act
        Category savedCategory = entityManager.persistAndFlush(category);

        // Assert
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Valid Category");
        assertThat(savedCategory.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should accept maximum allowed name length of 255 characters")
    void testCategoryName_AtMaxLength_IsPersisted() {
        // Arrange create name exactly 255 characters
        String maxLengthName = "A".repeat(255);
        Category category = CategoryAndSkillTestDataFactory.createActiveCategory(maxLengthName);

        // Act
        Category savedCategory = entityManager.persistAndFlush(category);

        // Assert
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).hasSize(255);
    }
}