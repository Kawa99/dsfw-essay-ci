package com.team_proj.dsfw_team_proj.Self_Assessment;

import jakarta.persistence.*;

@Entity
@Table(name = "skills_category")
public class SkillCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "skill_id")
    private Skills skill;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    // Getters and settors

    public Long getId() {
        return id;
    }

    public Skills getSkill() {
        return skill;
    }

    public Category getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSkill(Skills skill) {
        this.skill = skill;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
