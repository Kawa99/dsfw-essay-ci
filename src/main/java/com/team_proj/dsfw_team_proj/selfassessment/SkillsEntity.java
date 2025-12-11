package com.team_proj.dsfw_team_proj.selfassessment;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "skills")
public class SkillsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false, length = 50)
    private QuestionType questionType = QuestionType.RATING_SCALE;

    @Column(name = "options", columnDefinition = "TEXT")
    private String options;

    @ManyToMany
    @JoinTable(
            name = "skill_tags",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public List<String> getOptionsList() {
        if (options == null || options.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(options.trim().split("\\r?\\n"));
    }
}