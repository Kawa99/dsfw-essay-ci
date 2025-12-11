package com.team_proj.dsfw_team_proj.auth_test;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.teams.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceLogicTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMembershipRepository membershipRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TeamServiceImpl teamService;

    private UserEntity manager;
    private UserEntity member;
    private TeamEntity team;

    @BeforeEach
    public void setup() {
        manager = new UserEntity();
        manager.setId(1L);
        manager.setEmail("boss@gov.uk");

        member = new UserEntity();
        member.setId(2L);
        member.setEmail("worker@gov.uk");

        team = new TeamEntity();
        team.setId(10L);
        team.setTeamName("Alpha");
        team.setPassword("EncodedPass");
    }

    @Test
    public void shouldFailJoinWithIncorrectPassword() {
        team.setJoinCode("CODE12");
        when(teamRepository.findByJoinCode("CODE12")).thenReturn(Optional.of(team));
        when(passwordEncoder.matches("WrongPass", "EncodedPass")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () ->
                teamService.joinTeam("CODE12", "WrongPass", member)
        );
        assertEquals("Incorrect password", exception.getMessage());
    }

    @Test
    public void managerCannotLeaveTheirOwnTeam() {
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        TeamMembershipEntity managerMembership = new TeamMembershipEntity();
        managerMembership.setRole(TeamRole.MANAGER);
        when(membershipRepository.findByUserAndTeam(manager, team)).thenReturn(Optional.of(managerMembership));

        Exception exception = assertThrows(RuntimeException.class, () ->
                teamService.leaveTeam(manager, 10L)
        );
        assertTrue(exception.getMessage().contains("Managers cannot leave their own team"));
    }

    @Test
    public void cannotLeaveTeamIfNotAMember() {
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(membershipRepository.findByUserAndTeam(member, team)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                teamService.leaveTeam(member, 10L)
        );
    }

    @Test
    public void nonManagerCannotRemoveMembers() {
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        TeamMembershipEntity memberMembership = new TeamMembershipEntity();
        memberMembership.setRole(TeamRole.MEMBER);
        when(membershipRepository.findByUserAndTeam(member, team)).thenReturn(Optional.of(memberMembership));

        assertThrows(RuntimeException.class, () ->
                teamService.removeMember(member, 10L, 99L)
        );
    }

    @Test
    public void managerCannotRemoveThemselves() {
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        TeamMembershipEntity managerMembership = new TeamMembershipEntity();
        managerMembership.setRole(TeamRole.MANAGER);
        when(membershipRepository.findByUserAndTeam(manager, team)).thenReturn(Optional.of(managerMembership));

        Exception exception = assertThrows(RuntimeException.class, () ->
                teamService.removeMember(manager, 10L, 1L)
        );
        assertTrue(exception.getMessage().contains("Managers cannot remove themselves"));
    }

    @Test
    public void shouldRejectEmptyTeamNameUpdate() {
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        TeamMembershipEntity managerMembership = new TeamMembershipEntity();
        managerMembership.setRole(TeamRole.MANAGER);
        when(membershipRepository.findByUserAndTeam(manager, team)).thenReturn(Optional.of(managerMembership));

        assertThrows(RuntimeException.class, () ->
                teamService.updateTeamName(10L, "", manager)
        );
        assertThrows(RuntimeException.class, () ->
                teamService.updateTeamName(10L, "   ", manager)
        );
    }

    @Test
    public void shouldRejectPasswordChangeWithWrongCurrentPassword() {
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        TeamMembershipEntity managerMembership = new TeamMembershipEntity();
        managerMembership.setRole(TeamRole.MANAGER);
        when(membershipRepository.findByUserAndTeam(manager, team)).thenReturn(Optional.of(managerMembership));

        when(passwordEncoder.matches("WrongCurrent", "EncodedPass")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () ->
                teamService.changeTeamPassword(10L, "WrongCurrent", "NewStrongPass1!", manager)
        );
        assertEquals("Current password is incorrect", exception.getMessage());
    }
}