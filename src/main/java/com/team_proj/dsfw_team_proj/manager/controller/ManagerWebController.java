package com.team_proj.dsfw_team_proj.manager.controller;

// Logic and Data Imports
import com.team_proj.dsfw_team_proj.manager.dto.ManagerResultDTO;
import com.team_proj.dsfw_team_proj.manager.entity.Manager;
import com.team_proj.dsfw_team_proj.manager.entity.TeamMember;
import com.team_proj.dsfw_team_proj.manager.repository.ManagerRepository;
import com.team_proj.dsfw_team_proj.manager.repository.TeamMemberRepository;
import com.team_proj.dsfw_team_proj.manager.service.ManagerService;

// Spring MVC Imports
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller; // Note: NOT RestController
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ManagerWebController {

    private final ManagerRepository managerRepo;
    private final TeamMemberRepository teamMemberRepo;
    private final ManagerService managerService;

    // View Team List
    @GetMapping("/dashboard/manager/{managerId}")
    public String showTeamDashboard(@PathVariable Long managerId, Model model) {
        Manager manager = managerRepo.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        List<TeamMember> team = teamMemberRepo.findByManagerId(managerId);

        model.addAttribute("manager", manager);
        model.addAttribute("teamMembers", team);

        return "manager-dashboard"; // Maps to manager-dashboard.html
    }

    // View Individual Results
    @GetMapping("/dashboard/member/{memberId}/results")
    public String showMemberResults(@PathVariable Long memberId, Model model) {
        TeamMember member = teamMemberRepo.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<ManagerResultDTO> results = managerService.getResultsForTeamMember(memberId);

        model.addAttribute("member", member);
        model.addAttribute("results", results);

        return "member-results"; // Maps to member-results.html
    }
}