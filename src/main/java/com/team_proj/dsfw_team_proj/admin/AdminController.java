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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
            Map<String, List<String>> recMap = new java.util.HashMap<>();

            // Get question type and options to map condition keys properly
            String[] optionsArray = null;
            if (options != null && !options.trim().isEmpty()) {
                optionsArray = options.split("\n");
            }

            for (String key : paramMap.keySet()) {
                if (key.startsWith("rec_")) {
                    String formKey = key.substring(4); // e.g., "BEGINNER", "Strongly_Disagree", "YES"
                    String[] urls = paramMap.get(key);

                    List<String> urlList = new java.util.ArrayList<>();
                    if (urls != null) {
                        for (String url : urls) {
                            if (url != null && !url.trim().isEmpty()) {
                                urlList.add(url.trim());
                            }
                        }
                    }

                    if (urlList.isEmpty()) continue; // Skip empty URL lists

                    // Convert form key to database condition key
                    List<String> conditionKeys = mapFormKeyToConditionKeys(formKey, questionType, optionsArray);
                    for (String conditionKey : conditionKeys) {
                        // Merge URLs if the condition key already exists
                        recMap.computeIfAbsent(conditionKey, k -> new ArrayList<>()).addAll(urlList);
                    }
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
            Map<String, List<String>> recMap = new java.util.HashMap<>();

            String[] optionsArray = null;
            if (options != null && !options.trim().isEmpty()) {
                optionsArray = options.split("\n");
            }

            for (String key : paramMap.keySet()) {
                if (key.startsWith("rec_")) {
                    String formKey = key.substring(4); // e.g., "BEGINNER", "Strongly_Disagree", "YES"
                    String[] urls = paramMap.get(key);

                    List<String> urlList = new java.util.ArrayList<>();
                    if (urls != null) {
                        for (String url : urls) {
                            if (url != null && !url.trim().isEmpty()) {
                                urlList.add(url.trim());
                            }
                        }
                    }

                    if (urlList.isEmpty()) continue; // Skip empty URL lists

                    // Convert form key to database condition key
                    List<String> conditionKeys = mapFormKeyToConditionKeys(formKey, questionType, optionsArray);
                    for (String conditionKey : conditionKeys) {
                        // Merge URLs if the condition key already exists
                        recMap.computeIfAbsent(conditionKey, k -> new ArrayList<>()).addAll(urlList);
                    }
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

    @GetMapping("/skills/{id}/recommendations")
    @ResponseBody
    public Map<String, List<String>> getSkillRecommendations(@PathVariable Long id) {
        return saService.getSkillRecommendationsGrouped(id);
    }

    private List<String> mapFormKeyToConditionKeys(String formKey, QuestionType questionType, String[] options) {
        List<String> keys = new ArrayList<>();

        switch (questionType) {
            case RATING_SCALE:
                if ("BEGINNER".equals(formKey)) {
                    keys.add("rating_1");
                    keys.add("rating_2");
                } else if ("INTERMEDIATE".equals(formKey)) {
                    keys.add("rating_3");
                } else if ("ADVANCED".equals(formKey)) {
                    keys.add("rating_4");
                    keys.add("rating_5");
                }
                break;

            case YES_NO:
                if ("YES".equals(formKey)) {
                    keys.add("yes");
                } else if ("NO".equals(formKey)) {
                    keys.add("no");
                }
                break;

            case DROPDOWN:
            case MULTIPLE_CHOICE:
                // Find which option index this corresponds to
                if (options != null) {
                    for (int i = 0; i < options.length; i++) {
                        String optionSafeKey = options[i].trim().replaceAll("[^a-zA-Z0-9-_]", "_");
                        if (optionSafeKey.equals(formKey)) {
                            keys.add("option_" + (i + 1));
                            break;
                        }
                    }
                }
                break;
        }

        return keys;
    }
}