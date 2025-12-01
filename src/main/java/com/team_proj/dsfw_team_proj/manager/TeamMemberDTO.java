package com.team_proj.dsfw_team_proj.manager;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TeamMemberDTO {
    private Long userId;
    private String name;
    private String role;
    private LocalDateTime lastSubmissionDate;

    public TeamMemberDTO(Long userId, String name, String role, LocalDateTime lastSubmissionDate) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.lastSubmissionDate = lastSubmissionDate;
    }
}