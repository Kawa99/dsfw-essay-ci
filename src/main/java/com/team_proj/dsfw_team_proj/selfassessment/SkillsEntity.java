package com.team_proj.dsfw_team_proj.selfassessment;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "skills")
public class SkillsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The skill / question text
    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;
}
