package com.team_proj.dsfw_team_proj.selfassessment;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SelfAssessmentForm {

    private Map<Long, Integer> answers = new HashMap<>();
}
