package com.team_proj.dsfw_team_proj.selfassessment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/self-assessment")
public class SelfAssessmentAPIController {

    private final SelfAssessmentService saService;

    public SelfAssessmentAPIController(SelfAssessmentService saService) {
        this.saService = saService;
    }

    @GetMapping
    public Map<Category, List<Skills>> getAssessmentData() {
        return saService.getAssessmentData();
    }
}