package com.team_proj.dsfw_team_proj.selfassessment;

import java.util.List;
import java.util.Map;

public interface SelfAssessmentService {
    Map<Category, List<SkillsEntity>> getAssessmentData();

    void saveSubmission(Map<Long, Integer> userAnswers);

    List<Map<Long, Integer>> getAllSubmissions();
}