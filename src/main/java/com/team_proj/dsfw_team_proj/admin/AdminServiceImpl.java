package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final TagService tagService;
    private final RecommendationService recommendationService;

    public AdminServiceImpl(CategoryRepository categoryRepository,
                            SkillRepository skillRepository,
                            TagService tagService,
                            RecommendationService recommendationService) {
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
        this.tagService = tagService;
        this.recommendationService = recommendationService;
    }

    // Category related methods

    @Override
    public List<Category> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    @Override
    public Category addCategory(String name) {
        validateText(name, "Category name");
        String trimmedName = name.trim();

        // Check if category already exists (active or inactive)
        Optional<Category> existingCategory = categoryRepository.findByName(trimmedName);

        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();

            // If it exists and is active, we can't add it again
            if (category.isActive()) {
                throw new IllegalArgumentException("Category '" + trimmedName + "' already exists.");
            }

            // If it exists but is inactive (soft deleted), reactivate it
            category.setActive(true);
            return categoryRepository.save(category);
        }

        // If it doesn't exist, create a new one
        Category category = new Category();
        category.setName(trimmedName);
        category.setActive(true);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, String newName) {
        validateText(newName, "Category name");

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setName(newName.trim());
        return categoryRepository.save(category);
    }

    @Override
    public void deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setActive(false);
        categoryRepository.save(category);
    }

    // SkillsEntity related methods

    @Override
    public Map<Long, List<SkillsEntity>> getActiveSkillsGroupedByCategory() {
        List<SkillsEntity> allSkills = skillRepository.findByIsActiveTrueAndCategory_IsActiveTrueOrderByCategoryIdAsc();
        return allSkills.stream()
                .collect(Collectors.groupingBy(skill -> skill.getCategory().getId(),Collectors.toList()));
    }

    @Override
    public SkillsEntity addSkill(String name, Long categoryId, QuestionType questionType, String options) {
        validateText(name, "Skill/question text");

        if (skillRepository.existsByName(name.trim())) {
            throw new IllegalArgumentException("This question already exists.");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));

        // Validate options for question types that need them
        if (questionType == QuestionType.MULTIPLE_CHOICE || questionType == QuestionType.DROPDOWN) {
            if (options == null || options.trim().isEmpty()) {
                throw new IllegalArgumentException("Options are required for multiple choice and dropdown questions");
            }
        }

        SkillsEntity skill = new SkillsEntity();
        skill.setName(name.trim());
        skill.setActive(true);
        skill.setCategory(category);
        skill.setQuestionType(questionType);
        skill.setOptions(options != null ? options.trim() : null);

        return skillRepository.save(skill);
    }

    @Override
    public SkillsEntity updateSkill(Long id, String newName, QuestionType questionType, String options) {
        validateText(newName, "Skill/question text");

        SkillsEntity skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));

        // Validate options for question types that need them
        if (questionType == QuestionType.MULTIPLE_CHOICE || questionType == QuestionType.DROPDOWN) {
            if (options == null || options.trim().isEmpty()) {
                throw new IllegalArgumentException("Options are required for multiple choice and dropdown questions");
            }
        }

        skill.setName(newName.trim());
        skill.setQuestionType(questionType);
        skill.setOptions(options != null ? options.trim() : null);

        return skillRepository.save(skill);
    }

    @Override
    public void deactivateSkill(Long id) {
        SkillsEntity skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));

        skill.setActive(false);
        skillRepository.save(skill);
    }

    // Helper method

    private void validateText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        if (value.trim().length() > 255) {
            throw new IllegalArgumentException(fieldName + " is too long (max 255 chars)");
        }
    }

    @Override
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @Override
    public Tag createTag(String name) {
        return tagService.createTag(name);
    }

    @Override
    public void updateSkillTags(Long skillId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            tagService.clearTagsFromSkill(skillId);
        } else {
            tagService.assignTagsToSkill(skillId, tagIds);
        }
    }

    @Override
    public void updateSkillRecommendations(Long skillId, Map<String, List<String>> conditionToUrlMap) {
        recommendationService.saveRecommendations(skillId, conditionToUrlMap);
    }

    @Override
    public Map<String, String> getSkillRecommendations(Long skillId) {
        return recommendationService.getRecommendations(skillId);
    }

    @Override
    public void saveRecommendations(Long skillId, Map<String, List<String>> recMap) {
        recommendationService.saveRecommendations(skillId, recMap);
    }

    @Override
    public Map<String, List<String>> getSkillRecommendationsGrouped(Long skillId) {
        return recommendationService.getRecommendationsGrouped(skillId);
    }
}