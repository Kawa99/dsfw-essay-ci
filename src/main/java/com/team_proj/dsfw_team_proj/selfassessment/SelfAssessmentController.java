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

    private final SelfAssessmentValidator validator;

    public SelfAssessmentController(SelfAssessmentService saService,
                                    SelfAssessmentValidator validator) {
        this.saService = saService;
        this.validator = validator;
    }

    @GetMapping
    public String showAssessmentPage(Model model) {
        model.addAttribute("assessmentData", saService.getAssessmentData());
        model.addAttribute("assessmentForm", new ResultsFormDTO());
        return "self-assessment/self-assessment";
    }


    @PostMapping("/submit")
    public ModelAndView saveAssessment(
            @ModelAttribute("assessmentForm") ResultsFormDTO form,
            Model model) {

        // Load category + skills structure
        Map<Category, List<SkillsEntity>> assessmentData = saService.getAssessmentData();

        // Validate
        Map<String, List<String>> errors = validator.validate(assessmentData, form.getAnswers());

        if (!errors.isEmpty()) {
            // Return same page with errors and user-input preserved
            model.addAttribute("assessmentData", assessmentData);
            model.addAttribute("assessmentForm", form);
            model.addAttribute("validationErrors", errors);

            return new ModelAndView("self-assessment/self-assessment");
        }

        // If valid then save and redirect
        saService.saveSubmission(form.getAnswers());
        return new ModelAndView("redirect:/self-assessment/results");
    }


    @GetMapping("/results")
    public ModelAndView showAssessmentResults() {
        ModelAndView mav = new ModelAndView("self-assessment/self-assessment-results");
        List<AssessmentSubmission> answers = saService.getAllSubmissions();
        mav.addObject("assessmentData", answers);
        return mav;
    }
}