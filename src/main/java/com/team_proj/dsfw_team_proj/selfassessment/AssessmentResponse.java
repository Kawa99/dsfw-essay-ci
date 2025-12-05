package com.team_proj.dsfw_team_proj.selfassessment;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "assessment_responses")
public class AssessmentResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private AssessmentSubmission submission;


    @ManyToOne
    @JoinColumn(name = "skill_id")
    private SkillsEntity skill;

    private int score;
}