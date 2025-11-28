package com.team_proj.dsfw_team_proj.manager;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FakeOverviewDTO {
    private Long userID;
    private String Name;
    private LocalDateTime submissionDate;

    public FakeOverviewDTO(Long userID, String Name, LocalDateTime submissionDate) {
        this.userID = userID;
        this.Name = Name;
        this.submissionDate = submissionDate;
    }
}

