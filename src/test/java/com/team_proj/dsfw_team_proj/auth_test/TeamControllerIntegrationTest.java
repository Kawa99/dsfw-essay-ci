package com.team_proj.dsfw_team_proj.auth_test;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import com.team_proj.dsfw_team_proj.teams.TeamEntity;
import com.team_proj.dsfw_team_proj.teams.TeamRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}