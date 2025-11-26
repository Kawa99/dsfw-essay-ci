package com.team_proj.dsfw_team_proj.manager.service;

import com.team_proj.dsfw_team_proj.manager.dto.ManagerResultDTO;
import com.team_proj.dsfw_team_proj.manager.entity.Assessment;
import com.team_proj.dsfw_team_proj.manager.repository.AssessmentRepository;
import com.team_proj.dsfw_team_proj.self_assessment.Skills;
import com.team_proj.dsfw_team_proj.self_assessment.Category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
// FIX IS HERE: implements ManagerService (NOT ManagerServiceImpl)
public class ManagerServiceImpl implements ManagerService {

    private final AssessmentRepository assessmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ManagerResultDTO> getResultsForTeamMember(Long memberId) {
        List<Assessment> assessments = assessmentRepository.findByTeamMemberId(memberId);

        return assessments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ManagerResultDTO mapToDTO(Assessment assessment) {
        Skills skill = assessment.getSkill();

        String catName = "General";
        if (skill.getCategory() != null) {
            catName = skill.getCategory().getName();
        }

        return ManagerResultDTO.builder()
                .skillName(skill.getName())
                .categoryName(catName)
                .score(assessment.getScore())
                .gapAnalysis(calculateGap(assessment.getScore()))
                .build();
    }

    private String calculateGap(int score) {
        if (score >= 4) return "Expert - Can Teach Others";
        if (score == 3) return "Practitioner - Capable";
        return "GAP IDENTIFIED: Training Required";
    }
}