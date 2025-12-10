package com.team_proj.dsfw_team_proj.selfassessment;

import com.team_proj.dsfw_team_proj.auth.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "assessment_submissions")
public class AssessmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime submittedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private List<AssessmentResponse> responses = new ArrayList<>();

    public Map<String, List<AssessmentResponse>> getResponsesGroupedByCategory() {
        return responses.stream().collect(Collectors.groupingBy(r -> r.getSkill().getCategory().getName()));
    }
}