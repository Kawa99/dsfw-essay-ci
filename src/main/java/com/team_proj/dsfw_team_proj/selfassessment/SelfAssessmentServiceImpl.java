package com.team_proj.dsfw_team_proj.selfassessment;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SelfAssessmentServiceImpl implements SelfAssessmentService {


    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final AssessmentSubmissionRepository submissionRepository;

    public SelfAssessmentServiceImpl(CategoryRepository categoryRepository, SkillRepository skillRepository, AssessmentSubmissionRepository submissionRepository) {
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public Map<Category, List<SkillsEntity>> getAssessmentData() {
        Map<Category, List<SkillsEntity>> data = new LinkedHashMap<>();

        List<Category> currentCategories = categoryRepository.findByIsActiveTrue();


        for (Category category : currentCategories) {
            List<SkillsEntity> skills = skillRepository.findByIsActiveTrueAndCategory_Id(category.getId());
            if (!skills.isEmpty()) {
                data.put(category, skills);
            }
        }
        return data;
    }

    @Override
    @Transactional
    public void saveSubmission(Map<Long, Integer> userAnswers) {
        AssessmentSubmission submission = new AssessmentSubmission();

        List<AssessmentResponse> responseList = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : userAnswers.entrySet()) {
            Long skillId = entry.getKey();
            Integer score = entry.getValue();


            SkillsEntity skill = skillRepository.findById(skillId).orElse(null);

            if (skill != null) {
                AssessmentResponse response = new AssessmentResponse();
                response.setSkill(skill);
                response.setScore(score);
                response.setSubmission(submission);

                responseList.add(response);
            }
        }

        submission.setResponses(responseList);

        submissionRepository.save(submission);
    }

    @Override
    public List<AssessmentSubmission> getAllSubmissions() {
        return submissionRepository.findAllByOrderBySubmittedAtDesc();
    }

}