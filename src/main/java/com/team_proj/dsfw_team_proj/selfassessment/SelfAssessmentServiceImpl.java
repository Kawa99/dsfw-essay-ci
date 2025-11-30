package com.team_proj.dsfw_team_proj.selfassessment;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SelfAssessmentServiceImpl implements SelfAssessmentService {

    private final List<Map<Long, Integer>> mockDatabase = new ArrayList<>();


    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    public SelfAssessmentServiceImpl(CategoryRepository categoryRepository, SkillRepository skillRepository) {
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public Map<Category, List<SkillsEntity>> getAssessmentData() {
        Map<Category, List<SkillsEntity>> data = new LinkedHashMap<>();

        List<Category> currentCategories = categoryRepository.findByIsActiveTrue();


        for (Category category : currentCategories) {
            List<SkillsEntity>skills = skillRepository.findByIsActiveTrueAndCategory_Id(category.getId());
            if(!skills.isEmpty()) {
                data.put(category, skills);
            }
        }
        return data;
    }

    @Override
    public void saveSubmission(Map<Long, Integer> userAnswers) {
        mockDatabase.add(userAnswers);
    }

    @Override
    public List<Map<Long, Integer>> getAllSubmissions() {
        return mockDatabase;
    }

}