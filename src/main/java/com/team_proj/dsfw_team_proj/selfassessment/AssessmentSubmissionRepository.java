package com.team_proj.dsfw_team_proj.selfassessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentSubmissionRepository extends JpaRepository<AssessmentSubmission, Long> {

    List<AssessmentSubmission> findAllByOrderBySubmittedAtDesc();
}