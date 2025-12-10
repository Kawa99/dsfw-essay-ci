package com.team_proj.dsfw_team_proj.teams;


import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import com.team_proj.dsfw_team_proj.notifications.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {


    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMembershipRepository membershipRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TeamServiceImpl teamService;

    private final String userEmail = "user@test.com";
    private final String managerEmail = "manager@test.com";
    private final String teamName = "Test";

    @Test
    void removedMemberFromTeamSendsNotificationToManagerAndUser() {

        //Initializing the mock

        Long teamId = 1L;

        //creating mock user
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(userEmail);

        //creating mock manager
        UserEntity manager = new UserEntity();
        manager.setId(2L);
        manager.setEmail(managerEmail);

        //creating mock team
        TeamEntity team = new TeamEntity();
        team.setId(teamId);
        team.setTeamName(teamName);

        //assigning manager to manager role
        TeamMembershipEntity managerMembership = new TeamMembershipEntity();
        managerMembership.setTeam(team);
        managerMembership.setUser(manager);
        managerMembership.setRole(TeamRole.MANAGER);

        //assigning user to member role
        TeamMembershipEntity userMembership = new TeamMembershipEntity();
        userMembership.setTeam(team);
        userMembership.setUser(user);
        userMembership.setRole(TeamRole.MEMBER);

        // isManager(requester, teamId) -> true
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(membershipRepository.findByUserAndTeam(manager, team)).thenReturn(Optional.of(managerMembership));

        // removeMember(...) loads all memberships by team id to find the one to remove
        when(membershipRepository.findAllByTeamId(teamId)).thenReturn(List.of(userMembership));

        teamService.removeMember(manager, teamId, user.getId());

        verify(membershipRepository).delete(userMembership);

        verify(notificationService).sendNotification(user, "You were removed from team " + team.getTeamName());
        verify(notificationService).sendNotification(manager, "You removed " + user.getEmail() + " from team " + team.getTeamName());


    }


    @Test
    void addedMemberFromTeamSendsNotificationToManagerAndUser() {
        String joinCode = "TEST123";
        String password = "test123";
        Long teamId = 1L;

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(userEmail);

        UserEntity manager = new UserEntity();
        manager.setId(2L);
        manager.setEmail(managerEmail);

        TeamEntity team = new TeamEntity();
        team.setId(teamId);
        team.setTeamName(teamName);
        team.setPassword(password);
        team.setJoinCode(joinCode);

        TeamMembershipEntity managerMembership = new TeamMembershipEntity();
        managerMembership.setTeam(team);
        managerMembership.setUser(manager);
        managerMembership.setRole(TeamRole.MANAGER);

        when(teamRepository.findByJoinCode(joinCode)).thenReturn(Optional.of(team));
        when(passwordEncoder.matches(password, team.getPassword())).thenReturn(true);
        when(membershipRepository.existsByUserAndTeam(user, team)).thenReturn(false);
        when(membershipRepository.findAllByTeamId(teamId)).thenReturn(List.of(managerMembership));


        teamService.joinTeam(joinCode, password, user);

        verify(notificationService).sendNotification(user, "You were added to Team " + team.getTeamName());
        verify(notificationService).sendNotification(manager, user.getEmail() + " successfully joined Team " + team.getTeamName());

    }

    @Test
    void leaveTeamSendsNotificationToManagerAndUser() {
        Long teamId = 1L;

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(userEmail);

        UserEntity manager = new UserEntity();
        manager.setId(2L);
        manager.setEmail(managerEmail);

        TeamEntity team = new TeamEntity();
        team.setId(teamId);
        team.setTeamName(teamName);

        TeamMembershipEntity managerMembership = new TeamMembershipEntity();
        managerMembership.setTeam(team);
        managerMembership.setUser(manager);
        managerMembership.setRole(TeamRole.MANAGER);

        TeamMembershipEntity userMembership = new TeamMembershipEntity();
        userMembership.setTeam(team);
        userMembership.setUser(user);
        userMembership.setRole(TeamRole.MEMBER);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(membershipRepository.findByUserAndTeam(user, team)).thenReturn(Optional.of(userMembership));
        when(membershipRepository.findAllByTeamId(teamId)).thenReturn(List.of(managerMembership));

        teamService.leaveTeam(user, teamId);

        verify(notificationService).sendNotification(user, "You have left team " + team.getTeamName());
        verify(notificationService).sendNotification(manager, user.getEmail() + " left your " + team.getTeamName() + " team");

        verify(membershipRepository).delete(userMembership);


    }


}


