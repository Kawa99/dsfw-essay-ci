package com.team_proj.dsfw_team_proj.selfassessment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/api/charts")
@RequiredArgsConstructor
public class ChartsController {

    private final CategoryRepository categoryRepository;
    private final AssessmentResponseRepository assessmentResponseRepository;
    private final AssessmentSubmissionRepository assessmentSubmissionRepository;

    @GetMapping("/skills-average")
    public ChartDataDTO getSkillAverages() {
        // fetch all skills and responses from the database
        List<Category> categories = categoryRepository.findAll();
        List<AssessmentResponse> responses = assessmentResponseRepository.findAll();

        // Groups responses by category ID and calculate the average score
        Map<Long, Double> averageScores = responses.stream()
                .filter(r -> r.getSkill() != null && r.getSkill().getCategory() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getSkill().getCategory().getId(),
                        Collectors.averagingInt(AssessmentResponse::getScore)
                ));
        // prepare the lists for the DTO
        List<String> labels = new ArrayList<>();
        List<Double> dataPoints = new ArrayList<>();

        for (Category category : categories) {
            labels.add(category.getName());

            Double avg = averageScores.getOrDefault(category.getId(), 0.0);
            dataPoints.add(avg);
        }

        ChartDataDTO.Dataset dataset = ChartDataDTO.Dataset.builder()
                .label("Average Team Competency")
                .data(dataPoints)
                .backgroundColor("rgba(54, 162, 235, 0.5)")
                .borderColor("rgba(54, 162, 235, 1)")
                .borderWidth(1)
                .build();

        long totalSubmissions = assessmentSubmissionRepository.count();

        Double overallAvg = 0.0;
        if (!responses.isEmpty()) {
            overallAvg= responses.stream()
                    .collect(Collectors.averagingInt(AssessmentResponse::getScore));
        }

        ChartDataDTO.SummaryStats summary = ChartDataDTO.SummaryStats.builder()
                .overallAverageScore(overallAvg)
                .totalSubmissions(totalSubmissions)
                .build();

        return ChartDataDTO.builder()
                .labels(labels)
                .datasets(List.of(dataset))
                .build();
    }
}