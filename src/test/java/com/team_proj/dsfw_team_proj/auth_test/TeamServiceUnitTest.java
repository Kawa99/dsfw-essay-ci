package com.team_proj.dsfw_team_proj.auth_test;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.teams.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceUnitTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMembershipRepository membershipRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TeamServiceImpl teamService;

    private UserEntity user;
    private UserEntity manager;
    private TeamEntity team;

    @BeforeEach
    public void setup() {
        user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@gov.uk");
        user.setRole("USER");

        manager = new UserEntity();
        manager.setId(2L);
        manager.setEmail("manager@gov.uk");

        team = new TeamEntity();
        team.setId(10L);
        team.setTeamName("Alpha Team");
        team.setPassword("EncodedPass");
    }

    // --- Happy Path Tests ---

    @Test
    public void shouldCreateTeamAndAssignManagerRole() {
        String rawPassword = "StrongPassword1!";
        String encodedPassword = "encoded_password";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(teamRepository.save(any(TeamEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        TeamEntity result = teamService.createTeam("Ops Team", "Description", rawPassword, user);

        assertNotNull(result);
        assertNotNull(result.getJoinCode());
        assertEquals(6, result.getJoinCode().length());
        assertEquals("Ops Team", result.getTeamName());
        assertEquals(encodedPassword, result.getPassword());

        verify(membershipRepository).save(argThat(membership ->
                membership.getUser().equals(user) &&
                        membership.getTeam().equals(result) &&
                        membership.getRole() == TeamRole.MANAGER
        ));
    }

    // --- Password Validation Tests ---

    @Test
    public void shouldRejectNullOrEmptyPassword() {
        assertThrows(RuntimeException.class, () ->
                teamService.createTeam("Team", "Desc", null, user));

        assertThrows(RuntimeException.class, () ->
                teamService.createTeam("Team", "Desc", "", user));
    }

    @Test
    public void shouldRejectShortPassword() {
        Exception e = assertThrows(RuntimeException.class, () ->
                teamService.createTeam("Team", "Desc", "Short1!", user));
        assertEquals("Password must be at least 8 characters", e.getMessage());
    }

    @Test
    public void shouldRejectPasswordWithoutUppercase() {
        Exception e = assertThrows(RuntimeException.class, () ->
                teamService.createTeam("Team", "Desc", "lower123!", user));
        assertEquals("Password must contain at least one uppercase letter", e.getMessage());
    }

    @Test
    public void shouldRejectPasswordWithoutLowercase() {
        Exception e = assertThrows(RuntimeException.class, () ->
                teamService.createTeam("Team", "Desc", "UPPER123!", user));
        assertEquals("Password must contain at least one lowercase letter", e.getMessage());
    }

    @Test
    public void shouldRejectPasswordWithoutNumber() {
        Exception e = assertThrows(RuntimeException.class, () ->
                teamService.createTeam("Team", "Desc", "NoNumber!", user));
        assertEquals("Password must contain at least one number", e.getMessage());
    }

    @Test
    public void shouldRejectPasswordWithoutSpecialChar() {
        Exception e = assertThrows(RuntimeException.class, () ->
                teamService.createTeam("Team", "Desc", "NoSpecial1", user));
        assertEquals("Password must contain at least one special character", e.getMessage());
    }

    // --- Join Team Edge Cases ---

    @Test
    public void shouldThrowExceptionIfJoinCodeIsInvalid() {
        when(teamRepository.findByJoinCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                teamService.joinTeam("INVALID", "pass", user)
        );
    }

    @Test
    public void shouldThrowExceptionIfUserAlreadyMember() {
        TeamEntity team = new TeamEntity();
        team.setJoinCode("CODE12");
        team.setPassword("encoded");

        when(teamRepository.findByJoinCode("CODE12")).thenReturn(Optional.of(team));
        when(passwordEncoder.matches("pass", "encoded")).thenReturn(true);
        when(membershipRepository.existsByUserAndTeam(user, team)).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                teamService.joinTeam("CODE12", "pass", user)
        );
    }

    @Test
    public void shouldFailJoinWithIncorrectPassword() {
        team.setJoinCode("CODE12");
        when(teamRepository.findByJoinCode("CODE12")).thenReturn(Optional.of(team));
        when(passwordEncoder.matches("WrongPass", "EncodedPass")).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () ->
                teamService.joinTeam("CODE12", "WrongPass", user)
        );
        assertEquals("Incorrect password", exception.getMessage());
    }

    // --- Retry Logic / Data Integrity ---

    @Test
    public void shouldRetryGeneratingJoinCodeOnDatabaseCollision() {
        String rawPassword = "StrongPassword1!";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encoded_hash");

        when(teamRepository.save(any(TeamEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate Join Code"))
                .thenReturn(new TeamEntity());

        TeamEntity result = teamService.createTeam("Retry Team", "Desc", rawPassword, user);

        assertNotNull(result);
        verify(teamRepository, times(2)).save(any(TeamEntity.class));
    }

    @Test
    public void shouldThrowExceptionAfterMaxRetriesForJoinCode() {
        String rawPassword = "StrongPassword1!";
        when(passwordEncoder.encode(rawPassword)).thenReturn("encoded_hash");

        when(teamRepository.save(any(TeamEntity.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate"));

        assertThrows(RuntimeException.class, () ->
                teamService.createTeam("Name", "Desc", rawPassword, user)
        );
        verify(teamRepository, times(5)).save(any());
    }

    // --- Management Logic Edge Cases ---

    @Test
    public void isManager_ShouldReturnFalseIfTeamOrMembershipNotFound() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(teamService.isManager(manager, 99L));

        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));
        when(membershipRepository.findByUserAndTeam(manager, team)).thenReturn(Optional.empty());
        assertFalse(teamService.isManager(manager, 10L));
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
    public void updateTeamDescription_ShouldThrowIfNull() {
        when(teamRepository.findById(10L)).thenReturn(Optional.of(team));

        TeamMembershipEntity membership = new TeamMembershipEntity();
        membership.setRole(TeamRole.MANAGER);
        when(membershipRepository.findByUserAndTeam(manager, team)).thenReturn(Optional.of(membership));

        assertThrows(RuntimeException.class, () ->
                teamService.updateTeamDescription(10L, null, manager)
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