package com.team_proj.dsfw_team_proj.manager;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import com.team_proj.dsfw_team_proj.teams.TeamEntity;
import com.team_proj.dsfw_team_proj.teams.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.team_proj.dsfw_team_proj.teams.TeamEntity;
import com.team_proj.dsfw_team_proj.teams.TeamRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;
    private final TeamService teamService;
    private final UserService userService;
    private final TeamRepository teamRepository;

    public ManagerController(ManagerService managerService, TeamService teamService, UserService userService, TeamRepository teamRepository) {
        this.managerService = managerService;
        this.teamService = teamService;
        this.userService = userService;
        this.teamRepository = teamRepository;
    }

    @GetMapping("/overview/{teamId}")
    public String showManagerOverview(@PathVariable Long teamId, Model model, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());

        if (!teamService.isManager(user, teamId)) {
            return "redirect:/home";
        }

        List<TeamMemberDTO> members = managerService.getTeamMembers(teamId);

        // Fetch team details for display and editing
        TeamEntity team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        model.addAttribute("teamId", teamId);
        model.addAttribute("team", team);  // NEW: add team object
        model.addAttribute("employees", members);

        return "manager/overview";
    }
}