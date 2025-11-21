package com.team_proj.dsfw_team_proj.Self_Assessment;

import java.util.List;
import java.util.Map;

public interface SA_Service {

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
