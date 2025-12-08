package com.team_proj.dsfw_team_proj.notifications;


import com.team_proj.dsfw_team_proj.auth.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    public void sendNotification(UserEntity user, String message) {
        Notification notif = new Notification();
        notif.setUser(user);
        notif.setMessage(message);
        notificationRepository.save(notif);
    }

    public List<Notification> getUserNotifications(UserEntity user) {
        return notificationRepository.findByUserOrderByTimestampDesc(user);
    }

    // still working on this feature.
    public long countUnreadNotifications(UserEntity user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }


}
