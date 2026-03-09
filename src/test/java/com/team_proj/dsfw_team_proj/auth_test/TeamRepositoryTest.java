//package com.team_proj.dsfw_team_proj.auth_test;
//
//import com.team_proj.dsfw_team_proj.teams.TeamEntity;
//import com.team_proj.dsfw_team_proj.teams.TeamRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//public class TeamRepositoryTest {
//
//    @Autowired
//    private TeamRepository teamRepository;
//
//    @Test
//    public void shouldFindTeamByJoinCode() {
//        TeamEntity team = new TeamEntity();
//        team.setTeamName("Find Me");
//        team.setJoinCode("ABC123");
//        team.setPassword("hashed");
//        teamRepository.save(team);
//
//        Optional<TeamEntity> found = teamRepository.findByJoinCode("ABC123");
//
//        assertTrue(found.isPresent());
//        assertEquals("Find Me", found.get().getTeamName());
//    }
//
//    @Test
//    public void shouldReturnEmptyIfJoinCodeDoesNotExist() {
//        Optional<TeamEntity> found = teamRepository.findByJoinCode("NONONO");
//        assertFalse(found.isPresent());
//    }
//}