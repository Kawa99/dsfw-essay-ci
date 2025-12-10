package com.team_proj.dsfw_team_proj.notifications;

import com.team_proj.dsfw_team_proj.auth.UserEntity;

import java.util.List;

public interface NotificationService {

    void sendNotification(UserEntity user, String message);

    List<Notification> getUserNotifications(UserEntity user);

    long getUnreadCount(UserEntity user);

    void markAllAsRead(UserEntity user);
}
