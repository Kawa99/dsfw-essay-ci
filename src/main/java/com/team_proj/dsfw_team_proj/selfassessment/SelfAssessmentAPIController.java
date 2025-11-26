package com.team_proj.dsfw_team_proj.self_assessment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/self-assessment")
public class SA_API_Controller {

    private final SA_Service saService;

    public SA_API_Controller(SA_Service saService) {
        this.saService = saService;
    }

    @GetMapping
    public Map<Category, List<Skills>> getAssessmentData() {
        return saService.getAssessmentData();
    }
}