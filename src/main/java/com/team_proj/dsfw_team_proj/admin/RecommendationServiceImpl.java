package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private final SkillRepository skillRepository;
    private final SkillRecommendationRepository recommendationRepository;

    public RecommendationServiceImpl(SkillRepository skillRepository,
                                     SkillRecommendationRepository recommendationRepository) {
        this.skillRepository = skillRepository;
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public void saveRecommendations(Long skillId, Map<String, List<String>> map) {
        SkillsEntity skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found: " + skillId));

        // Remove old recommendations and flush to ensure deletion completes
        recommendationRepository.deleteBySkillId(skillId);
        recommendationRepository.flush();

        // Save new ones
        map.forEach((condition, urls) -> {
            for (String url : urls) {
                if (url == null || url.trim().isEmpty()) continue;

                SkillRecommendation rec = new SkillRecommendation();
                rec.setSkill(skill);
                rec.setConditionKey(condition);
                rec.setRecommendedUrl(url.trim());
                recommendationRepository.save(rec);
            }
        });
    }

    @Override
    public Map<String, String> getRecommendations(Long skillId) {
        Map<String, String> result = new LinkedHashMap<>();
        recommendationRepository.findBySkillId(skillId)
                .forEach(r -> result.put(r.getConditionKey(), r.getRecommendedUrl()));
        return result;
    }

    @Override
    public Map<String, List<String>> getRecommendationsGrouped(Long skillId) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        List<SkillRecommendation> recommendations = recommendationRepository.findBySkillId(skillId);

        for (SkillRecommendation rec : recommendations) {
            result.computeIfAbsent(rec.getConditionKey(), k -> new ArrayList<>())
                    .add(rec.getRecommendedUrl());
        }

        return result;
    }
}