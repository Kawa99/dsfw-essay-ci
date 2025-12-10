package com.team_proj.dsfw_team_proj.notification;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.notifications.Notification;
import com.team_proj.dsfw_team_proj.notifications.NotificationRepository;
import com.team_proj.dsfw_team_proj.notifications.NotificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void getUnreadCountDelegatesToRepository() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(notificationRepository.countByUserAndIsReadFalse(user)).thenReturn(3L);

        long result = notificationService.getUnreadCount(user);

        assertThat(result).isEqualTo(3L);
        verify(notificationRepository).countByUserAndIsReadFalse(user);
    }

    @Test
    void markAllAsReadSetsFlagsAndSaves() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        Notification n1 = new Notification();
        n1.setUser(user);
        n1.setMessage("One");
        n1.setTimestamp(LocalDateTime.now().minusMinutes(2));
        n1.setRead(false);

        Notification n2 = new Notification();
        n2.setUser(user);
        n2.setMessage("Two");
        n2.setTimestamp(LocalDateTime.now().minusMinutes(1));
        n2.setRead(false);

        when(notificationRepository.findByUserAndIsReadFalse(user))
                .thenReturn(List.of(n1, n2));

        notificationService.markAllAsRead(user);

        assertThat(n1.isRead()).isTrue();
        assertThat(n2.isRead()).isTrue();

        verify(notificationRepository).saveAll(List.of(n1, n2));
    }
}
