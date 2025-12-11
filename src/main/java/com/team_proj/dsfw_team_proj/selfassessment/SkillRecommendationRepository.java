package com.team_proj.dsfw_team_proj.selfassessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRecommendationRepository extends JpaRepository<SkillRecommendation, Long> {

    List<SkillRecommendation> findBySkillId(Long skillId);

    void deleteBySkillId(Long skillId);
}
