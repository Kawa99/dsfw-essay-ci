package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.Category;
import com.team_proj.dsfw_team_proj.selfassessment.QuestionType;
import com.team_proj.dsfw_team_proj.selfassessment.SkillsEntity;
import com.team_proj.dsfw_team_proj.selfassessment.Tag;

import java.util.List;
import java.util.Map;

public interface AdminService {

    // Categories
    List<Category> getActiveCategories();
    Category addCategory(String name);
    Category updateCategory(Long id, String newName);
    void deactivateCategory(Long id);

    // SkillsEntity
    Map<Long, List<SkillsEntity>> getActiveSkillsGroupedByCategory();
    SkillsEntity addSkill(String name, Long categoryId, QuestionType questionType, String options);
    SkillsEntity updateSkill(Long id, String newName, QuestionType questionType, String options);
    void deactivateSkill(Long id);

    // Tags
    List<Tag> getAllTags();
    Tag createTag(String name);
    void updateSkillTags(Long skillId, List<Long> tagIds);
}
