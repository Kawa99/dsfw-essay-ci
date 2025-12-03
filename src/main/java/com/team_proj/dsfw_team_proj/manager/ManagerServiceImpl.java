package com.team_proj.dsfw_team_proj.manager;

import com.team_proj.dsfw_team_proj.teams.TeamMembershipEntity;
import com.team_proj.dsfw_team_proj.teams.TeamMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}