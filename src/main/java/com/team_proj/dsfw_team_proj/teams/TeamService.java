package com.team_proj.dsfw_team_proj.teams;

import com.team_proj.dsfw_team_proj.auth.UserEntity;

public interface TeamService {

    TeamEntity createTeam(String teamName, String description, String password, UserEntity creator);
    void joinTeam(String joinCode, String password, UserEntity user);
    boolean isManager(UserEntity user, Long teamId);
    void deleteTeam(Long teamId);
    void leaveTeam(UserEntity user, Long teamId);
}
