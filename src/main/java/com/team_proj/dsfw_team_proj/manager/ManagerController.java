package com.team_proj.dsfw_team_proj.manager;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import com.team_proj.dsfw_team_proj.teams.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;
    private final TeamService teamService;
    private final UserService userService;

    public ManagerController(ManagerService managerService, TeamService teamService, UserService userService) {
        this.managerService = managerService;
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping("/overview/{teamId}")
    public String showManagerOverview(@PathVariable Long teamId, Model model, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());

        if (!teamService.isManager(user, teamId)) {
            return "redirect:/home";
        }

        List<TeamMemberDTO> members = managerService.getTeamMembers(teamId);

        model.addAttribute("teamId", teamId);
        model.addAttribute("employees", members);

        return "manager/overview";
    }
}