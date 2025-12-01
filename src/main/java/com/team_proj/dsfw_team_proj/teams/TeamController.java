package com.team_proj.dsfw_team_proj.teams;

import org.springframework.ui.Model;
import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamRepository teamRepository;

    @PostMapping("/teams/create")
    public String createTeam (@RequestParam(defaultValue = "My Team") String teamName, Principal principal){
        UserEntity user = userService.findByEmail(principal.getName());
        TeamEntity newTeam = teamService.createTeam(teamName, user);
        return "redirect:/manager/homepage/" + newTeam.getId();
    }
    @GetMapping("/manager/homepage/{teamId}")
    public String showManagerHomepage(@PathVariable Long teamId, Model model) {
        TeamEntity team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        model.addAttribute("team", team);
        return "manager/manager-homepage";
    }
}
