package com.team_proj.dsfw_team_proj.Self_Assessment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/self-assessment")
public class SA_Controller {

    private final SA_Service saService;

    public SA_Controller(SA_Service saService) {
        this.saService = saService;
    }

    @GetMapping
    public String showConfigPage(Model model) {
        List<Category> categories = saService.getActiveCategories();
        Map<Long, List<Skills>> skillsByCategory = saService.getActiveSkillsGroupedByCategory();

        model.addAttribute("categories", categories);
        model.addAttribute("skillsByCategory", skillsByCategory);

        return "self_assessment_admin";
    }

    // Category related actions

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam("name") String name) {
        saService.addCategory(name);
        return "redirect:/admin/self-assessment";
    }

    @PostMapping("/categories/{id}/edit")
    public String editCategory(@PathVariable Long id,
                               @RequestParam("name") String name) {
        saService.updateCategory(id, name);
        return "redirect:/admin/self-assessment";
    }

    @PostMapping("/categories/{id}/deactivate")
    public String deactivateCategory(@PathVariable Long id) {
        saService.deactivateCategory(id);
        return "redirect:/admin/self-assessment";
    }

    // Skill related actions

    @PostMapping("/skills/add")
    public String addSkill(@RequestParam("categoryId") Long categoryId,
                           @RequestParam("name") String name) {
        saService.addSkill(name, categoryId);
        return "redirect:/admin/self-assessment";
    }

    @PostMapping("/skills/{id}/edit")
    public String editSkill(@PathVariable Long id,
                            @RequestParam("name") String name) {
        saService.updateSkill(id, name);
        return "redirect:/admin/self-assessment";
    }

    @PostMapping("/skills/{id}/deactivate")
    public String deactivateSkill(@PathVariable Long id) {
        saService.deactivateSkill(id);
        return "redirect:/admin/self-assessment";
    }
}
