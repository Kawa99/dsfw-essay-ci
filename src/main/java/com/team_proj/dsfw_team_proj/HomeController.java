package com.team_proj.dsfw_team_proj;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import com.team_proj.dsfw_team_proj.teams.TeamMembershipEntity;
import com.team_proj.dsfw_team_proj.teams.TeamMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamMembershipRepository membershipRepository;

    @GetMapping("/")
    public String home() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(Model model, Principal principal) {
        if (principal != null) {
            UserEntity user = userService.findByEmail(principal.getName());
            List<TeamMembershipEntity> myTeams = membershipRepository.findByUser(user);
            model.addAttribute("myMemberships", myTeams);
        }
        return "home";
    }
}