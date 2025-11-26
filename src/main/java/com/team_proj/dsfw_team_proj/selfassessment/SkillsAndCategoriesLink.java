package com.team_proj.dsfw_team_proj.selfassessment;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "skills_category")
public class SkillsAndCategoriesLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "skill_id")
    private Skills skill;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

}