package com.team_proj.dsfw_team_proj.auth_test;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import com.team_proj.dsfw_team_proj.teams.TeamEntity;
import com.team_proj.dsfw_team_proj.teams.TeamRepository;
import com.team_proj.dsfw_team_proj.teams.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TeamControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Test
    @WithMockUser(username = "manager@gov.uk")
    public void authenticatedUserCanCreateTeamAndPersistDetails() throws Exception {
        UserEntity manager = new UserEntity();
        manager.setFirstName("Boss");
        manager.setLastName("Man");
        manager.setEmail("manager@gov.uk");
        manager.setPassword("Pass123!");
        userService.save(manager);

        mockMvc.perform(post("/teams/create")
                        .param("teamName", "Alpha Squadron")
                        .param("description", "Top secret project")
                        .param("password", "SecurePass1!")
                        .param("passwordConfirm", "SecurePass1!")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/manager-homepage"));

        List<TeamEntity> teams = teamRepository.findAll();
        assertFalse(teams.isEmpty());

        TeamEntity savedTeam = teams.stream()
                .filter(t -> t.getTeamName().equals("Alpha Squadron"))
                .findFirst()
                .orElseThrow();

        assertEquals("Top secret project", savedTeam.getDescription());
        assertNotNull(savedTeam.getJoinCode());
        assertEquals(6, savedTeam.getJoinCode().length());
        assertNotEquals("SecurePass1!", savedTeam.getPassword());
    }

    @Test
    @WithMockUser(username = "user@gov.uk")
    public void shouldFailCreationWhenPasswordsDoNotMatch() throws Exception {
        UserEntity user = new UserEntity();
        user.setEmail("user@gov.uk");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("pass");
        userService.save(user);

        mockMvc.perform(post("/teams/create")
                        .param("teamName", "Mis-match Team")
                        .param("description", "Desc")
                        .param("password", "Password123!")
                        .param("passwordConfirm", "Different123!")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("teams/create-team"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Passwords do not match"));
    }

    @Test
    @WithMockUser(username = "user@gov.uk")
    public void shouldReturnForbiddenWhenNonManagerUpdatesTeam() throws Exception {
        // 1. Create a Manager and a Team
        UserEntity manager = new UserEntity();
        manager.setEmail("manager@gov.uk");
        manager.setFirstName("M"); manager.setLastName("M"); manager.setPassword("p");
        userService.save(manager);
        TeamEntity team = teamService.createTeam("Target Team", "Desc", "Pass123!", manager);

        // 2. Create a normal User who tries to hack/update the team
        UserEntity hacker = new UserEntity();
        hacker.setEmail("user@gov.uk");
        hacker.setFirstName("H"); hacker.setLastName("H"); hacker.setPassword("p");
        userService.save(hacker);

        // 3. Attempt update as 'user@gov.uk' (the hacker)
        mockMvc.perform(put("/teams/" + team.getId() + "/name")
                        .param("newName", "Hacked Name")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@gov.uk")
    public void shouldReturnBadRequestIfConfirmPasswordMismatch() throws Exception {
        // Setup user and team
        UserEntity user = new UserEntity();
        user.setEmail("user@gov.uk");
        user.setFirstName("U"); user.setLastName("K"); user.setPassword("p");
        userService.save(user);
        TeamEntity team = teamService.createTeam("My Team", "Desc", "Pass123!", user);

        mockMvc.perform(put("/teams/" + team.getId() + "/password")
                        .param("currentPassword", "Pass123!")
                        .param("newPassword", "NewPass1!")
                        .param("confirmPassword", "Mismatch!")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}