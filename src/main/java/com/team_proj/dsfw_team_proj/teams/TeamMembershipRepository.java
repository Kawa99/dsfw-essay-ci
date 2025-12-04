package com.team_proj.dsfw_team_proj.teams;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMembershipRepository extends JpaRepository<TeamMembershipEntity, Long> {

    Optional<TeamMembershipEntity> findByUserAndTeam(UserEntity user, TeamEntity team);
    boolean existsByUserAndTeam(UserEntity user, TeamEntity team);
    List<TeamMembershipEntity> findByUser(UserEntity user);

    @Query("SELECT m FROM TeamMembershipEntity m JOIN FETCH m.user WHERE m.team.id = :teamId")
    List<TeamMembershipEntity> findAllByTeamId(Long teamId);

    Optional<TeamMembershipEntity> findByUserAndRole(UserEntity user, TeamRole role);
}