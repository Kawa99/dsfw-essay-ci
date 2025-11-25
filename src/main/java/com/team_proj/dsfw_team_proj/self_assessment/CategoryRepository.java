package com.team_proj.dsfw_team_proj.self_assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

     List<Category> findByIsActiveTrue();
}
