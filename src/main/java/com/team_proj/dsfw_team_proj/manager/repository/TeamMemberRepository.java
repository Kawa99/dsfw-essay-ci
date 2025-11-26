package com.team_proj.dsfw_team_proj.manager.repository;

import com.team_proj.dsfw_team_proj.manager.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    // Find all members managed by a specific manager
    List<TeamMember> findByManagerId(Long managerId);
}