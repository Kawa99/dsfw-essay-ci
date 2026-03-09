//package com.team_proj.dsfw_team_proj.auth_test;
//
//import com.team_proj.dsfw_team_proj.auth.UserEntity;
//import com.team_proj.dsfw_team_proj.teams.TeamServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//public class TeamValidationTest {
//
//    @Autowired
//    private TeamServiceImpl teamService;
//
//    @Test
//    public void shouldRejectWeakPasswordWithoutSpecialChar() {
//        UserEntity user = new UserEntity();
//        user.setId(1L);
//
//        assertThrows(RuntimeException.class, () ->
//                teamService.createTeam("Name", "Desc", "WeakPass123", user)
//        );
//    }
//
//    @Test
//    public void shouldRejectWeakPasswordTooShort() {
//        UserEntity user = new UserEntity();
//        user.setId(1L);
//
//        assertThrows(RuntimeException.class, () ->
//                teamService.createTeam("Name", "Desc", "Short1!", user)
//        );
//    }
//
//    @Test
//    public void shouldRejectWeakPasswordNoUppercase() {
//        UserEntity user = new UserEntity();
//        user.setId(1L);
//
//        assertThrows(RuntimeException.class, () ->
//                teamService.createTeam("Name", "Desc", "lower123!", user)
//        );
//    }
//}