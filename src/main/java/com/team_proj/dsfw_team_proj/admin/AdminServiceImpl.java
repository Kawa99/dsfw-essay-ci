package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.self_assessment.Category;
import com.team_proj.dsfw_team_proj.self_assessment.CategoryRepository;
import com.team_proj.dsfw_team_proj.self_assessment.SkillRepository;
import com.team_proj.dsfw_team_proj.self_assessment.Skills;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;

    public AdminServiceImpl(CategoryRepository categoryRepository,
                            SkillRepository skillRepository) {
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
    }

    // Category related methods

    @Override
    public List<Category> getActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    @Override
    public Category addCategory(String name) {
        validateText(name, "Category name");

        Category category = new Category();
        category.setName(name.trim());
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

    // Skills related methods

    @Override
    public Map<Long, List<Skills>> getActiveSkillsGroupedByCategory() {
        Map<Long, List<Skills>> result = new HashMap<>();
        List<Category> categories = getActiveCategories();

        for (Category category : categories) {
            List<Skills> skills =
                    skillRepository.findByIsActiveTrueAndCategory_Id(category.getId());
            result.put(category.getId(), skills);
        }

        return result;
    }

    @Override
    public Skills addSkill(String name, Long categoryId) {
        validateText(name, "Skill/question text");

        if (skillRepository.existsByName(name.trim())) {
            throw new IllegalArgumentException("This question already exists.");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));

        Skills skill = new Skills();
        skill.setName(name.trim());
        skill.setActive(true);
        skill.setCategory(category);

        return skillRepository.save(skill);
    }

    @Override
    public Skills updateSkill(Long id, String newName) {
        validateText(newName, "Skill/question text");

        Skills skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));

        skill.setName(newName.trim());
        return skillRepository.save(skill);
    }

    @Override
    public void deactivateSkill(Long id) {
        Skills skill = skillRepository.findById(id)
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
}
