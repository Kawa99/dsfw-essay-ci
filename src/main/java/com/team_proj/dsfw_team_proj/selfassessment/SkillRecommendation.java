package com.team_proj.dsfw_team_proj.selfassessment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "skill_recommendations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"skill_id", "condition_key"}))
public class SkillRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private SkillsEntity skill;

    @Column(name = "condition_key", nullable = false, length = 50)
    private String conditionKey;

    @Column(name = "recommended_url", nullable = false, length = 500)
    private String recommendedUrl;
}
