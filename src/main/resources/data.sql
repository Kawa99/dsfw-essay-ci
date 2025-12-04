INSERT IGNORE INTO categories (name, is_active) VALUES ('Service Delivery Requirements', true);
INSERT IGNORE INTO categories (name, is_active) VALUES ('Permissions and Organisation', true);
INSERT IGNORE INTO categories (name, is_active) VALUES ('Ways of Working', true);
INSERT IGNORE INTO categories (name, is_active) VALUES ('Environmental Focus', true);
INSERT IGNORE INTO categories (name, is_active) VALUES ('Team working considerations and adaptability', true);


INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I can accurately assess and list the specific capabilities required to deliver this service.', true, 1);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I am confident in my ability to assemble a core team that possesses the necessary skills for the project.', true, 1);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I have a clear plan for engaging with external specialists to bring in specific knowledge when the core team lacks it.', true, 1);


INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I can clearly define and communicate the roles and responsibilities for every member of the team.', true, 2);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I am able to align the entire team around a unified purpose and shared goals.', true, 2);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I understand exactly how each team member''s specific role contributes to achieving our overall objectives.', true, 2);

INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I can facilitate the team in defining and agreeing upon shared working arrangements and expectations.', true, 3);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I am confident in establishing routines (e.g., stand-ups, retro) that keep the team aligned and on track.', true, 3);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I can clearly define how the team should collaborate effectively to minimize friction.', true, 3);

INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I actively encourage and integrate diverse perspectives and ideas within the team.', true, 4);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I am able to foster a psychological safety net where team members feel safe to speak up and contribute.', true, 4);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I ensure that team members have the right support and resources to develop their skills while they work.', true, 4);

INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I regularly monitor the team''s processes to identify areas where our "ways of working" need improvement.', true, 5);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I can effectively lead the team in reflecting on our collective performance (e.g., during retrospectives).', true, 5);
INSERT IGNORE INTO skills (name, is_active, category_id) VALUES ('I am confident in suggesting and implementing specific actions to improve team efficiency based on feedback.', true, 5);

DELETE FROM user_details WHERE email='admin@test.com';
INSERT INTO user_details (first_name, last_name, email, password, role)
VALUES (
           'Admin',
           'User',
           'admin@test.com',
           '$2b$12$OEVQ6zyfoZRYP7XvTB6Qyu7Z7ojRtitSEYq45QQzv4Mfcz98Vibj6',
           'ADMIN'
       );