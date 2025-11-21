package com.team_proj.dsfw_team_proj.self_assessment;

import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skills {

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

    //Getters and setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public Category getCategory() {
        return category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
