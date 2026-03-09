//package com.team_proj.dsfw_team_proj.auth_test;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class TeamSecurityTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void unauthenticatedUserCannotAccessTeamCreation() throws Exception {
//        mockMvc.perform(get("/teams/create"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("**/login"));
//    }
//
//    @Test
//    public void unauthenticatedUserCannotPostToCreateTeam() throws Exception {
//        mockMvc.perform(post("/teams/create")
//                        .param("teamName", "Hacker Team")
//                        .param("description", "Should fail")
//                        .param("password", "StrongPass1!")
//                        .param("passwordConfirm", "StrongPass1!")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("**/login"));
//    }
//
//    @Test
//    public void unauthenticatedUserCannotJoinTeamEvenWithValidCode() throws Exception {
//        mockMvc.perform(post("/teams/join")
//                        .param("joinCode", "VALID1")
//                        .param("password", "Pass123!")
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrlPattern("**/login"));
//    }
//}