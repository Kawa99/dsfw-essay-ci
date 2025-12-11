package com.team_proj.dsfw_team_proj.selfassessment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SelfAssessmentValidatorTest {

    private SelfAssessmentValidator validator;
    private Map<Category, List<SkillsEntity>> assessmentData;

    @BeforeEach
    void setUp() {
        validator = new SelfAssessmentValidator();
        assessmentData = TestDataFactory.createMultiCategoryData();
    }

    @Test
    @DisplayName("Should pass validation when at least one category is fully completed")
    void shouldPassValidationWithOneCompleteCategory() {
        // Given: answers for all questions in category 1
        Map<Long, Integer> answers = TestDataFactory.createCompleteAnswersForCategory1();

        // When: validate
        Map<String, List<String>> errors = validator.validate(assessmentData, answers);

        // Then: no errors
        assertTrue(errors.isEmpty(), "Should have no validation errors");
    }

    @Test
    @DisplayName("Should reject validation when category is partially completed")
    void shouldRejectPartiallyCompletedCategory() {
        // Given: only 2 out of 3 questions answered in category 1
        Map<Long, Integer> answers = TestDataFactory.createPartialAnswersForCategory1();

        // When: validate
        Map<String, List<String>> errors = validator.validate(assessmentData, answers);

        // Then: should have partial category error
        assertTrue(errors.containsKey("partialCategories"));
        assertTrue(errors.get("partialCategories").contains("Digital Skills"));
        assertTrue(errors.containsKey("incompleteQuestions"));
    }

    @Test
    @DisplayName("Should allow empty categories as long as one is complete")
    void shouldAllowEmptyCategoriesWithOneComplete() {
        // Given: category 1 complete, category 2 empty
        Map<Long, Integer> answers = TestDataFactory.createCompleteAnswersForCategory1();

        // When: validate
        Map<String, List<String>> errors = validator.validate(assessmentData, answers);

        // Then: no errors because empty categories are allowed
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should reject when all categories are empty")
    void shouldRejectAllEmptyCategories() {
        // Given: no answers provided
        Map<Long, Integer> answers = new HashMap<>();

        // When: validate
        Map<String, List<String>> errors = validator.validate(assessmentData, answers);

        // Then: should have error about no complete categories
        assertTrue(errors.containsKey("noCompleteCategories"));
    }

    @Test
    @DisplayName("Should reject when answer has null value")
    void shouldRejectNullAnswerValue() {
        // Given: answers with null value
        Map<Long, Integer> answers = new HashMap<>();
        answers.put(1L, 3);
        answers.put(2L, null);
        answers.put(3L, 5);

        // When: validate
        Map<String, List<String>> errors = validator.validate(assessmentData, answers);

        // Then: should treat null as unanswered and reject partial category
        assertTrue(errors.containsKey("partialCategories"));
    }
}