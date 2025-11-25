package com.team_proj.dsfw_team_proj.self_assessment;

import java.util.List;
import java.util.Map;

public interface SA_Service {
    Map<Category, List<Skills>> getAssessmentData();

    void saveSubmission(Map<Long, Integer> userAnswers);

    List<Map<Long, Integer>> getAllSubmissions();
}