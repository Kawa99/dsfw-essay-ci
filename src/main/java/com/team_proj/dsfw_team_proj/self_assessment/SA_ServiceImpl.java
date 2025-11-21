package com.team_proj.dsfw_team_proj.self_assessment;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SA_ServiceImpl implements SA_Service {

    @Override
    public Map<Category, List<Skills>> getMockAssessmentData() {
        Map<Category, List<Skills>> data = new LinkedHashMap<>();

        for (int i = 1; i <= 5; i++) {
            Category category = new Category();
            category.setId((long) i);
            category.setName("Category " + i);

            List<Skills> skills = new ArrayList<>();
            for (int j = 1; j <= 6; j++) {
                Skills skill = new Skills();
                skill.setId((long) (i * 100 + j));
                skill.setName("Question " + j + ": Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
                skill.setCategory(category);
                skills.add(skill);
            }
            data.put(category, skills);
        }
        return data;
    }
}