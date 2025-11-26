package com.team_proj.dsfw_team_proj.manager.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for displaying Assessment Results.
 * This class filters what we show on the screen so we don't expose
 * raw database entities to the frontend.
 */
@Data
@Builder
public class ManagerResultDTO {

    // The name of the skill (e.g., "Agile Working")
    private String skillName;

    // The category it belongs to (e.g., "Ways of Working")
    private String categoryName;

    // The raw score (e.g., 3)
    private int score;

    // The calculated text (e.g., "Practitioner - Capable")
    private String gapAnalysis;
}