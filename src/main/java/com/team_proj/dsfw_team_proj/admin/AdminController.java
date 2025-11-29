package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.Category;
import com.team_proj.dsfw_team_proj.selfassessment.Skills;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/self-assessment")
public class AdminController {

    private final AdminService saService;

    public AdminController(AdminService saService) {
        this.saService = saService;
    }

    @GetMapping
    public String showConfigPage(Model model) {
        List<Category> categories = saService.getActiveCategories();
        Map<Long, List<Skills>> skillsByCategory = saService.getActiveSkillsGroupedByCategory();

        model.addAttribute("categories", categories);
        model.addAttribute("skillsByCategory", skillsByCategory);

        return "self-assessment/self-assessment-admin";
    }

    // Category related actions

    @PostMapping("/categories/add")
    public String addCategory(@RequestParam("name") String name,
                              RedirectAttributes redirectAttributes) {
        try {
            // This calls the service.
            // If the category is new -> Creates it.
            // If it is inactive -> Reactivates it.
            // If it is active/duplicate -> Throws Exception.
            saService.addCategory(name);

            redirectAttributes.addFlashAttribute("success", "Category added successfully");

        } catch (IllegalArgumentException e) {
            // This catches the "Category already exists" error from the service
            redirectAttributes.addFlashAttribute("error", e.getMessage());

        } catch (Exception e) {
            // This catches any unexpected database crashes
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
        }

        return "redirect:/admin/self-assessment";
    }

    @PostMapping("/categories/{id}/edit")
    public String editCategory(@PathVariable Long id,
                               @RequestParam("name") String name,
                               RedirectAttributes redirectAttributes) {
        try {
            saService.updateCategory(id, name);
            redirectAttributes.addFlashAttribute("success", "Category updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not update category.");
        }
        return "redirect:/admin/self-assessment";
    }

    @PostMapping("/categories/{id}/deactivate")
    public String deactivateCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        saService.deactivateCategory(id);
        redirectAttributes.addFlashAttribute("success", "Category deactivated.");
        return "redirect:/admin/self-assessment";
    }

    // Skill related actions

    @PostMapping("/skills/add")
    public String addSkill(@RequestParam("categoryId") Long categoryId,
                           @RequestParam("name") String name,
                           RedirectAttributes redirectAttributes) {

        try {
            saService.addSkill(name, categoryId);
            redirectAttributes.addFlashAttribute("success", "Skill added successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/self-assessment";
    }

    @PostMapping("/skills/{id}/edit")
    public String editSkill(@PathVariable Long id,
                            @RequestParam("name") String name,
                            RedirectAttributes redirectAttributes) {
        saService.updateSkill(id, name);
        redirectAttributes.addFlashAttribute("success", "Skill updated successfully");
        return "redirect:/admin/self-assessment";
    }

    @PostMapping("/skills/{id}/deactivate")
    public String deactivateSkill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        saService.deactivateSkill(id);
        redirectAttributes.addFlashAttribute("success", "Skill deactivated.");
        return "redirect:/admin/self-assessment";
    }
}