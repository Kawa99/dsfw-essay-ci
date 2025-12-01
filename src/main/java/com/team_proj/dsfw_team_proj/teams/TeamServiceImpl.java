package com.team_proj.dsfw_team_proj.teams;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMembershipRepository membershipRepository;

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
        TeamEntity team = teamRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new RuntimeException("Invalid join code"));

        if (membershipRepository.existsByUserAndTeam(user, team)) {
            throw new RuntimeException("User is already a member of this team");
        }

        TeamMembershipEntity membership = new TeamMembershipEntity();
        membership.setUser(user);
        membership.setTeam(team);
        membership.setRole("MEMBER");

        membershipRepository.save(membership);
    }

    @Override
    public boolean isManager(UserEntity user, Long teamId) {
        TeamEntity team = teamRepository.findById(teamId).orElse(null);
        if (team == null) return false;

        Optional<TeamMembershipEntity> membership = membershipRepository.findByUserAndTeam(user, team);
        return membership.isPresent() && "MANAGER".equals(membership.get().getRole());
    }
    @Override
    @Transactional
    public void deleteTeam(Long teamId) {
        List<TeamMembershipEntity> memberships = membershipRepository.findAllByTeamId(teamId);
        membershipRepository.deleteAll(memberships);
        teamRepository.deleteById(teamId);
    }
    @Override
    @Transactional
    public void leaveTeam(UserEntity user, Long teamId) {
        TeamEntity team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));

        TeamMembershipEntity membership = membershipRepository.findByUserAndTeam(user, team).orElseThrow(() -> new RuntimeException("You are not a member of this team"));
        if ("MANAGER".equals(membership.getRole())) {
            throw new RuntimeException("Managers cannot leave their own team. You must delete the team instead.");
        }
        membershipRepository.delete(membership);
    }
}