-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: smart_glasses
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` varchar(30) DEFAULT 'ADMIN',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin_elaf','$2a$10$v6gZKqOQ7DHAlIfsEGSSX.apwyJSUBIHxghQLboAwwxz5dAf54wgS','ADMIN');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `face_analysis`
--

DROP TABLE IF EXISTS `face_analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `face_analysis` (
  `analysis_id` int NOT NULL AUTO_INCREMENT,
  `sessions_id` varchar(100) NOT NULL,
  `detected_shape_id` int DEFAULT NULL,
  `scan_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `confidence_score` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`analysis_id`),
  KEY `detected_shape_id` (`detected_shape_id`),
  CONSTRAINT `face_analysis_ibfk_1` FOREIGN KEY (`detected_shape_id`) REFERENCES `face_shapes` (`shape_id`),
  CONSTRAINT `face_analysis_chk_1` CHECK ((`confidence_score` between 0 and 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `face_analysis`
--

LOCK TABLES `face_analysis` WRITE;
/*!40000 ALTER TABLE `face_analysis` DISABLE KEYS */;
/*!40000 ALTER TABLE `face_analysis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `face_shapes`
--

DROP TABLE IF EXISTS `face_shapes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `face_shapes` (
  `shape_id` int NOT NULL AUTO_INCREMENT,
  `shape_name` varchar(50) NOT NULL,
  `description` text,
  PRIMARY KEY (`shape_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `face_shapes`
--

LOCK TABLES `face_shapes` WRITE;
/*!40000 ALTER TABLE `face_shapes` DISABLE KEYS */;
INSERT INTO `face_shapes` VALUES (1,'Oval','Balanced proportions with softly rounded contours. Rectangular, round, cat-eye, and geometric frames complement the natural balance.'),(2,'Round','Soft curves with full cheeks and similar width and length. Rectangular and angular frames add definition.'),(3,'Square','A defined jawline and angular features. Round frames soften sharp angles, while gently lifted cat-eye frames complement the face.'),(4,'Heart','A broad forehead and cheekbones with a narrow chin. Round and cat-eye frames help balance facial proportions.');
/*!40000 ALTER TABLE `face_shapes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `glasses_frames`
--

DROP TABLE IF EXISTS `glasses_frames`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `glasses_frames` (
  `frame_id` int NOT NULL AUTO_INCREMENT,
  `frame_name` varchar(100) NOT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `frame_style` varchar(50) DEFAULT NULL,
  `color` varchar(30) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `model_3d_url` varchar(255) DEFAULT NULL,
  `managed_by_admin_id` int DEFAULT NULL,
  PRIMARY KEY (`frame_id`),
  KEY `managed_by_admin_id` (`managed_by_admin_id`),
  CONSTRAINT `glasses_frames_ibfk_1` FOREIGN KEY (`managed_by_admin_id`) REFERENCES `admin` (`admin_id`),
  CONSTRAINT `glasses_frames_chk_1` CHECK ((`price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `glasses_frames`
--

LOCK TABLES `glasses_frames` WRITE;
/*!40000 ALTER TABLE `glasses_frames` DISABLE KEYS */;
INSERT INTO `glasses_frames` VALUES (1,'Noval Classic Square','NOVAL','Rectangular','Black',250.00,'assets/models/rect_3d.glb',1),(2,'Oyoon Aura Round','Oyoon','Round','Silver',300.00,'assets/models/geo_3d.glb',1),(3,'Navira Najd Cat-Eye','Navira','Cat-Eye','Tortoise',280.00,'assets/models/cateye_3d.glb',1),(4,'Jossen Air Titanium','Jossen','Oval','Blue',350.00,'assets/models/titanium_3d.glb',1);
/*!40000 ALTER TABLE `glasses_frames` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shape_frame_match`
--

DROP TABLE IF EXISTS `shape_frame_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shape_frame_match` (
  `match_id` int NOT NULL AUTO_INCREMENT,
  `shape_id` int NOT NULL,
  `frame_id` int NOT NULL,
  `suitability_score` int NOT NULL,
  PRIMARY KEY (`match_id`),
  UNIQUE KEY `unique_shape_frame` (`shape_id`,`frame_id`),
  UNIQUE KEY `UK9m5wmxh1fjj0w4pis8blchtq2` (`shape_id`,`frame_id`),
  KEY `frame_id` (`frame_id`),
  CONSTRAINT `shape_frame_match_ibfk_1` FOREIGN KEY (`shape_id`) REFERENCES `face_shapes` (`shape_id`),
  CONSTRAINT `shape_frame_match_ibfk_2` FOREIGN KEY (`frame_id`) REFERENCES `glasses_frames` (`frame_id`),
  CONSTRAINT `shape_frame_match_chk_1` CHECK ((`suitability_score` between 0 and 100))
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shape_frame_match`
--

LOCK TABLES `shape_frame_match` WRITE;
/*!40000 ALTER TABLE `shape_frame_match` DISABLE KEYS */;
INSERT INTO `shape_frame_match` VALUES (1,1,1,90),(2,1,2,92),(3,1,3,95),(4,1,4,88),(5,2,1,95),(6,2,3,90),(7,2,4,92),(8,3,2,95),(9,3,3,92),(10,4,2,90),(11,4,3,96);
/*!40000 ALTER TABLE `shape_frame_match` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-26 23:57:29
