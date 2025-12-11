package com.team_proj.dsfw_team_proj.admin;

import com.team_proj.dsfw_team_proj.selfassessment.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getAllTags();

    Tag createTag(String name);

    void assignTagsToSkill(Long skillId, List<Long> tagIds);

    void clearTagsFromSkill(Long skillId);

    Tag updateTag(Long id, String newName);
    void deactivateTag(Long id);
}
