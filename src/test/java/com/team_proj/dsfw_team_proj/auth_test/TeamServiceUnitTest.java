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

    @BeforeEach
    public void setup() {
        user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@gov.uk");
        user.setRole("USER");
    }

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

    @Test
    public void shouldThrowExceptionForWeakPassword() {
        assertThrows(RuntimeException.class, () ->
                teamService.createTeam("Team", "Desc", "weak", user)
        );
    }

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
}