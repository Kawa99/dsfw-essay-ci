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
    public String createTeam(
            @RequestParam String teamName,
            @RequestParam String description,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            Principal principal,
            Model model
    ) {
        // Validate passwords match
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error", "Passwords do not match");
            return "teams/create-team";
        }

        // Validate password strength
        String passwordError = validatePassword(password);
        if (passwordError != null) {
            model.addAttribute("error", passwordError);
            return "teams/create-team";
        }

        UserEntity user = userService.findByEmail(principal.getName());
        TeamEntity newTeam = teamService.createTeam(teamName, description, password, user);
        return "redirect:/manager/homepage/" + newTeam.getId();
    }

    @PostMapping("/teams/join")
    public String joinTeam(
            @RequestParam String joinCode,
            @RequestParam String password,
            Principal principal
    ) {
        UserEntity user = userService.findByEmail(principal.getName());
        try {
            teamService.joinTeam(joinCode, password, user);
        } catch (RuntimeException e) {
            return "redirect:/home?error=" + e.getMessage();
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
    @PostMapping("/teams/leave/{teamId}")
    public String leaveTeam(@PathVariable Long teamId, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());

        try {
            teamService.leaveTeam(user, teamId);
        } catch (RuntimeException e) {
            return "redirect:/my-teams?error=" + e.getMessage();
        }
        return "redirect:/my-teams?success=left_team";
    }

    @GetMapping("/teams/create")
    public String showCreateTeamForm(Model model) {
        return "teams/create-team";
    }

    private String validatePassword(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }

        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }

        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one number";
        }

        if (!password.matches(".*[!@#$%^&*()_+<>/?;:'\"\\\\|><].*")) {
            return "Password must contain at least one special character (!@#$%^&*()_+<>/?;:'\"|><)";
        }

        return null; // null means valid
    }
}