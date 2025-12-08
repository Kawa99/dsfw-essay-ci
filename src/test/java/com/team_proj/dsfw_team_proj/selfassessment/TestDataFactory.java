package com.team_proj.dsfw_team_proj.selfassessment;

import java.util.*;

public class TestDataFactory {

    public static SkillsEntity createSkill(Long id, String name) {
        SkillsEntity skill = new SkillsEntity();
        skill.setId(id);
        skill.setName(name);
        return skill;
    }

    public static Category createCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    public static Map<Category, List<SkillsEntity>> createSingleCategoryData() {
        Map<Category, List<SkillsEntity>> data = new LinkedHashMap<>();

        Category cat1 = createCategory(1L, "Digital Skills");
        data.put(cat1, Arrays.asList(
                createSkill(1L, "Email Management"),
                createSkill(2L, "Basic Computing"),
                createSkill(3L, "Internet Safety")
        ));

        return data;
    }

    public static Map<Category, List<SkillsEntity>> createMultiCategoryData() {
        Map<Category, List<SkillsEntity>> data = createSingleCategoryData();

        Category cat2 = createCategory(2L, "Data Skills");
        data.put(cat2, Arrays.asList(
                createSkill(4L, "Spreadsheet Usage"),
                createSkill(5L, "Data Analysis")
        ));

        return data;
    }

    public static Map<Long, Integer> createCompleteAnswersForCategory1() {
        Map<Long, Integer> answers = new HashMap<>();
        answers.put(1L, 3);
        answers.put(2L, 4);
        answers.put(3L, 5);
        return answers;
    }

    public static Map<Long, Integer> createPartialAnswersForCategory1() {
        Map<Long, Integer> answers = new HashMap<>();
        answers.put(1L, 3);
        answers.put(2L, 4);
        return answers;
    }
}