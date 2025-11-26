package com.team_proj.dsfw_team_proj.manager.service;

import com.team_proj.dsfw_team_proj.manager.dto.ManagerResultDTO;
import java.util.List;

public interface ManagerService {

    // This is the method your Controller is trying to call
    List<ManagerResultDTO> getResultsForTeamMember(Long memberId);

}