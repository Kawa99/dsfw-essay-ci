package com.team_proj.dsfw_team_proj.manager;

import com.team_proj.dsfw_team_proj.teams.TeamMembershipEntity;
import com.team_proj.dsfw_team_proj.teams.TeamMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private TeamMembershipRepository membershipRepository;

    @Override
    public List<TeamMemberDTO> getTeamMembers(Long teamId) {
        List<TeamMembershipEntity> memberships = membershipRepository.findAllByTeamId(teamId);

        return memberships.stream().map(m -> new TeamMemberDTO(m.getUser().getId(), m.getUser().getFirstName() + " " + m.getUser().getLastName(), m.getRole().name(), null)).collect(Collectors.toList());
    }
    public Map<String, Object> getTeamStats(Long teamId){
        Map<String,Object> stats = new HashMap<>();
        int totalMembers = membershipRepository.findAllByTeamId(teamId).size();
        stats.put("totalMembers", totalMembers);
        // Additional stats can be calculated and added here
        stats.put("completedAssessments", 0); // placeholder, todo: link to assessmentrepository
        stats.put("pendingAssessments", 0); // placeholder todo: calculate from assessmentrepository
        stats.put("averageScore", 0); // placeholder todo: calculate from assessmentrepository
        return stats;
    }

    public List<Map<String, Object>> getSkillGaps(Long teamId){
        // Returning empty list or dummy data so the page doesn't crash
        // Once you have an Assessment Entity, query for lowest average scores here.
        List<Map<String, Object>> gaps = new ArrayList<>();
        //public List<Map<String, Object>> getSkillGaps(Long teamId){
            // Returning empty list or dummy data so the page doesn't crash
            // Once you have an Assessment Entity, query for lowest average scores here.
            //List<Map<String, Object>> gaps = new ArrayList<>();

            // Example structure for when you have real data:
            // Map<String, Object> gap = new HashMap<>();
            // gap.put("category", "Data Analysis");
            // gap.put("level", "Low");
            // gap.put("score", "30%");
            // gaps.add(gap);

            return gaps;
        }
}