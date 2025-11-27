package com.team_proj.dsfw_team_proj.manager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List; // Required for List<>

@Entity
@Table(name = "managers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // One Manager -> Many Team Members
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<TeamMember> teamMembers;
}