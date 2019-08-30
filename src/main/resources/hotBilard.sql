-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: hot_bilard
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `calibration_params`
--

DROP TABLE IF EXISTS `calibration_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `calibration_params` (
  `id` bigint(20) NOT NULL,
  `ball_diameter` int(11) NOT NULL,
  `left_bottom_corner` geometry DEFAULT NULL,
  `left_bottom_corner_projector` geometry DEFAULT NULL,
  `left_upper_corner` geometry DEFAULT NULL,
  `left_upper_corner_projector` geometry DEFAULT NULL,
  `preset_name` varchar(255) DEFAULT NULL,
  `right_bottom_corner` geometry DEFAULT NULL,
  `right_bottom_corner_projector` geometry DEFAULT NULL,
  `right_upper_corner` geometry DEFAULT NULL,
  `right_upper_corner_projector` geometry DEFAULT NULL,
  `table_size_in_cm` geometry DEFAULT NULL,
  `white_ball_density` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calibration_params`
--

LOCK TABLES `calibration_params` WRITE;
/*!40000 ALTER TABLE `calibration_params` DISABLE KEYS */;
INSERT INTO `calibration_params` VALUES (1,26,_binary '\0\0\0\0\0\0\0\0\0\0\0\0@{@\0\0\0\0\0Äã@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0@Z@\0\0\0\0\0Äç@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0†{@\0\0\0\0\0@r@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0ÄQ@\0\0\0\0\0\0G@','Default',_binary '\0\0\0\0\0\0\0\0\0\0\0\0ô@\0\0\0\0\0»ã@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0Ñú@\0\0\0\0\0†ç@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0,ô@\0\0\0\0\0@s@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0ù@\0\0\0\0\0\0I@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0¿o@\0\0\0\0\0¿_@',320000);
/*!40000 ALTER TABLE `calibration_params` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (2);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hit_point_hint`
--

DROP TABLE IF EXISTS `hit_point_hint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hit_point_hint` (
  `hit_point` int(11) DEFAULT NULL,
  `position` geometry DEFAULT NULL,
  `radius` double NOT NULL,
  `individual_training_id` bigint(20) NOT NULL,
  `inside_circles_offsets` geometry DEFAULT NULL,
  PRIMARY KEY (`individual_training_id`),
  CONSTRAINT `FKh82075ihbv49jc6lq2g71iesh` FOREIGN KEY (`individual_training_id`) REFERENCES `individual_training` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hit_point_hint`
--

LOCK TABLES `hit_point_hint` WRITE;
/*!40000 ALTER TABLE `hit_point_hint` DISABLE KEYS */;
INSERT INTO `hit_point_hint` VALUES (5,_binary '\0\0\0\0\0\0\0Åpè\ÿ?:ùNßS¨\„?',0.07874015748031496,1,NULL),(0,_binary '\0\0\0\0\0\0\0´\’jµ:\≈\“?sπ\\.\◊(\Á?',0.07874015748031496,2,NULL);
/*!40000 ALTER TABLE `hit_point_hint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hit_power_hint`
--

DROP TABLE IF EXISTS `hit_power_hint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hit_power_hint` (
  `hit_power` double NOT NULL,
  `position` geometry DEFAULT NULL,
  `size` geometry DEFAULT NULL,
  `individual_training_id` bigint(20) NOT NULL,
  PRIMARY KEY (`individual_training_id`),
  CONSTRAINT `FKpm1tfw62hh2lfpd2s9o59olyj` FOREIGN KEY (`individual_training_id`) REFERENCES `individual_training` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hit_power_hint`
--

LOCK TABLES `hit_power_hint` WRITE;
/*!40000 ALTER TABLE `hit_power_hint` DISABLE KEYS */;
INSERT INTO `hit_power_hint` VALUES (60,_binary '\0\0\0\0\0\0\0kµZ≠ñÖ\–?\ÿ\ÎızΩ\\\‡?',_binary '\0\0\0\0\0\0\0\nÖB°P(î?M&ì\…d2\Ÿ?',1);
/*!40000 ALTER TABLE `hit_power_hint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `individual_training`
--

DROP TABLE IF EXISTS `individual_training`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `individual_training` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `difficulty_level` int(11) DEFAULT NULL,
  `disturb_balls_positions` geometry DEFAULT NULL,
  `pocket_id` int(11) NOT NULL,
  `rectangle_position` geometry DEFAULT NULL,
  `selected_ball_position` geometry DEFAULT NULL,
  `status_position` geometry DEFAULT NULL,
  `white_ball_position` geometry DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `individual_training`
--

LOCK TABLES `individual_training` WRITE;
/*!40000 ALTER TABLE `individual_training` DISABLE KEYS */;
INSERT INTO `individual_training` VALUES (1,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0a0ˆ	∂?ø\ﬂ\Ô˜Ù\Î?',5,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0á√°lä?I$â4<\‹?\0\0\0{Ω^ØG∏\«?Ø\◊\Îı\nı\Í?',_binary '\0\0\0\0\0\0\0\"ëH$\“-¢?\∆b±X¨S\Ï?',_binary '\0\0\0\0\0\0\0wª\›n˜¡ö?\ZçF£±rø?',_binary '\0\0\0\0\0\0\0M&ì\…4À¥?i4\Zçfô\Ê?'),(2,0,NULL,4,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0±X,K\Ì\⁄?,ã\≈Ú}\⁄?\0\0\0&ì\…dbÅ\‚?´\’jµ\“Ù\Ó?',_binary '\0\0\0\0\0\0\0sπ\\.\«8\ﬂ?ôL&ìiñ\È?',_binary '\0\0\0\0\0\0\0\Êrπ\\\Ó ë?\“h4\Z-˜∫?',_binary '\0\0\0\0\0\0\0y<èwà\ﬂ?.ó\À\Â\"\›\‚?'),(3,0,NULL,0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0ø\ﬂ\Ô˜€™ò?kµZ≠\∆q\‚?\0\0\0Y,ã˜\œ\”?ß\”\Èt*ù\Ó?',_binary '\0\0\0\0\0\0\0,ã\≈2Õ≤?çF£\—(\◊\Ë?',_binary '\0\0\0\0\0\0\0W´\’jı√ò?\Ít:ùÆuº?',_binary '\0\0\0\0\0\0\0i4\Zçfô\∆?É¡`0\ÿ\'\ÿ?'),(4,2,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\–\ÁÛ˘¸\È?\Ã\Ârπ<\√\‹?',2,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0õ\Õf≥ëä\Î?(\nÖ‚§Æ?\0\0\0õ\Õf≥!\¬\Ô?}>ü\œCK\‹?',_binary '\0\0\0\0\0\0\0õ\Õf≥I∂\Ì?´\’jµZ•\ ?',_binary '\0\0\0\0\0\0\0M&ì\…X¶?\"ëH$2Úø?',_binary '\0\0\0\0\0\0\0ÉÅ~\Â?U*ïJ•Z\Â?'),(5,2,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0c±X,\∆9\∆?É¡0\œ\–?\0\0\0´\’jµJµ\ ?C°P(\‘+\‘?\0\0\0°P(\Í\ ?á\√\·pòg\»?\0\0\0á\√Aæ¡?ß\”\Ètöe\ ?\0\0\0.ó\À\Â\"\›\¬?M&ì\…4\À\‘?',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0ì\…d2ô\ÌÖ?¸˝~øünà?\0\0\0\ﬁn∑\€-aæ?ã\≈bµ\Ÿ\Õ?',_binary '\0\0\0\0\0\0\0\ZçF£QÆ°?_Ø\◊\Î\Íµ?',_binary '\0\0\0\0\0\0\0©T*ï˙\–\‹?\nÖB\·0\»?',_binary '\0\0\0\0\0\0\0\"ëH$\‚\⁄?.ó\À\Â\"\›\‚?'),(6,2,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\‘\Èt:ΩB\’?\‹\Ìvª=\¬\›?\0\0\0\∆b±Xúc\‡?Ω^Ø\◊\‰\À?\0\0\0c±X,\÷)\‚?Å@¿?\‡?\0\0\0ˆzΩ^ØP\œ?á\√\·pòg\»?',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0W´\’jU1í?sπ\\.W0£?\0\0\0A \»?m6õ\ÕN@\◊?',_binary '\0\0\0\0\0\0\0ì\…d2\…6\—?\"ëH$\“-\“?',_binary '\0\0\0\0\0\0\0á\√[£?\ÏızΩ\Œu\Ï?',_binary '\0\0\0\0\0\0\0\Êrπ\\ûa\Í?\⁄l6õ]¢\›?'),(7,1,NULL,5,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0y<è◊Øì?G£\—h \‰?\0\0\0_Ø\◊\Î§\"\—?˜˚˝ö,\Ô?',_binary '\0\0\0\0\0\0\0o∑\€\Ì˘\∆?á\√\·pòg\Ë?',_binary '\0\0\0\0\0\0\0á\√\·PF\≈?˙|>üØtΩ?',_binary '\0\0\0\0\0\0\0ß\”\Ètäu\Ê? Ú\r\“?'),(8,1,NULL,0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0âD\"\Á?[≠V´U1¢?\0\0\0\¬`0¨7\Ó?ã\≈b±\›\Ÿ?',_binary '\0\0\0\0\0\0\0Úx<\ﬂ \Î?âD\"\—.¡?',_binary '\0\0\0\0\0\0\0\“h4\ZçœÆ?\‚p8.ˆª?',_binary '\0\0\0\0\0\0\0©T*ïzÖ∫?\‡\Ô˜˚˝\ﬁ?');
/*!40000 ALTER TABLE `individual_training` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `target_ball_hit_point_hint`
--

DROP TABLE IF EXISTS `target_ball_hit_point_hint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `target_ball_hit_point_hint` (
  `radius` double NOT NULL,
  `target_ball` geometry DEFAULT NULL,
  `white_ball` geometry DEFAULT NULL,
  `individual_training_id` bigint(20) NOT NULL,
  PRIMARY KEY (`individual_training_id`),
  CONSTRAINT `FKsaoga0ddd36ialyknfsqll3hk` FOREIGN KEY (`individual_training_id`) REFERENCES `individual_training` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `target_ball_hit_point_hint`
--

LOCK TABLES `target_ball_hit_point_hint` WRITE;
/*!40000 ALTER TABLE `target_ball_hit_point_hint` DISABLE KEYS */;
INSERT INTO `target_ball_hit_point_hint` VALUES (0.03937007874015748,_binary '\0\0\0\0\0\0\0¸˝~øø~\ÿ?ø\ﬂ\Ô˜˚\Ï?',_binary '\0\0\0\0\0\0\0á\√¡ˇ\ÿ?ø\ﬂ\Ô˜˚\Ï?',1);
/*!40000 ALTER TABLE `target_ball_hit_point_hint` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-30 12:46:35
