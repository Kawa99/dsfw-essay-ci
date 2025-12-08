package com.team_proj.dsfw_team_proj.notifications;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByTimestampDesc(UserEntity user);

    long countByUserAndIsReadFalse(UserEntity user);
}
