package com.team_proj.dsfw_team_proj.selfassessment;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AssessmentSubmissionRepositoryTest {

    @Autowired
    private AssessmentSubmissionRepository submissionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByUserOrdersByMostRecentFirst() {


        UserEntity user = new UserEntity();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("user@test.com");
        entityManager.persist(user);


        AssessmentSubmission older = new AssessmentSubmission();
        older.setUser(user);
        older.setSubmittedAt(LocalDateTime.now().minusDays(1));
        entityManager.persist(older);


        AssessmentSubmission newer = new AssessmentSubmission();
        newer.setUser(user);
        newer.setSubmittedAt(LocalDateTime.now());
        entityManager.persist(newer);

        entityManager.flush();


        List<AssessmentSubmission> results =
                submissionRepository.findByUserOrderBySubmittedAtDesc(user);


        assertEquals(2, results.size());
        assertEquals(newer.getId(), results.get(0).getId());
        assertEquals(older.getId(), results.get(1).getId());
    }
}
