package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.Category;
import com.team_proj.dsfw_team_proj.selfassessment.QuestionType;
import com.team_proj.dsfw_team_proj.selfassessment.SkillsEntity;
import com.team_proj.dsfw_team_proj.selfassessment.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/self-assessment")
public class AdminController {

    private final AdminService saService;
    private final TagService tagService;

    public AdminController(AdminService saService, TagService tagService) {

        this.saService = saService;
        this.tagService = tagService;
    }

    @GetMapping
    public String showConfigPage(Model model) {
        List<Category> categories = saService.getActiveCategories();
        Map<Long, List<SkillsEntity>> skillsByCategory = saService.getActiveSkillsGroupedByCategory();

        model.addAttribute("categories", categories);
        model.addAttribute("skillsByCategory", skillsByCategory);

        List<Tag> allTags = tagService.getAllTags();
        model.addAttribute("allTags", allTags);


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
    public String addSkill(@RequestParam("name") String name,
                           @RequestParam("categoryId") Long categoryId,
                           @RequestParam("questionType") String questionTypeStr,
                           @RequestParam(value = "options", required = false) String options,
                           @RequestParam(value = "tagIds", required = false) List<Long> tagIds,
                           RedirectAttributes redirectAttributes,
                           HttpServletRequest request) {
        try {

            QuestionType questionType = QuestionType.valueOf(questionTypeStr);

            // Create the skill
            SkillsEntity skill = saService.addSkill(name, categoryId, questionType, options);

            // Attach tags
            saService.updateSkillTags(skill.getId(), tagIds);

            Map<String, String[]> paramMap = request.getParameterMap();
            Map<String, String> recMap = new java.util.HashMap<>();

            for (String key : paramMap.keySet()) {
                if (key.startsWith("rec_")) {
                    recMap.put(key.substring(4), request.getParameter(key));
                }
            }

            saService.saveRecommendations(skill.getId(), recMap);

            redirectAttributes.addFlashAttribute("success", "Question added successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/self-assessment";
    }


    @PostMapping("/skills/{id}/edit")
    public String editSkill(@PathVariable Long id,
                            @RequestParam("name") String name,
                            @RequestParam("questionType") String questionTypeStr,
                            @RequestParam(value = "options", required = false) String options,
                            @RequestParam(value = "tagIds", required = false) List<Long> tagIds,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {
        try {
            QuestionType questionType = QuestionType.valueOf(questionTypeStr);

            saService.updateSkill(id, name, questionType, options);

            // Save tags
            saService.updateSkillTags(id, tagIds);

            Map<String, String[]> paramMap = request.getParameterMap();
            Map<String, String> recMap = new java.util.HashMap<>();

            for (String key : paramMap.keySet()) {
                if (key.startsWith("rec_")) {
                    recMap.put(key.substring(4), request.getParameter(key));
                }
            }

            saService.updateSkillRecommendations(id, recMap);

            redirectAttributes.addFlashAttribute("success", "Question updated successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not update question.");
        }
        return "redirect:/admin/self-assessment";
    }


    @PostMapping("/skills/{id}/deactivate")
    public String deactivateSkill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        saService.deactivateSkill(id);
        redirectAttributes.addFlashAttribute("success", "Skill deactivated.");
        return "redirect:/admin/self-assessment";
    }

    @PostMapping("/tags/{id}/edit")
    @ResponseBody
    public Tag editTag(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return tagService.updateTag(id, body.get("name"));
    }

    @PostMapping("/tags/{id}/deactivate")
    @ResponseBody
    public void deactivateTag(@PathVariable Long id) {
        tagService.deactivateTag(id);
    }

    @PostMapping("/tags/create")
    @ResponseBody
    public Tag createTagAjax(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        return saService.createTag(name);
    }
}