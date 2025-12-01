package com.team_proj.dsfw_team_proj.teams;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired TeamMembershipRepository membershipRepository;

    @Override
    public TeamEntity createTeam(String teamName, UserEntity creator){
        TeamEntity team = new TeamEntity();
        team.setTeamName(teamName);
        String code = java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        team.setJoinCode(code);

        TeamEntity savedTeam = teamRepository.save(team);

        TeamMembershipEntity membership = new TeamMembershipEntity();
        membership.setUser(creator);
        membership.setTeam(savedTeam);
        membership.setRole("MANAGER");

        membershipRepository.save(membership);

        return savedTeam;
    }
    @Override
    public void joinTeam(String joinCode, UserEntity user) {
        // Coming soon
    }

    @Override
    public boolean isManager(UserEntity user, Long teamId) {
        // Coming soon
        return false;
    }
}
