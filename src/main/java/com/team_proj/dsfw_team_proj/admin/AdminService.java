package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.Category;
import com.team_proj.dsfw_team_proj.selfassessment.Skills;

import java.util.List;
import java.util.Map;

public interface AdminService {

    // Categories
    List<Category> getActiveCategories();
    Category addCategory(String name);
    Category updateCategory(Long id, String newName);
    void deactivateCategory(Long id);

    // Skills
    Map<Long, List<Skills>> getActiveSkillsGroupedByCategory();
    Skills addSkill(String name, Long categoryId);
    Skills updateSkill(Long id, String newName);
    void deactivateSkill(Long id);
}
