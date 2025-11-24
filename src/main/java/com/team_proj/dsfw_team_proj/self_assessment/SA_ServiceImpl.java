package com.team_proj.dsfw_team_proj.self_assessment;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SA_ServiceImpl implements SA_Service {

    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    public SA_ServiceImpl(CategoryRepository categoryRepository, SkillRepository skillRepository) {
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public Map<Category, List<Skills>> getAssessmentData() {
        Map<Category, List<Skills>> data = new LinkedHashMap<>();

        List<Category> currentCategories = categoryRepository.findByIsActiveTrue();


        for (Category category : currentCategories) {
            List<Skills>skills = skillRepository.findByIsActiveTrueAndCategory_Id(category.getId());
            if(!skills.isEmpty()) {
                data.put(category, skills);
            }
        }
        return data;
    }
}