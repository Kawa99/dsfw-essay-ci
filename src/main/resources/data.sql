-- ============================================================================
-- SELECT DATABASE
-- ============================================================================
USE dsfw_team_proj;

-- ============================================================================
-- CATEGORIES
-- ============================================================================
INSERT IGNORE INTO categories (name, is_active) VALUES ('Service Delivery Requirements', true);
INSERT IGNORE INTO categories (name, is_active) VALUES ('Permissions and Organisation', true);
INSERT IGNORE INTO categories (name, is_active) VALUES ('Ways of Working', true);
INSERT IGNORE INTO categories (name, is_active) VALUES ('Environmental Focus', true);
INSERT IGNORE INTO categories (name, is_active) VALUES ('Team working considerations and adaptability', true);

-- ============================================================================
-- TAGS - Different skill areas for classification
-- ============================================================================
INSERT IGNORE INTO tags (name, is_active) VALUES ('Leadership', true);
INSERT IGNORE INTO tags (name, is_active) VALUES ('Communication', true);
INSERT IGNORE INTO tags (name, is_active) VALUES ('Team Management', true);
INSERT IGNORE INTO tags (name, is_active) VALUES ('Planning', true);
INSERT IGNORE INTO tags (name, is_active) VALUES ('Agile Practices', true);
INSERT IGNORE INTO tags (name, is_active) VALUES ('Collaboration', true);
INSERT IGNORE INTO tags (name, is_active) VALUES ('Problem Solving', true);
INSERT IGNORE INTO tags (name, is_active) VALUES ('Continuous Improvement', true);
INSERT IGNORE INTO tags (name, is_active) VALUES ('Resource Management', true);
INSERT IGNORE INTO tags (name, is_active) VALUES ('Strategic Thinking', true);

-- ============================================================================
-- SKILLS - Mix of all question types (DROPDOWN, RATING_SCALE, YES_NO, MULTIPLE_CHOICE)
-- ============================================================================

-- Category 1: Service Delivery Requirements
INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I can accurately assess and list the specific capabilities required to deliver this service.',
        true,
        1,
        'DROPDOWN',
        'Strongly Disagree
Disagree
Neutral
Agree
Strongly Agree');

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I am confident in my ability to assemble a core team that possesses the necessary skills for the project.',
        true,
        1,
        'RATING_SCALE',
        NULL);

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I have a clear plan for engaging with external specialists to bring in specific knowledge when the core team lacks it.',
        true,
        1,
        'YES_NO',
        NULL);

-- Category 2: Permissions and Organisation
INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I can clearly define and communicate the roles and responsibilities for every member of the team.',
        true,
        2,
        'MULTIPLE_CHOICE',
        'I struggle with this and need significant support
I can do this with guidance
I am competent and can do this independently
I am highly skilled and can mentor others');

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I am able to align the entire team around a unified purpose and shared goals.',
        true,
        2,
        'DROPDOWN',
        'Strongly Disagree
Disagree
Neutral
Agree
Strongly Agree');

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I understand exactly how each team member''s specific role contributes to achieving our overall objectives.',
        true,
        2,
        'RATING_SCALE',
        NULL);

-- Category 3: Ways of Working
INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I can facilitate the team in defining and agreeing upon shared working arrangements and expectations.',
        true,
        3,
        'YES_NO',
        NULL);

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I am confident in establishing routines (e.g., stand-ups, retro) that keep the team aligned and on track.',
        true,
        3,
        'DROPDOWN',
        'Strongly Disagree
Disagree
Neutral
Agree
Strongly Agree');

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I can clearly define how the team should collaborate effectively to minimize friction.',
        true,
        3,
        'MULTIPLE_CHOICE',
        'Not at all confident
Slightly confident
Moderately confident
Very confident
Extremely confident');

-- Category 4: Environmental Focus
INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I actively encourage and integrate diverse perspectives and ideas within the team.',
        true,
        4,
        'RATING_SCALE',
        NULL);

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I am able to foster a psychological safety net where team members feel safe to speak up and contribute.',
        true,
        4,
        'DROPDOWN',
        'Strongly Disagree
Disagree
Neutral
Agree
Strongly Agree');

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I ensure that team members have the right support and resources to develop their skills while they work.',
        true,
        4,
        'YES_NO',
        NULL);

-- Category 5: Team working considerations and adaptability
INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I regularly monitor the team''s processes to identify areas where our "ways of working" need improvement.',
        true,
        5,
        'MULTIPLE_CHOICE',
        'Never
Rarely
Sometimes
Often
Always');

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I can effectively lead the team in reflecting on our collective performance (e.g., during retrospectives).',
        true,
        5,
        'DROPDOWN',
        'Strongly Disagree
Disagree
Neutral
Agree
Strongly Agree');

INSERT IGNORE INTO skills (name, is_active, category_id, question_type, options)
VALUES ('I am confident in suggesting and implementing specific actions to improve team efficiency based on feedback.',
        true,
        5,
        'RATING_SCALE',
        NULL);

-- ============================================================================
-- SKILL TAGS - Link skills to relevant tags (many-to-many)
-- ============================================================================

-- Skill 1: Service Delivery - assess capabilities
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (1, 4);  -- Planning
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (1, 10); -- Strategic Thinking

-- Skill 2: Service Delivery - assemble team
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (2, 3);  -- Team Management
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (2, 9);  -- Resource Management

-- Skill 3: Service Delivery - external specialists
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (3, 4);  -- Planning
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (3, 9);  -- Resource Management

-- Skill 4: Permissions - define roles
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (4, 1);  -- Leadership
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (4, 2);  -- Communication
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (4, 3);  -- Team Management

-- Skill 5: Permissions - align team
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (5, 1);  -- Leadership
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (5, 2);  -- Communication

-- Skill 6: Permissions - understand roles
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (6, 3);  -- Team Management
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (6, 10); -- Strategic Thinking

-- Skill 7: Ways of Working - shared arrangements
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (7, 6);  -- Collaboration
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (7, 2);  -- Communication

-- Skill 8: Ways of Working - routines
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (8, 5);  -- Agile Practices
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (8, 3);  -- Team Management

-- Skill 9: Ways of Working - minimize friction
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (9, 6);  -- Collaboration
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (9, 7);  -- Problem Solving

-- Skill 10: Environmental - diverse perspectives
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (10, 1); -- Leadership
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (10, 6); -- Collaboration

-- Skill 11: Environmental - psychological safety
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (11, 1); -- Leadership
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (11, 3); -- Team Management

-- Skill 12: Environmental - support and resources
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (12, 9); -- Resource Management
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (12, 1); -- Leadership

-- Skill 13: Adaptability - monitor processes
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (13, 8); -- Continuous Improvement
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (13, 7); -- Problem Solving

-- Skill 14: Adaptability - retrospectives
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (14, 5); -- Agile Practices
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (14, 8); -- Continuous Improvement

-- Skill 15: Adaptability - implement improvements
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (15, 8); -- Continuous Improvement
INSERT IGNORE INTO skill_tags (skill_id, tag_id) VALUES (15, 1); -- Leadership

-- ============================================================================
-- SKILL RECOMMENDATIONS - Based on response scores/conditions
-- ============================================================================

-- Skill 1: Service Delivery - assess capabilities (DROPDOWN)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (1, 'option_1', 'https://www.gov.uk/service-manual/service-assessments/get-ready-for-an-assessment');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (1, 'option_2', 'https://www.gov.uk/service-manual/design/scoping-your-service');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (1, 'option_3', 'https://www.gov.uk/service-manual/design/scoping-your-service');

-- Skill 2: Service Delivery - assemble team (RATING_SCALE)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (2, 'rating_1', 'https://www.gov.uk/service-manual/the-team/set-up-a-service-team');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (2, 'rating_2', 'https://www.gov.uk/service-manual/the-team/set-up-a-service-team');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (2, 'rating_3', 'https://www.gov.uk/service-manual/the-team/recruitment/seniority-levels');

-- Skill 3: Service Delivery - external specialists (YES_NO)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (3, 'no', 'https://www.gov.uk/service-manual/the-team/working-with-specialists');

-- Skill 4: Permissions - define roles (MULTIPLE_CHOICE)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (4, 'option_1', 'https://www.gov.uk/service-manual/the-team/set-up-a-service-team');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (4, 'option_2', 'https://www.gov.uk/service-manual/the-team/set-up-a-service-team');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (4, 'option_3', 'https://www.gov.uk/service-manual/agile-delivery/core-principles-agile');

-- Skill 5: Permissions - align team (DROPDOWN)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (5, 'option_1', 'https://www.gov.uk/service-manual/agile-delivery/core-principles-agile');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (5, 'option_2', 'https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (5, 'option_3', 'https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');

-- Skill 6: Permissions - understand roles (RATING_SCALE)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (6, 'rating_1', 'https://www.gov.uk/service-manual/the-team');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (6, 'rating_2', 'https://www.gov.uk/service-manual/the-team/set-up-a-service-team');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (6, 'rating_3', 'https://www.gov.uk/service-manual/the-team/set-up-a-service-team');

-- Skill 7: Ways of Working - shared arrangements (YES_NO)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (7, 'no', 'https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');

-- Skill 8: Ways of Working - routines (DROPDOWN)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (8, 'option_1', 'https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (8, 'option_2', 'https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (8, 'option_3', 'https://www.gov.uk/service-manual/agile-delivery/running-retrospectives');

-- Skill 9: Ways of Working - minimize friction (MULTIPLE_CHOICE)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (9, 'option_1', 'https://www.gov.uk/service-manual/agile-delivery/core-principles-agile');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (9, 'option_2', 'https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (9, 'option_3', 'https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');

-- Skill 10: Environmental - diverse perspectives (RATING_SCALE)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (10, 'rating_1', 'https://www.gov.uk/service-manual/the-team/working-across-organisational-boundaries');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (10, 'rating_2', 'https://www.gov.uk/service-manual/the-team/working-across-organisational-boundaries');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (10, 'rating_3', 'https://www.gov.uk/service-manual/the-team/working-across-organisational-boundaries');

-- Skill 11: Environmental - psychological safety (DROPDOWN)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (11, 'option_1', 'https://www.gov.uk/service-manual/the-team/managing-a-team');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (11, 'option_2', 'https://www.gov.uk/service-manual/the-team/managing-a-team');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (11, 'option_3', 'https://www.gov.uk/service-manual/the-team/managing-a-team');

-- Skill 12: Environmental - support and resources (YES_NO)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (12, 'no', 'https://www.gov.uk/service-manual/the-team/how-the-digital-data-and-technology-profession-deals-with-career-development');

-- Skill 13: Adaptability - monitor processes (MULTIPLE_CHOICE)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (13, 'option_1', 'https://www.gov.uk/service-manual/agile-delivery/running-retrospectives');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (13, 'option_2', 'https://www.gov.uk/service-manual/agile-delivery/running-retrospectives');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (13, 'option_3', 'https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');

-- Skill 14: Adaptability - retrospectives (DROPDOWN)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (14, 'option_1', 'https://www.gov.uk/service-manual/agile-delivery/running-retrospectives');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (14, 'option_2', 'https://www.gov.uk/service-manual/agile-delivery/running-retrospectives');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (14, 'option_3', 'https://www.gov.uk/service-manual/agile-delivery/running-retrospectives');

-- Skill 15: Adaptability - implement improvements (RATING_SCALE)
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (15, 'rating_1', 'https://www.gov.uk/service-manual/agile-delivery/running-retrospectives');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (15, 'rating_2', 'https://www.gov.uk/service-manual/agile-delivery/running-retrospectives');
INSERT IGNORE INTO skill_recommendations (skill_id, condition_key, recommended_url)
VALUES (15, 'rating_3', 'https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');

-- ============================================================================
-- ADMIN USER
-- ============================================================================
INSERT INTO user_details (first_name, last_name, email, password, role)
VALUES (
           'Admin',
           'User',
           'admin@test.com',
           '$2b$12$OEVQ6zyfoZRYP7XvTB6Qyu7Z7ojRtitSEYq45QQzv4Mfcz98Vibj6',
           'ADMIN'
       )
ON DUPLICATE KEY UPDATE
                     first_name = VALUES(first_name),
                     last_name = VALUES(last_name),
                     password = VALUES(password),
                     role = VALUES(role);