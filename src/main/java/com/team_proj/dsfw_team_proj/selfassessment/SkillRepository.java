package com.team_proj.dsfw_team_proj.selfassessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<SkillsEntity, Long> {

    List<SkillsEntity> findByIsActiveTrue();

    List<SkillsEntity> findByIsActiveTrueAndCategory_Id(Long categoryId);

    boolean existsByName(String name);
}
