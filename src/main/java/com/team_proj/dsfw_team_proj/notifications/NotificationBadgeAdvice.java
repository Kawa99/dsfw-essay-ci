package com.team_proj.dsfw_team_proj.notifications;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NotificationBadgeAdvice {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationBadgeAdvice(NotificationService notificationService,
                                   UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @ModelAttribute("unreadNotificationCount")
    public Long addUnreadNotificationCount(Authentication authentication) {
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return 0L;
        }

        UserEntity user = userService.getCurrentUser();
        return notificationService.getUnreadCount(user);
    }
}
