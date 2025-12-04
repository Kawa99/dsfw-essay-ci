package com.team_proj.dsfw_team_proj.manager;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import com.team_proj.dsfw_team_proj.auth.UserService;
import com.team_proj.dsfw_team_proj.teams.TeamEntity;
import com.team_proj.dsfw_team_proj.teams.TeamRepository;
import com.team_proj.dsfw_team_proj.teams.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        model.addAttribute("teamId", teamId);
        model.addAttribute("employees", members);

        return "manager/overview";
    }

    @GetMapping("/dashboard")
    public String showManagerDashboard(Model model, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());

        Long teamId = teamService.getTeamIdByManager(user);
        if (teamId == null) {
            return "redirect:/home";
        }

        TeamEntity team = teamRepository.findById(teamId).orElse(null);
        if (team == null) {
            return "redirect:/home";
        }

        List<TeamMemberDTO> members = managerService.getTeamMembers(teamId);

        Map<String, Object> stats = managerService.getTeamStats(teamId);
        List<Map<String, Object>> skillGaps = managerService.getSkillGaps(teamId);

        model.addAttribute("team", team);        // Needed for ${team.teamName}
        model.addAttribute("members", members);  // Needed for table listing
        model.addAttribute("stats", stats);      // Needed for dashboard cards
        model.addAttribute("skillGaps", skillGaps); // Needed for Skill Gaps table

        return "manager/manager-dashboard";
    }
}
