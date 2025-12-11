package com.team_proj.dsfw_team_proj.selfassessment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/charts")
@RequiredArgsConstructor
public class ChartsController {

    private final SkillRepository skillRepository;
    private final AssessmentResponseRepository assessmentResponseRepository;

    @GetMapping("/skills-average")
    public ChartDataDTO getSkillAverages() {
        // fetch all skills and responses from the database
        List<SkillsEntity> skills = skillRepository.findAll();
        List<AssessmentResponse> responses = assessmentResponseRepository.findAll();

        // Groups responses by Skill ID and calculate the average score
        Map<Long, Double> averageScores = responses.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getSkill().getId(),
                        Collectors.averagingInt(AssessmentResponse::getScore)
                ));

        // prepare the lists for the DTO
        List<String> labels = new ArrayList<>();
        List<Double> dataPoints = new ArrayList<>();

        for (SkillsEntity skill : skills) {
            labels.add(skill.getName());

            Double avg = averageScores.getOrDefault(skill.getId(), 0.0);
            dataPoints.add(avg);
        }

        ChartDataDTO.Dataset dataset = ChartDataDTO.Dataset.builder()
                .label("Average Team Competency")
                .data(dataPoints)
                .backgroundColor("rgba(54, 162, 235, 0.5)")
                .borderColor("rgba(54, 162, 235, 1)")
                .borderWidth(1)
                .build();

        return ChartDataDTO.builder()
                .labels(labels)
                .datasets(List.of(dataset))
                .build();
    }
}