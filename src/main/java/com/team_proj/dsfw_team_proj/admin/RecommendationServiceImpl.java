package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
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
    public void saveRecommendations(Long skillId, Map<String, String> map) {
        SkillsEntity skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found: " + skillId));

        // Remove old recommendations
        recommendationRepository.deleteBySkillId(skillId);

        // Save new ones
        map.forEach((condition, url) -> {
            if (url == null || url.trim().isEmpty()) return;

            SkillRecommendation rec = new SkillRecommendation();
            rec.setSkill(skill);
            rec.setConditionKey(condition);
            rec.setRecommendedUrl(url.trim());
            recommendationRepository.save(rec);
        });
    }

    @Override
    public Map<String, String> getRecommendations(Long skillId) {
        Map<String, String> result = new LinkedHashMap<>();

        recommendationRepository.findBySkillId(skillId)
                .forEach(r -> result.put(r.getConditionKey(), r.getRecommendedUrl()));

        return result;
    }
}
