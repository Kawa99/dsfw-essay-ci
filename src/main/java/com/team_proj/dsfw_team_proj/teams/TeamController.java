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
import java.util.List;

@Controller
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMembershipRepository membershipRepository;

    @PostMapping("/teams/create")
    public String createTeam (@RequestParam(defaultValue = "My Team") String teamName, Principal principal){
        UserEntity user = userService.findByEmail(principal.getName());
        TeamEntity newTeam = teamService.createTeam(teamName, user);
        return "redirect:/manager/homepage/" + newTeam.getId();
    }

    @PostMapping("/teams/join")
    public String joinTeam(@RequestParam String joinCode, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        try {
            teamService.joinTeam(joinCode, user);
        } catch (RuntimeException e) {
            return "redirect:/home?error=invalid_code";
        }
        return "redirect:/my-teams";
    }

    @GetMapping("/manager/homepage/{teamId}")
    public String showManagerHomepage(@PathVariable Long teamId, Model model, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());

        if (!teamService.isManager(user, teamId)) {
            return "redirect:/home";
        }

        TeamEntity team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        model.addAttribute("team", team);
        return "manager/manager-homepage";
    }

    @GetMapping("/my-teams")
    public String showMyTeams(Model model, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        List<TeamMembershipEntity> myMemberships = membershipRepository.findByUser(user);
        model.addAttribute("myMemberships", myMemberships);
        return "teams/my-teams";
    }
    @PostMapping("/manager/delete/{teamId}")
    public String deleteTeam(@PathVariable Long teamId, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        if (!teamService.isManager(user, teamId)) {
            return "redirect:/home?error=unauthorized";
        }
            teamService.deleteTeam(teamId);
            return "redirect:/my-teams?success=team_deleted";
    }

}