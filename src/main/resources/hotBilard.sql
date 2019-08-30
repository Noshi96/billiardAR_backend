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
INSERT INTO `calibration_params` VALUES (1,26,_binary '\0\0\0\0\0\0\0\0\0\0\0\0@{@\0\0\0\0\0��@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0@Z@\0\0\0\0\0��@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�{@\0\0\0\0\0@r@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�Q@\0\0\0\0\0\0G@','Default',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�@\0\0\0\0\0ȋ@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0��@\0\0\0\0\0��@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0,�@\0\0\0\0\0@s@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�@\0\0\0\0\0\0I@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�o@\0\0\0\0\0�_@',320000);
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
INSERT INTO `hit_point_hint` VALUES (5,_binary '\0\0\0\0\0\0\0�p�\�?:�N�S�\�?',0.07874015748031496,1,NULL),(0,_binary '\0\0\0\0\0\0\0�\�j�:\�\�?s�\\.\�(\�?',0.07874015748031496,2,NULL);
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
INSERT INTO `hit_power_hint` VALUES (60,_binary '\0\0\0\0\0\0\0k�Z���\�?\�\��z�\\\�?',_binary '\0\0\0\0\0\0\0\n�B�P(�?M&�\�d2\�?',1);
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
INSERT INTO `individual_training` VALUES (1,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0a0�	�?�\�\���\�?',5,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�ál�?I$�4<\�?\0\0\0{�^�G�\�?�\�\��\n�\�?',_binary '\0\0\0\0\0\0\0\"�H$\�-�?\�b�X�S\�?',_binary '\0\0\0\0\0\0\0w�\�n���?\Z�F��r�?',_binary '\0\0\0\0\0\0\0M&�\�4˴?i4\Z�f�\�?'),(2,0,NULL,4,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�X,K\�\�?,�\��}\�?\0\0\0&�\�db�\�?�\�j�\��\�?',_binary '\0\0\0\0\0\0\0s�\\.\�8\�?�L&�i�\�?',_binary '\0\0\0\0\0\0\0\�r�\\\�ʑ?\�h4\Z-��?',_binary '\0\0\0\0\0\0\0y<�w�\�?.�\�\�\"\�\�?'),(3,0,NULL,0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�\�\��۪�?k�Z�\�q\�?\0\0\0Y,��\�\�?�\�\�t*�\�?',_binary '\0\0\0\0\0\0\0,�\�2Ͳ?�F�\�(\�\�?',_binary '\0\0\0\0\0\0\0W�\�j�Ø?\�t:��u�?',_binary '\0\0\0\0\0\0\0i4\Z�f�\�?��`0\�\'\�?'),(4,2,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\�\����\�?\�\�r�<\�\�?',2,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�\�f���\�?(\n�⤮?\0\0\0�\�f�!\�\�?}>�\�CK\�?',_binary '\0\0\0\0\0\0\0�\�f�I�\�?�\�j�Z�\�?',_binary '\0\0\0\0\0\0\0M&�\�X�?\"�H$2�?',_binary '\0\0\0\0\0\0\0��~\�?U*�J�Z\�?'),(5,2,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0c�X,\�9\�?��0\�\�?\0\0\0�\�j�J�\�?C�P(\�+\�?\0\0\0�P(\�\�?�\�\�p�g\�?\0\0\0�\�A��?�\�\�t�e\�?\0\0\0.�\�\�\"\�\�?M&�\�4\�\�?',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�\�d2�\�?��~��n�?\0\0\0\�n�\�-a�?�\�b�\�\�?',_binary '\0\0\0\0\0\0\0\Z�F�Q��?_�\�\�\�?',_binary '\0\0\0\0\0\0\0�T*��\�\�?\n�B\�0\�?',_binary '\0\0\0\0\0\0\0\"�H$\�\�?.�\�\�\"\�\�?'),(6,2,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\�\�t:�B\�?\�\�v�=\�\�?\0\0\0\�b�X�c\�?�^�\�\�\�?\0\0\0c�X,\�)\�?�@�?\�?\0\0\0�z�^�P\�?�\�\�p�g\�?',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0W�\�jU1�?s�\\.W0�?\0\0\0A \�?m6�\�N@\�?',_binary '\0\0\0\0\0\0\0�\�d2\�6\�?\"�H$\�-\�?',_binary '\0\0\0\0\0\0\0�\�[�?\��z�\�u\�?',_binary '\0\0\0\0\0\0\0\�r�\\�a\�?\�l6�]�\�?'),(7,1,NULL,5,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0y<�ׯ�?G�\�h \�?\0\0\0_�\�\�\"\�?�����,\�?',_binary '\0\0\0\0\0\0\0o�\�\��\�?�\�\�p�g\�?',_binary '\0\0\0\0\0\0\0�\�\�PF\�?�|>��t�?',_binary '\0\0\0\0\0\0\0�\�\�t�u\�? �\r\�?'),(8,1,NULL,0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�D\"\�?[�V�U1�?\0\0\0\�`0�7\�?�\�b�\�\�?',_binary '\0\0\0\0\0\0\0�x<\� \�?�D\"\�.�?',_binary '\0\0\0\0\0\0\0\�h4\Z�Ϯ?\�p8.��?',_binary '\0\0\0\0\0\0\0�T*�z��?\�\����\�?');
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
INSERT INTO `target_ball_hit_point_hint` VALUES (0.03937007874015748,_binary '\0\0\0\0\0\0\0��~��~\�?�\�\���\�?',_binary '\0\0\0\0\0\0\0�\���\�?�\�\���\�?',1);
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
