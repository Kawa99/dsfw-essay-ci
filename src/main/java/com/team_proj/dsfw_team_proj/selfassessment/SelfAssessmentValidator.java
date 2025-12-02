package com.team_proj.dsfw_team_proj.selfassessment;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SelfAssessmentValidator {

    public Map<String, List<String>> validate(
            Map<Category, List<SkillsEntity>> assessmentData,
            Map<Long, Integer> answers) {

        Map<String, List<String>> errors = new LinkedHashMap<>();
        List<String> partialCats = new ArrayList<>();
        List<String> incompleteQuestions = new ArrayList<>();

        int completeCategories = 0;

        for (Map.Entry<Category, List<SkillsEntity>> entry : assessmentData.entrySet()) {
            Category category = entry.getKey();
            List<SkillsEntity> skills = entry.getValue();

            int total = skills.size();
            int answered = 0;

            for (SkillsEntity skill : skills) {
                if (answers.containsKey(skill.getId()) && answers.get(skill.getId()) != null) {
                    answered++;
                }
            }

            if (answered == 0) {
                // empty category is allowed
                continue;
            }

            if (answered == total) {
                completeCategories++;
            } else {
                // partially completed categories are invalid
                partialCats.add(category.getName());

                // list each unanswered question
                for (SkillsEntity skill : skills) {
                    if (!answers.containsKey(skill.getId())) {
                        incompleteQuestions.add(category.getName() + ": " + skill.getName());
                    }
                }
            }
        }

        if (!partialCats.isEmpty()) {
            errors.put("partialCategories", partialCats);
        }

        if (completeCategories == 0) {
            errors.put("noCompleteCategories",
                    Collections.singletonList("You must complete at least one category."));
        }

        if (!incompleteQuestions.isEmpty()) {
            errors.put("incompleteQuestions", incompleteQuestions);
        }

        return errors;
    }
}
