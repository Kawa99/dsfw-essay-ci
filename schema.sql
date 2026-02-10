/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.15-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: dsfw_team_proj
-- ------------------------------------------------------
-- Server version	10.11.15-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assessment_responses`
--

DROP TABLE IF EXISTS `assessment_responses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessment_responses` (
  `score` int(11) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `skill_id` bigint(20) DEFAULT NULL,
  `submission_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtqhoofoo7qph36b5c115qocjl` (`skill_id`),
  KEY `FK2yvb0r0nfnlw9giarcmvghm1c` (`submission_id`),
  CONSTRAINT `FK2yvb0r0nfnlw9giarcmvghm1c` FOREIGN KEY (`submission_id`) REFERENCES `assessment_submissions` (`id`),
  CONSTRAINT `FKtqhoofoo7qph36b5c115qocjl` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_responses`
--

LOCK TABLES `assessment_responses` WRITE;
/*!40000 ALTER TABLE `assessment_responses` DISABLE KEYS */;
/*!40000 ALTER TABLE `assessment_responses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assessment_submissions`
--

DROP TABLE IF EXISTS `assessment_submissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `assessment_submissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `submitted_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmbh6663x8xd3tx9is0j122oho` (`user_id`),
  CONSTRAINT `FKmbh6663x8xd3tx9is0j122oho` FOREIGN KEY (`user_id`) REFERENCES `user_details` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assessment_submissions`
--

LOCK TABLES `assessment_submissions` WRITE;
/*!40000 ALTER TABLE `assessment_submissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `assessment_submissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `is_active` bit(1) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES
('',1,'Service Delivery Requirements'),
('',2,'Permissions and Organisation'),
('',3,'Ways of Working'),
('',4,'Environmental Focus'),
('',5,'Team working considerations and adaptability');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `manager` (
  `results` int(11) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager_entity`
--

DROP TABLE IF EXISTS `manager_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `manager_entity` (
  `results` int(11) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager_entity`
--

LOCK TABLES `manager_entity` WRITE;
/*!40000 ALTER TABLE `manager_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `manager_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `is_read` bit(1) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `timestamp` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `message` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfpmbgp72w3a5lqwnfix6ijg6y` (`user_id`),
  CONSTRAINT `FKfpmbgp72w3a5lqwnfix6ijg6y` FOREIGN KEY (`user_id`) REFERENCES `user_details` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill_recommendations`
--

DROP TABLE IF EXISTS `skill_recommendations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `skill_recommendations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `skill_id` bigint(20) NOT NULL,
  `condition_key` varchar(50) NOT NULL,
  `recommended_url` varchar(500) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKtr7ua1kqwwnkqnk49gqb94b5e` (`skill_id`,`condition_key`),
  CONSTRAINT `FKjdl4onnoq5vsdpmlfuq4xdl6y` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_recommendations`
--

LOCK TABLES `skill_recommendations` WRITE;
/*!40000 ALTER TABLE `skill_recommendations` DISABLE KEYS */;
INSERT INTO `skill_recommendations` VALUES
(1,1,'option_1','https://www.gov.uk/service-manual/service-assessments/get-ready-for-an-assessment'),
(2,1,'option_2','https://www.gov.uk/service-manual/design/scoping-your-service'),
(3,1,'option_3','https://www.gov.uk/service-manual/design/scoping-your-service'),
(4,2,'rating_1','https://www.gov.uk/service-manual/the-team/set-up-a-service-team'),
(5,2,'rating_2','https://www.gov.uk/service-manual/the-team/set-up-a-service-team'),
(6,2,'rating_3','https://www.gov.uk/service-manual/the-team/recruitment/seniority-levels'),
(7,3,'no','https://www.gov.uk/service-manual/the-team/working-with-specialists'),
(8,4,'option_1','https://www.gov.uk/service-manual/the-team/set-up-a-service-team'),
(9,4,'option_2','https://www.gov.uk/service-manual/the-team/set-up-a-service-team'),
(10,4,'option_3','https://www.gov.uk/service-manual/agile-delivery/core-principles-agile'),
(11,5,'option_1','https://www.gov.uk/service-manual/agile-delivery/core-principles-agile'),
(12,5,'option_2','https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques'),
(13,5,'option_3','https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques'),
(14,6,'rating_1','https://www.gov.uk/service-manual/the-team'),
(15,6,'rating_2','https://www.gov.uk/service-manual/the-team/set-up-a-service-team'),
(16,6,'rating_3','https://www.gov.uk/service-manual/the-team/set-up-a-service-team'),
(17,7,'no','https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques'),
(18,8,'option_1','https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques'),
(19,8,'option_2','https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques'),
(20,8,'option_3','https://www.gov.uk/service-manual/agile-delivery/running-retrospectives'),
(21,9,'option_1','https://www.gov.uk/service-manual/agile-delivery/core-principles-agile'),
(22,9,'option_2','https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques'),
(23,9,'option_3','https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques'),
(24,10,'rating_1','https://www.gov.uk/service-manual/the-team/working-across-organisational-boundaries'),
(25,10,'rating_2','https://www.gov.uk/service-manual/the-team/working-across-organisational-boundaries'),
(26,10,'rating_3','https://www.gov.uk/service-manual/the-team/working-across-organisational-boundaries'),
(27,11,'option_1','https://www.gov.uk/service-manual/the-team/managing-a-team'),
(28,11,'option_2','https://www.gov.uk/service-manual/the-team/managing-a-team'),
(29,11,'option_3','https://www.gov.uk/service-manual/the-team/managing-a-team'),
(30,12,'no','https://www.gov.uk/service-manual/the-team/how-the-digital-data-and-technology-profession-deals-with-career-development'),
(31,13,'option_1','https://www.gov.uk/service-manual/agile-delivery/running-retrospectives'),
(32,13,'option_2','https://www.gov.uk/service-manual/agile-delivery/running-retrospectives'),
(33,13,'option_3','https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques'),
(34,14,'option_1','https://www.gov.uk/service-manual/agile-delivery/running-retrospectives'),
(35,14,'option_2','https://www.gov.uk/service-manual/agile-delivery/running-retrospectives'),
(36,14,'option_3','https://www.gov.uk/service-manual/agile-delivery/running-retrospectives'),
(37,15,'rating_1','https://www.gov.uk/service-manual/agile-delivery/running-retrospectives'),
(38,15,'rating_2','https://www.gov.uk/service-manual/agile-delivery/running-retrospectives'),
(39,15,'rating_3','https://www.gov.uk/service-manual/agile-delivery/agile-tools-techniques');
/*!40000 ALTER TABLE `skill_recommendations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skill_tags`
--

DROP TABLE IF EXISTS `skill_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `skill_tags` (
  `skill_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`skill_id`,`tag_id`),
  KEY `FKa6k6rrvgpihtjh3paq52sgshl` (`tag_id`),
  CONSTRAINT `FK4h0kmt374yf74n30x7qn67p38` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`),
  CONSTRAINT `FKa6k6rrvgpihtjh3paq52sgshl` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_tags`
--

LOCK TABLES `skill_tags` WRITE;
/*!40000 ALTER TABLE `skill_tags` DISABLE KEYS */;
INSERT INTO `skill_tags` VALUES
(1,4),
(1,10),
(2,3),
(2,9),
(3,4),
(3,9),
(4,1),
(4,2),
(4,3),
(5,1),
(5,2),
(6,3),
(6,10),
(7,2),
(7,6),
(8,3),
(8,5),
(9,6),
(9,7),
(10,1),
(10,6),
(11,1),
(11,3),
(12,1),
(12,9),
(13,7),
(13,8),
(14,5),
(14,8),
(15,1),
(15,8);
/*!40000 ALTER TABLE `skill_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skills`
--

DROP TABLE IF EXISTS `skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `skills` (
  `is_active` bit(1) NOT NULL,
  `category_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `options` text DEFAULT NULL,
  `question_type` enum('DROPDOWN','MULTIPLE_CHOICE','RATING_SCALE','YES_NO') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK85woe63nu9klkk9fa73vf0jd0` (`name`),
  KEY `FKlyobx4bwdv7k6im48ru4pod1u` (`category_id`),
  CONSTRAINT `FKlyobx4bwdv7k6im48ru4pod1u` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skills`
--

LOCK TABLES `skills` WRITE;
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
INSERT INTO `skills` VALUES
('',1,1,'I can accurately assess and list the specific capabilities required to deliver this service.','Strongly Disagree\nDisagree\nNeutral\nAgree\nStrongly Agree','DROPDOWN'),
('',1,2,'I am confident in my ability to assemble a core team that possesses the necessary skills for the project.',NULL,'RATING_SCALE'),
('',1,3,'I have a clear plan for engaging with external specialists to bring in specific knowledge when the core team lacks it.',NULL,'YES_NO'),
('',2,4,'I can clearly define and communicate the roles and responsibilities for every member of the team.','I struggle with this and need significant support\nI can do this with guidance\nI am competent and can do this independently\nI am highly skilled and can mentor others','MULTIPLE_CHOICE'),
('',2,5,'I am able to align the entire team around a unified purpose and shared goals.','Strongly Disagree\nDisagree\nNeutral\nAgree\nStrongly Agree','DROPDOWN'),
('',2,6,'I understand exactly how each team member\'s specific role contributes to achieving our overall objectives.',NULL,'RATING_SCALE'),
('',3,7,'I can facilitate the team in defining and agreeing upon shared working arrangements and expectations.',NULL,'YES_NO'),
('',3,8,'I am confident in establishing routines (e.g., stand-ups, retro) that keep the team aligned and on track.','Strongly Disagree\nDisagree\nNeutral\nAgree\nStrongly Agree','DROPDOWN'),
('',3,9,'I can clearly define how the team should collaborate effectively to minimize friction.','Not at all confident\nSlightly confident\nModerately confident\nVery confident\nExtremely confident','MULTIPLE_CHOICE'),
('',4,10,'I actively encourage and integrate diverse perspectives and ideas within the team.',NULL,'RATING_SCALE'),
('',4,11,'I am able to foster a psychological safety net where team members feel safe to speak up and contribute.','Strongly Disagree\nDisagree\nNeutral\nAgree\nStrongly Agree','DROPDOWN'),
('',4,12,'I ensure that team members have the right support and resources to develop their skills while they work.',NULL,'YES_NO'),
('',5,13,'I regularly monitor the team\'s processes to identify areas where our \"ways of working\" need improvement.','Never\nRarely\nSometimes\nOften\nAlways','MULTIPLE_CHOICE'),
('',5,14,'I can effectively lead the team in reflecting on our collective performance (e.g., during retrospectives).','Strongly Disagree\nDisagree\nNeutral\nAgree\nStrongly Agree','DROPDOWN'),
('',5,15,'I am confident in suggesting and implementing specific actions to improve team efficiency based on feedback.',NULL,'RATING_SCALE');
/*!40000 ALTER TABLE `skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `skills_category`
--

DROP TABLE IF EXISTS `skills_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `skills_category` (
  `category_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `skill_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe12d8ocpb9pt13vod4cf5v4u6` (`category_id`),
  KEY `FK4lnxty5xv8mxbr2gehfq42107` (`skill_id`),
  CONSTRAINT `FK4lnxty5xv8mxbr2gehfq42107` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`),
  CONSTRAINT `FKe12d8ocpb9pt13vod4cf5v4u6` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skills_category`
--

LOCK TABLES `skills_category` WRITE;
/*!40000 ALTER TABLE `skills_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `skills_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt48xdq560gs3gap9g7jg36kgc` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES
(1,1,'Leadership'),
(1,2,'Communication'),
(1,3,'Team Management'),
(1,4,'Planning'),
(1,5,'Agile Practices'),
(1,6,'Collaboration'),
(1,7,'Problem Solving'),
(1,8,'Continuous Improvement'),
(1,9,'Resource Management'),
(1,10,'Strategic Thinking');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team_memberships`
--

DROP TABLE IF EXISTS `team_memberships`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `team_memberships` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `team_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `role` enum('MANAGER','MEMBER') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdqwto3i1w1kxarnesb33ydsms` (`team_id`),
  KEY `FKgclyc41esn4162kw9sasxo95n` (`user_id`),
  CONSTRAINT `FKdqwto3i1w1kxarnesb33ydsms` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`),
  CONSTRAINT `FKgclyc41esn4162kw9sasxo95n` FOREIGN KEY (`user_id`) REFERENCES `user_details` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team_memberships`
--

LOCK TABLES `team_memberships` WRITE;
/*!40000 ALTER TABLE `team_memberships` DISABLE KEYS */;
/*!40000 ALTER TABLE `team_memberships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teams`
--

DROP TABLE IF EXISTS `teams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `teams` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(500) DEFAULT NULL,
  `join_code` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `team_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9quco9dilvism8lxfv2s9fv0` (`join_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teams`
--

LOCK TABLES `teams` WRITE;
/*!40000 ALTER TABLE `teams` DISABLE KEYS */;
/*!40000 ALTER TABLE `teams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_details`
--

DROP TABLE IF EXISTS `user_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4d9rdl7d52k8x3etihxlaujvh` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_details`
--

LOCK TABLES `user_details` WRITE;
/*!40000 ALTER TABLE `user_details` DISABLE KEYS */;
INSERT INTO `user_details` VALUES
(1,'admin@test.com','Admin','User','$2b$12$OEVQ6zyfoZRYP7XvTB6Qyu7Z7ojRtitSEYq45QQzv4Mfcz98Vibj6','ADMIN');
/*!40000 ALTER TABLE `user_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-10 17:54:40
