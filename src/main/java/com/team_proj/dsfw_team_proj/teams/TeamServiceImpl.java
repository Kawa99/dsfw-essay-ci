package com.team_proj.dsfw_team_proj.teams;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TeamMembershipRepository membershipRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public TeamEntity createTeam(String teamName, String description, String password, UserEntity creator){
        TeamEntity team = new TeamEntity();
        team.setTeamName(teamName);
        team.setDescription(description);
        team.setPassword(passwordEncoder.encode(password));

        int maxRetries = 5;
        for (int i = 0; i < maxRetries; i++) {
            try {
                String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                team.setJoinCode(code);

                TeamEntity savedTeam = teamRepository.save(team);

                TeamMembershipEntity membership = new TeamMembershipEntity();
                membership.setUser(creator);
                membership.setTeam(savedTeam);
                membership.setRole(TeamRole.MANAGER);

                membershipRepository.save(membership);
                return savedTeam;

            } catch (DataIntegrityViolationException e) {
                if (i == maxRetries - 1) throw new RuntimeException("Unable to generate unique team code");
            }
        }
        return null;
    }

    @Override
    public void joinTeam(String joinCode, String password, UserEntity user) {
        TeamEntity team = teamRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new RuntimeException("Invalid join code"));

        if (!passwordEncoder.matches(password, team.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        if (membershipRepository.existsByUserAndTeam(user, team)) {
            throw new RuntimeException("User is already a member of this team");
        }

        TeamMembershipEntity membership = new TeamMembershipEntity();
        membership.setUser(user);
        membership.setTeam(team);
        membership.setRole(TeamRole.MEMBER);

        membershipRepository.save(membership);
    }

    @Override
    public boolean isManager(UserEntity user, Long teamId) {
        TeamEntity team = teamRepository.findById(teamId).orElse(null);
        if (team == null) return false;

        Optional<TeamMembershipEntity> membership = membershipRepository.findByUserAndTeam(user, team);
        return membership.isPresent() && membership.get().getRole() == TeamRole.MANAGER;
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
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamMembershipEntity membership = membershipRepository.findByUserAndTeam(user, team)
                .orElseThrow(() -> new RuntimeException("You are not a member of this team"));

        if (membership.getRole() == TeamRole.MANAGER) {
            throw new RuntimeException("Managers cannot leave their own team. You must delete the team instead.");
        }
        membershipRepository.delete(membership);
    }
}