package com.team_proj.dsfw_team_proj.admin;

import java.util.Map;

public interface RecommendationService {

    // Save all recommendations for a skill
    void saveRecommendations(Long skillId, Map<String, String> conditionToUrlMap);

    // Load all recommendations for a skill
    Map<String, String> getRecommendations(Long skillId);
}
