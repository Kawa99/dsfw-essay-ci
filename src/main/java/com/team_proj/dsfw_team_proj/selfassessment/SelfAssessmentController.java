package com.team_proj.dsfw_team_proj.selfassessment;

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
public class SelfAssessmentController {

    private final SelfAssessmentService saService;

    public SelfAssessmentController(SelfAssessmentService saService) {
        this.saService = saService;
    }

    @GetMapping
    public String showAssessmentPage(Model model) {
        model.addAttribute("assessmentData", saService.getAssessmentData());
        model.addAttribute("assessmentForm", new SelfAssessmentForm());
        return "self-assessment/self-assessment";
    }


    @PostMapping("/submit")
    public ModelAndView saveAssessment(@ModelAttribute("assessmentData") SelfAssessmentForm form) {
        ModelAndView mav;
        Map<Long, Integer> answers = form.getAnswers();
        saService.saveSubmission(answers);
        mav = new ModelAndView("redirect:/self-assessment/results");
        return mav;
    }

    @GetMapping("/results")
    public ModelAndView showAssessmentResults() {
        ModelAndView mav = new ModelAndView("self-assessment/self-assessment-results");
        List<Map<Long, Integer>> answers = saService.getAllSubmissions();
        mav.addObject("assessmentData", answers);
        return mav;
    }
}