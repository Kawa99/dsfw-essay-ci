//package com.team_proj.dsfw_team_proj.teams;
//
//
//import com.team_proj.dsfw_team_proj.auth.UserEntity;
//import com.team_proj.dsfw_team_proj.auth.UserRepository;
//import com.team_proj.dsfw_team_proj.notifications.Notification;
//import com.team_proj.dsfw_team_proj.notifications.NotificationRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//@SpringBootTest
//@Transactional
//public class TeamServiceNotificationIntegrationTest {
//
//    @Autowired
//    private TeamService teamService;
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Autowired
//    private TeamRepository teamRepository;
//
//    @Autowired
//    TeamMembershipRepository teamMembershipRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//
//    @Test
//    void createsColumnInDatabaseWhenJoinTeam(){
//
//        UserEntity user = new UserEntity();
//        user.setFirstName("firstName");
//        user.setLastName("lastName");
//        user.setEmail("user@test.com");
//        userRepository.save(user);
//
//        UserEntity manager = new UserEntity();
//        manager.setFirstName("firstName");
//        manager.setLastName("lastName");
//        manager.setEmail("manager@test.com");
//        userRepository.save(manager);
//
//        String joinCode = "TEST123";
//        String teamPass = "test123";
//
//        TeamEntity team = new TeamEntity();
//        team.setJoinCode(joinCode);
//        team.setPassword(teamPass);
//        teamRepository.save(team);
//        team.setPassword(passwordEncoder.encode(teamPass));
//        team.setJoinCode(joinCode);
//        team = teamRepository.save(team);
//
//        TeamMembershipEntity managerMembership = new TeamMembershipEntity();
//        managerMembership.setTeam(team);
//        managerMembership.setUser(manager);
//        managerMembership.setRole(TeamRole.MANAGER);
//        teamMembershipRepository.save(managerMembership);
//
//        teamService.joinTeam(joinCode, teamPass, user);
//
//        List<Notification> userNotifications =
//                notificationRepository.findByUserOrderByTimestampDesc(user);
//        List<Notification> managerNotifications =
//                notificationRepository.findByUserOrderByTimestampDesc(manager);
//
//        assertFalse(userNotifications.isEmpty(), "Expected a notification for the joining user");
//        assertFalse(managerNotifications.isEmpty(), "Expected a notification for the manager");
//
//
//    }
//}
