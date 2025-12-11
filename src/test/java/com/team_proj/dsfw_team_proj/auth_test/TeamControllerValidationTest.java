package com.team_proj.dsfw_team_proj.auth_test;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeamControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "user@gov.uk")
    public void shouldFailCreationWhenPasswordsDoNotMatch() throws Exception {
        UserEntity user = new UserEntity();
        user.setEmail("user@gov.uk");
        when(userService.findByEmail("user@gov.uk")).thenReturn(user);

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
}