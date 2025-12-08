package com.team_proj.dsfw_team_proj.teams;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import com.team_proj.dsfw_team_proj.notifications.NotificationService;
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

    private NotificationService notificationService;
    private UserService userService;

    public TeamServiceImpl(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    private void validateTeamPasswordOrThrow(String password) {
        if (password == null || password.isBlank()) {
            throw new RuntimeException("Password cannot be empty");
        }

        if (password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new RuntimeException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new RuntimeException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new RuntimeException("Password must contain at least one number");
        }

        if (!password.matches(".*[!@#$%^&*()_+{}\\[\\]'\"\\|/?.,><-].*")) {
            throw new RuntimeException("Password must contain at least one special character");
        }
    }


    @Override
    public TeamEntity createTeam(String teamName, String description, String password, UserEntity creator){
        // Validate password according to shared rules
        validateTeamPasswordOrThrow(password);

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
                notificationService.sendNotification(creator, "You have created team" + savedTeam.getTeamName());
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

        notificationService.sendNotification(user, "You were added to Team " + team.getTeamName());
        List<TeamMembershipEntity> memberships = membershipRepository.findAllByTeamId(team.getId());
        memberships.stream().filter(teamMembershipEntity -> teamMembershipEntity.getRole() == TeamRole.MANAGER)
                .forEach(teamMembershipEntity -> {
                    UserEntity manager = teamMembershipEntity.getUser();
                    notificationService.sendNotification(manager, user.getEmail() + " successfully joined Team " + team.getTeamName());
                });
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
        TeamEntity team = teamRepository.findById(teamId).orElse(null);

        membershipRepository.deleteAll(memberships);
        teamRepository.deleteById(teamId);

        UserEntity user = userService.getCurrentUser();
        notificationService.sendNotification(user, "You have deleted team " + team.getTeamName());
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

        notificationService.sendNotification(user, "You have left team" + team.getTeamName());

        List<TeamMembershipEntity> memberships = membershipRepository.findAllByTeamId(teamId);
        memberships.stream().filter(teamMembershipEntity -> teamMembershipEntity.getRole() == TeamRole.MANAGER)
                        .forEach(teamMembershipEntity -> {
                            UserEntity manager = teamMembershipEntity.getUser();
                            notificationService.sendNotification(manager, user.getEmail() + " left your" + team.getTeamName() + "team");
                        });

        membershipRepository.delete(membership);
    }

    @Override
    @Transactional
    public void removeMember(UserEntity requester, Long teamId, Long userId) {
        // 1. Only the team manager can remove members
        if (!isManager(requester, teamId)) {
            throw new RuntimeException("Only the team manager can remove team members");
        }

        // 2. Managers cannot remove themselves via this feature
        if (requester.getId().equals(userId)) {
            throw new RuntimeException("Managers cannot remove themselves. You must delete the team instead.");
        }

        // 3. Find the team
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // 4. Find the membership for the given user in this team
        List<TeamMembershipEntity> memberships = membershipRepository.findAllByTeamId(teamId);
        TeamMembershipEntity membershipToRemove = memberships.stream()
                .filter(m -> m.getUser() != null && m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User is not a member of this team"));

        // 5. Extra safety: do not allow removing another manager
        if (membershipToRemove.getRole() == TeamRole.MANAGER) {
            throw new RuntimeException("You cannot remove a team manager");
        }

        UserEntity removedUser = membershipToRemove.getUser();

        // 6. Delete the membership
        membershipRepository.delete(membershipToRemove);

        // For the manager
        notificationService.sendNotification(
                requester,
                "You removed " + removedUser.getEmail() + " from team " + team.getTeamName()
        );

        // For the removed user:
        notificationService.sendNotification(
                removedUser,
                "You were removed from team " + team.getTeamName()
        );
    }

    @Override
    @Transactional
    public void updateTeamName(Long teamId, String newName, UserEntity user) {
        // Check if user is manager
        if (!isManager(user, teamId)) {
            throw new RuntimeException("Only the team manager can update the team name");
        }

        // Validate team name is not empty
        if (newName == null || newName.trim().isEmpty()) {
            throw new RuntimeException("Team name cannot be empty");
        }

        // Find team and update name
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        team.setTeamName(newName.trim());
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void updateTeamDescription(Long teamId, String newDescription, UserEntity user) {
        // Check if user is manager
        if (!isManager(user, teamId)) {
            throw new RuntimeException("Only the team manager can update the team description");
        }

        // Validate description is not null (empty is okay)
        if (newDescription == null) {
            throw new RuntimeException("Team description cannot be null");
        }

        // Find team and update description
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        team.setDescription(newDescription.trim());
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void changeTeamPassword(Long teamId, String currentPassword, String newPassword, UserEntity user) {
        // Check if user is manager
        if (!isManager(user, teamId)) {
            throw new RuntimeException("Only the team manager can change the team password");
        }

        // Find team
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, team.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // ✅ Use shared validation for the new password
        validateTeamPasswordOrThrow(newPassword);

        // Hash and save new password
        team.setPassword(passwordEncoder.encode(newPassword));
        teamRepository.save(team);
    }

}