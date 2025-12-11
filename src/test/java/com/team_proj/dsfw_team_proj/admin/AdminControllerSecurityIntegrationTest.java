package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerSecurityIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        UserEntity admin = new UserEntity();
        admin.setFirstName("Test");
        admin.setLastName("Admin");
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("Password@123"));
        admin.setRole("ADMIN");
        userRepository.save(admin);
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void givenAdminRole_whenAccessAdmin_thenOk() throws Exception {
        mvc.perform(get("/admin/self-assessment"))
                .andExpect(status().isOk());
    }
}
