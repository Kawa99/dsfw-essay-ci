//package com.team_proj.dsfw_team_proj.notification;
//
//import com.team_proj.dsfw_team_proj.auth.UserEntity;
//import com.team_proj.dsfw_team_proj.auth.UserRepository;
//import com.team_proj.dsfw_team_proj.notifications.Notification;
//import com.team_proj.dsfw_team_proj.notifications.NotificationRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//class NotificationRepositoryTest {
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void findByUserOrderByTimestampDescReturnsNewestFirst() {
//
//        UserEntity user = new UserEntity();
//        user.setFirstName("test");
//        user.setLastName("test");
//        user.setEmail("user@test.com");
//        user = userRepository.save(user);
//
//        Notification older = new Notification();
//        older.setUser(user);
//        older.setMessage("old");
//        older.setTimestamp(LocalDateTime.now().minusMinutes(5));
//        older.setRead(false);
//
//        Notification newer = new Notification();
//        newer.setUser(user);
//        newer.setMessage("new");
//        newer.setTimestamp(LocalDateTime.now());
//        newer.setRead(false);
//
//        notificationRepository.saveAll(List.of(older, newer));
//
//        List<Notification> result = notificationRepository.findByUserOrderByTimestampDesc(user);
//
//
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getMessage()).isEqualTo("new");
//        assertThat(result.get(1).getMessage()).isEqualTo("old");
//    }
//
//    @Test
//    void countByUserAndIsReadFalseCountsOnlyUnread() {
//
//        UserEntity user = new UserEntity();
//        user.setFirstName("test");
//        user.setLastName("test");
//        user.setEmail("user@test.com");
//        user = userRepository.save(user);
//
//        Notification unread = new Notification();
//        unread.setUser(user);
//        unread.setMessage("unread");
//        unread.setRead(false);
//
//        Notification read = new Notification();
//        read.setUser(user);
//        read.setMessage("read");
//        read.setRead(true);
//
//        notificationRepository.saveAll(List.of(unread, read));
//
//        long count = notificationRepository.countByUserAndIsReadFalse(user);
//
//
//        assertThat(count).isEqualTo(1);
//    }
//}
