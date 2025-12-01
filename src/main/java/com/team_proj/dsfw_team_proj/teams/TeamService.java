package com.team_proj.dsfw_team_proj.teams;

import com.team_proj.dsfw_team_proj.auth.UserEntity;

public interface TeamService {

    TeamEntity createTeam(String teamName, UserEntity creator);
    void joinTeam(String joinCode, UserEntity user);
    boolean isManager(UserEntity user, Long teamId);
}
