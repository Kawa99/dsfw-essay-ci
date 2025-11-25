package com.team_proj.dsfw_team_proj.self_assessment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

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
        model.addAttribute("assessmentForm", new SelfAssessmentForm());
        return "self_assessment";
    }


    @PostMapping("/submit")
    public ModelAndView saveAssessment(@ModelAttribute("assessmentData") SelfAssessmentForm form) {
        Map<Long, Integer> answers = form.getAnswers();
        saService.saveSubmission(answers);
        return new ModelAndView("redirect:/self-assessment/results");
    }

    @GetMapping("/results")
    public ModelAndView showAssessmentResults() {
        ModelAndView mav = new ModelAndView("SA/SA_Results");
        List<Map<Long, Integer>> answers = saService.getAllSubmissions();
        mav.addObject("assessmentData", answers);
        return mav;
    }
}