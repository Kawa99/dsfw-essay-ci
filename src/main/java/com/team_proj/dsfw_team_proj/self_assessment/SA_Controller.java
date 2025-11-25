package com.team_proj.dsfw_team_proj.self_assessment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/self-assessment")
public class SA_Controller {

    private final SA_Service saService;

    public SA_Controller(SA_Service saService) {
        this.saService = saService;
    }

    @GetMapping
    public String showAssessmentPage(Model model) {
        model.addAttribute("assessmentData", saService.getAssessmentData());
        return "self_assessment";
    }
}