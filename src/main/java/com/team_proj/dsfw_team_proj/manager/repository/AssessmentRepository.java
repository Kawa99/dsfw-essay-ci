package com.team_proj.dsfw_team_proj.manager.repository;

import com.team_proj.dsfw_team_proj.manager.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository; // The magic Spring tool
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    // Find all assessments for a specific team member
    List<Assessment> findByTeamMemberId(Long teamMemberId);
}