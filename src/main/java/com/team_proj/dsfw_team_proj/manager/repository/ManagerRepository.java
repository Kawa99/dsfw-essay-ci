package com.team_proj.dsfw_team_proj.manager.repository;

import com.team_proj.dsfw_team_proj.manager.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    // JpaRepository gives you 'findById', 'save', 'findAll' automatically!
}