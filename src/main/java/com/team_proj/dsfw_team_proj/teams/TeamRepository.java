package com.team_proj.dsfw_team_proj.teams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Integer> {
    Optional<TeamEntity> findbyJoinCode(String joinCode);
}
