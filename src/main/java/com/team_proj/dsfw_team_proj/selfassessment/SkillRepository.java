package com.team_proj.dsfw_team_proj.selfassessment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<SkillsEntity, Long> {

    List<SkillsEntity> findByIsActiveTrue();

    @EntityGraph(attributePaths = {"category"})
    List<SkillsEntity> findByIsActiveTrueAndCategory_IsActiveTrueOrderByCategoryIdAsc();

    boolean existsByName(String name);
}
