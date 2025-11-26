package com.team_proj.dsfw_team_proj.selfassessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillCategoryRepository extends JpaRepository<SkillCategory, Long> {

    List<SkillCategory> findByCategoryAndSkill_IsActiveTrueAndCategory_IsActiveTrue(Category category);
}
