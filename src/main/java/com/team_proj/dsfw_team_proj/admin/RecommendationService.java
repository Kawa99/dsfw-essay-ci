package com.team_proj.dsfw_team_proj.admin;

import java.util.List;
import java.util.Map;

public interface RecommendationService {

    // Save all recommendations for a skill (Updated to handle lists)
    void saveRecommendations(Long skillId, Map<String, List<String>> conditionToUrlMap);

    // Load all recommendations for a skill
    Map<String, String> getRecommendations(Long skillId);
}