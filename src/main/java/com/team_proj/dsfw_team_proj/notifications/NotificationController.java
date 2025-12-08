package com.team_proj.dsfw_team_proj.notifications;


import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserRepository;
import com.team_proj.dsfw_team_proj.auth.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class NotificationController {

    NotificationService notificationService;
    UserService userService;
    UserRepository userRepository;

    public NotificationController(NotificationService notificationService,
                                  UserService userService,
                                  UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/notification")
    public ModelAndView getNotification(Authentication authentication, Model model) {
        ModelAndView modelAndView;

        if (authentication == null || !authentication.isAuthenticated()) {
            modelAndView = new ModelAndView("redirect:/login");
        }else{

            modelAndView = new ModelAndView("notification/notification");

            UserEntity user = userService.getCurrentUser();

            List<Notification> notifications = notificationService.getUserNotifications(user);
            modelAndView.addObject("notifications", notifications);

        }

        return modelAndView;
    }


}
