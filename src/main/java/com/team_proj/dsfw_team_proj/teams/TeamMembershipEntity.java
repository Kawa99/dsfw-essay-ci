package com.team_proj.dsfw_team_proj.teams;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TEAM_MEMBERSHIPS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamMembershipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    private String role;
}
