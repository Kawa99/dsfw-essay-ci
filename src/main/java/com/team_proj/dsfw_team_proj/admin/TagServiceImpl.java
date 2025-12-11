package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.SkillRepository;
import com.team_proj.dsfw_team_proj.selfassessment.SkillsEntity;
import com.team_proj.dsfw_team_proj.selfassessment.Tag;
import com.team_proj.dsfw_team_proj.selfassessment.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final SkillRepository skillRepository;

    public TagServiceImpl(TagRepository tagRepository, SkillRepository skillRepository) {
        this.tagRepository = tagRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findByIsActiveTrue();
    }

    @Override
    public Tag createTag(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }

        String trimmed = name.trim();

        if (trimmed.length() > 255) {
            throw new IllegalArgumentException("Tag name must be 50 characters or less.");
        }

        if (tagRepository.existsByName(trimmed)) {
            throw new IllegalArgumentException("Tag already exists");
        }

        Tag tag = new Tag();
        tag.setName(trimmed);
        return tagRepository.save(tag);
    }

    @Override
    public void assignTagsToSkill(Long skillId, List<Long> tagIds) {
        SkillsEntity skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found: " + skillId));

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));

        skill.setTags(tags);
        skillRepository.save(skill);
    }

    @Override
    public void clearTagsFromSkill(Long skillId) {
        SkillsEntity skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new EntityNotFoundException("Skill not found: " + skillId));

        skill.getTags().clear();
        skillRepository.save(skill);
    }

    @Override
    public Tag updateTag(Long id, String newName) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }

        String trimmed = newName.trim();

        if (!trimmed.equals(tag.getName()) && tagRepository.existsByName(trimmed)) {
            throw new IllegalArgumentException("Tag with this name already exists");
        }

        tag.setName(trimmed);
        return tagRepository.save(tag);
    }

    @Override
    public void deactivateTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        tag.setActive(false);
        tagRepository.save(tag);
    }
}
