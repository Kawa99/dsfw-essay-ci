package com.team_proj.dsfw_team_proj.selfassessment;

import java.util.ArrayList;
import java.util.List;
import com.team_proj.dsfw_team_proj.selfassessment.QuestionType;

public class CategoryAndSkillTestDataFactory {

    public static Category createCategory(String name, boolean isActive) {
        Category category = new Category();
        category.setName(name);
        category.setActive(isActive);
        return category;
    }

    public static Category createActiveCategory(String name) {
        return createCategory(name, true);
    }

    public static Category createInactiveCategory(String name) {
        return createCategory(name, false);
    }

    // Skill builders
    public static SkillsEntity createActiveSkill(String name, Category category) {
        SkillsEntity skill = new SkillsEntity();
        skill.setName(name);
        skill.setCategory(category);
        skill.setActive(true);
        skill.setQuestionType(QuestionType.RATING_SCALE);
        return skill;
    }

    public static SkillsEntity createInactiveSkill(String name, Category category) {
        SkillsEntity skill = new SkillsEntity();
        skill.setName(name);
        skill.setCategory(category);
        skill.setActive(false);
        skill.setQuestionType(QuestionType.RATING_SCALE);
        return skill;
    }

    public static List<CategoryWithSkills> createMultipleCategoriesWithSkills() {
        List<CategoryWithSkills> result = new ArrayList<>();

        // Category 1: active with active skills
        Category cat1 = createActiveCategory("Communication");
        List<SkillsEntity> skills1 = new ArrayList<>();
        skills1.add(createActiveSkill("Email", cat1));
        skills1.add(createActiveSkill("Video Calls", cat1));
        result.add(new CategoryWithSkills(cat1, skills1));

        // Category 2: Active with mixed skills
        Category cat2 = createActiveCategory("Programming");
        List<SkillsEntity> skills2 = new ArrayList<>();
        skills2.add(createActiveSkill("Java", cat2));
        skills2.add(createInactiveSkill("Python", cat2));
        result.add(new CategoryWithSkills(cat2, skills2));

        // Category 3: INactive with active skills
        Category cat3 = createInactiveCategory("Legacy Systems");
        List<SkillsEntity> skills3 = new ArrayList<>();
        skills3.add(createActiveSkill("COBOL", cat3));
        result.add(new CategoryWithSkills(cat3, skills3));

        return result;
    }

    // helper class
    public static class CategoryWithSkills {
        private final Category category;
        private final List<SkillsEntity> skills;

        public CategoryWithSkills(Category category, List<SkillsEntity> skills) {
            this.category = category;
            this.skills = skills;
        }

        public Category getCategory() {
            return category;
        }

        public List<SkillsEntity> getSkills() {
            return skills;
        }
    }
}