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
  `left_upper_corner` geometry DEFAULT NULL,
  `preset_name` varchar(255) DEFAULT NULL,
  `right_bottom_corner` geometry DEFAULT NULL,
  `right_upper_corner` geometry DEFAULT NULL,
  `left_bottom_cornerx` double NOT NULL,
  `left_bottom_cornery` double NOT NULL,
  `left_upper_cornerx` double NOT NULL,
  `left_upper_cornery` double NOT NULL,
  `right_bottom_cornerx` double NOT NULL,
  `right_bottom_cornery` double NOT NULL,
  `right_upper_cornerx` double NOT NULL,
  `right_upper_cornery` double NOT NULL,
  `table_size_in_cm` geometry DEFAULT NULL,
  `white_ball_density` int(11) NOT NULL,
  `left_bottom_corner_projector` geometry DEFAULT NULL,
  `left_upper_corner_projector` geometry DEFAULT NULL,
  `right_bottom_corner_projector` geometry DEFAULT NULL,
  `right_upper_corner_projector` geometry DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calibration_params`
--

LOCK TABLES `calibration_params` WRITE;
/*!40000 ALTER TABLE `calibration_params` DISABLE KEYS */;
INSERT INTO `calibration_params` VALUES (1,20,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\04@\0\0\0\0\0\��@','Default',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�@\0\0\0\0\0\0\0\0',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�@\0\0\0\0\0\��@',0,0,0,0,0,0,0,0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0�o@\0\0\0\0\0�_@',375000,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\��@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�@\0\0\0\0\0\0\0\0',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0�@\0\0\0\0\0\��@');
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
  `white_ball_position` geometry DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `individual_training`
--

LOCK TABLES `individual_training` WRITE;
/*!40000 ALTER TABLE `individual_training` DISABLE KEYS */;
INSERT INTO `individual_training` VALUES (1,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0$@\0\0\0\0\0\04@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0@\0\0\0\0\0\0@\0\0\0\0\0\0\0\0\0$@\0\0\0\0\0\0@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0@\0\0\0\0\0\0@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0@\0\0\0\0\0\0@'),(2,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\05@\0\0\0\0\0\0G@\0\0\0\0\0\0\0\0\0&@\0\0\0\0\0�U@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0@\0\0\0\0\0\0@\0\0\0\0\0\0\0\0\0(@\0\0\0\0\0\0,@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0@\0\0\0\0\0\0$@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0N@\0\0\0\0\0\0>@'),(3,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\05@\0\0\0\0\0�K@\0\0\0\0\0\0\0\0�@@\0\0\0\0\0\0(@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\05@\0\0\0\0\0\0$@\0\0\0\0\0\0\0\0\0@\0\0\0\0\0\0(@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0(@\0\0\0\0\0\0P@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\04@\0\0\0\0\0\0$@'),(4,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0&@\0\0\0\0\0\06@\0\0\0\0\0\0\0\0�@@\0\0\0\0\0\0F@\0\0\0\0\0\0\0\0@S@\0\0\0\0\0�^@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0@\0\0\0\0\0\0@\0\0\0\0\0\0\0\0\0@\0\0\0\0\0\0@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0$@\0\0\0\0\0\04@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0I@\0\0\0\0\0\0D@'),(5,1,NULL,0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0(@\0\0\0\0\0\04@\0\0\0\0\0\0\0\0\0>@\0\0\0\0\0\0D@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0I@\0\0\0\0\0\0D@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�Q@\0\0\0\0\0\0D@'),(6,1,NULL,0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0I@\0\0\0\0\0\�u@\0\0\0\0\0\0\0\0\0i@\0\0\0\0\0@@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0Y@\0\0\0\0\0\�u@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0i@\0\0\0\0\0\0i@'),(7,1,NULL,0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0��@\0\0\0\0\0\0y@\0\0\0\0\0\0\0\00�@\0\0\0\0\00�@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0 �@\0\0\0\0\0\0y@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0��@\0\0\0\0\0�b@'),(8,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0��@\0\0\0\0\0@o@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0��@\0\0\0\0\0�b@\0\0\0\0\0\0\0\0��@\0\0\0\0\0\0y@',_binary '\0\0\0\0\0\0\0\0\0\0\0\00�@\0\0\0\0\0@o@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0p�@\0\0\0\0\0�b@'),(9,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�r@\0\0\0\0\0\�u@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�?\0\0\0\0\0\0�?\0\0\0\0\0\0\0\0@o@\0\0\0\0\0@o@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0Y@\0\0\0\0\0\0i@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0y@\0\0\0\0\0\0y@'),(10,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0��@\0\0\0\0\0\�u@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�?\0\0\0\0\0\0�?\0\0\0\0\0\0\0\0@o@\0\0\0\0\0@o@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�r@\0\0\0\0\0\0i@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0h�@\0\0\0\0\0@@'),(11,1,NULL,0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�?\0\0\0\0\0\0�?\0\0\0\0\0\0\0\0@o@\0\0\0\0\0@o@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0@Z@\0\0\0\0\0@o@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0@o@\0\0\0\0\0\�z@'),(12,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�~@\0\0\0\0\0\0t@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0`e@\0\0\0\0\0�l@\0\0\0\0\0\0\0\0\0y@\0\0\0\0\0�|@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�k@\0\0\0\0\0\0t@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0��@\0\0\0\0\0\0y@'),(13,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0`u@\0\0\0\0\0\0y@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0`e@\0\0\0\0\0\0y@\0\0\0\0\0\0\0\0\0y@\0\0\0\0\0��@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�k@\0\0\0\0\0h�@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�@\0\0\0\0\0�\\@'),(14,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0`u@\0\0\0\0\0\�q@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0y@\0\0\0\0\0�\\@\0\0\0\0\0\0\0\0H�@\0\0\0\0\0\�q@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�@\0\0\0\0\0`e@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0`e@\0\0\0\0\0\0y@'),(15,1,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\�u@\0\0\0\0\0�q@',0,_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0y@\0\0\0\0\0�\\@\0\0\0\0\0\0\0\0��@\0\0\0\0\0\�q@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0@�@\0\0\0\0\0�d@',_binary '\0\0\0\0\0\0\0\0\0\0\0\0�f@\0\0\0\0\0`x@');
/*!40000 ALTER TABLE `individual_training` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-22 11:05:12
