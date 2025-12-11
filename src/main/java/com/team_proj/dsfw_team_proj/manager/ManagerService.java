package com.team_proj.dsfw_team_proj.manager;

import java.util.List;
import java.util.Map;

public interface ManagerService {
    List<TeamMemberDTO> getTeamMembers(Long teamId);

    List<Map<String, Object>> getSkillGaps(Long teamId);

    Map<String, Object> getTeamStats(Long teamId);
}