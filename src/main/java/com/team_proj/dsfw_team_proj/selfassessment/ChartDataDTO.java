package com.team_proj.dsfw_team_proj.selfassessment;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ChartDataDTO {
    //holds skill names
    private List<String> labels;

    //holds corresponding skill scores
    private List<Dataset> datasets;

    @Data
    @Builder
    public static class Dataset {
        private String label;
        private List<Double> data; // average scores for each skill
        private String backgroundColor;
        private String borderColor;
        private int borderWidth;
    }
}
