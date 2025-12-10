package com.team_proj.dsfw_team_proj.selfassessment;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentSubmissionRepository extends JpaRepository<AssessmentSubmission, Long> {

    List<AssessmentSubmission> findByUserOrderBySubmittedAtDesc(UserEntity user);
}