package com.team_proj.dsfw_team_proj.notification;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserRepository;
import com.team_proj.dsfw_team_proj.auth.UserService;
import com.team_proj.dsfw_team_proj.notifications.Notification;
import com.team_proj.dsfw_team_proj.notifications.NotificationController;
import com.team_proj.dsfw_team_proj.notifications.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


//This test uses controller only approach
@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private NotificationController controller;

    @Test
    void unauthenticatedUserIsRedirectedToLogin() {
        ExtendedModelMap model = new ExtendedModelMap();

        when(authentication.isAuthenticated()).thenReturn(false);

        ModelAndView mav = controller.getNotification(authentication, model);

        assertThat(mav.getViewName()).isEqualTo("redirect:/login");
        verifyNoInteractions(notificationService);
    }

    @Test
    void authenticatedUserSeesNotificationPageAndMarksRead() {
        ExtendedModelMap model = new ExtendedModelMap();

        when(authentication.isAuthenticated()).thenReturn(true);

        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage("Test");

        when(userService.getCurrentUser()).thenReturn(user);
        when(notificationService.getUserNotifications(user))
                .thenReturn(List.of(notification));

        ModelAndView mav = controller.getNotification(authentication, model);

        assertThat(mav.getViewName()).isEqualTo("notification/notification");
        assertThat(mav.getModel()).containsKey("notifications");

        verify(notificationService).getUserNotifications(user);
        verify(notificationService).markAllAsRead(user);
    }
}
