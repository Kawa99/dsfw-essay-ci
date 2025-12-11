package com.team_proj.dsfw_team_proj.notifications;


import com.team_proj.dsfw_team_proj.auth.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name="is_read")
    private boolean isRead=false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
