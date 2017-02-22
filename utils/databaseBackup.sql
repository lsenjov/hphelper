-- MySQL dump 10.15  Distrib 10.0.28-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: localhost
-- ------------------------------------------------------
-- Server version	10.0.28-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cbay`
--

DROP TABLE IF EXISTS `cbay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cbay` (
  `c_id` int(11) DEFAULT NULL,
  `cbay_item` varchar(200) NOT NULL,
  `cbay_cost` int(11) NOT NULL,
  PRIMARY KEY (`cbay_item`),
  UNIQUE KEY `cbay_item_UNIQUE` (`cbay_item`),
  KEY `fk_cbay_1_idx` (`c_id`),
  CONSTRAINT `fk_cbay_1` FOREIGN KEY (`c_id`) REFERENCES `crisis` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `crisis`
--

DROP TABLE IF EXISTS `crisis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crisis` (
  `c_id` int(11) NOT NULL AUTO_INCREMENT,
  `c_type` enum('both','classic','straight') NOT NULL DEFAULT 'both',
  `c_desc` text NOT NULL,
  PRIMARY KEY (`c_id`),
  UNIQUE KEY `c_id_UNIQUE` (`c_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `crisis_tag`
--

DROP TABLE IF EXISTS `crisis_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crisis_tag` (
  `c_id` int(11) NOT NULL,
  `ct_tag` varchar(3) NOT NULL,
  PRIMARY KEY (`c_id`,`ct_tag`),
  CONSTRAINT `fk_crisis_tag_1` FOREIGN KEY (`c_id`) REFERENCES `crisis` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `crisis_text`
--

DROP TABLE IF EXISTS `crisis_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crisis_text` (
  `ct_id` int(11) NOT NULL AUTO_INCREMENT,
  `c_id` int(11) NOT NULL,
  `ct_desc` text NOT NULL,
  PRIMARY KEY (`ct_id`),
  KEY `fk_crisis_text_1_idx` (`c_id`),
  CONSTRAINT `fk_crisis_text_1` FOREIGN KEY (`c_id`) REFERENCES `crisis` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=218 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drawbacks`
--

DROP TABLE IF EXISTS `drawbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drawbacks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `first_name`
--

DROP TABLE IF EXISTS `first_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `first_name` (
  `fn_id` int(11) NOT NULL AUTO_INCREMENT,
  `fn_name` varchar(45) NOT NULL,
  PRIMARY KEY (`fn_id`),
  UNIQUE KEY `fn_id_UNIQUE` (`fn_id`),
  UNIQUE KEY `fn_name_UNIQUE` (`fn_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2439 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `live_ind`
--

DROP TABLE IF EXISTS `live_ind`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `live_ind` (
  `live_ind_id` int(11) NOT NULL AUTO_INCREMENT,
  `live_ind_name` varchar(45) NOT NULL,
  `live_ind_current` int(11) NOT NULL DEFAULT '0',
  `live_ind_last` int(11) NOT NULL DEFAULT '0',
  `live_ind_start` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`live_ind_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `live_news`
--

DROP TABLE IF EXISTS `live_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `live_news` (
  `live_news_id` int(11) NOT NULL AUTO_INCREMENT,
  `live_news_desc` varchar(200) NOT NULL,
  PRIMARY KEY (`live_news_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minion`
--

DROP TABLE IF EXISTS `minion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minion` (
  `minion_id` int(11) NOT NULL AUTO_INCREMENT,
  `minion_name` varchar(45) NOT NULL,
  `minion_clearance` varchar(2) NOT NULL,
  `minion_cost` int(11) NOT NULL,
  `sg_id` int(11) NOT NULL,
  `minion_desc` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`minion_id`),
  KEY `fk_minion_1_idx` (`sg_id`),
  CONSTRAINT `fk_minion_1` FOREIGN KEY (`sg_id`) REFERENCES `sg` (`sg_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=433 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `minion_skill`
--

DROP TABLE IF EXISTS `minion_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `minion_skill` (
  `skills_id` int(11) NOT NULL,
  `minion_id` int(11) NOT NULL,
  `minion_skill_bonus` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`skills_id`,`minion_id`),
  KEY `fk_minion_skill_2_idx` (`minion_id`),
  CONSTRAINT `fk_minion_skill_1` FOREIGN KEY (`skills_id`) REFERENCES `skills` (`skills_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_minion_skill_2` FOREIGN KEY (`minion_id`) REFERENCES `minion` (`minion_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `minion_skills`
--

DROP TABLE IF EXISTS `minion_skills`;
/*!50001 DROP VIEW IF EXISTS `minion_skills`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `minion_skills` (
  `minion_id` tinyint NOT NULL,
  `minion_name` tinyint NOT NULL,
  `minion_clearance` tinyint NOT NULL,
  `minion_cost` tinyint NOT NULL,
  `mskills` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `mutations`
--

DROP TABLE IF EXISTS `mutations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mutations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `desc` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `name_old`
--

DROP TABLE IF EXISTS `name_old`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `name_old` (
  `name_id` int(11) NOT NULL AUTO_INCREMENT,
  `name_first` varchar(45) NOT NULL,
  `name_clearance` varchar(2) NOT NULL,
  `name_zone` varchar(3) NOT NULL,
  `name_clone` int(11) NOT NULL,
  PRIMARY KEY (`name_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
  `news_id` int(11) NOT NULL AUTO_INCREMENT,
  `news_desc` varchar(200) NOT NULL,
  `c_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`news_id`),
  KEY `fk_news_1_idx` (`c_id`),
  CONSTRAINT `fk_news_1` FOREIGN KEY (`c_id`) REFERENCES `crisis` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
  `resource_id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(200) NOT NULL,
  `resource_type` varchar(3) NOT NULL,
  PRIMARY KEY (`resource_id`),
  KEY `index2` (`resource_type`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sf`
--

DROP TABLE IF EXISTS `sf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sf` (
  `sf_id` int(11) NOT NULL AUTO_INCREMENT,
  `sf_name` varchar(45) NOT NULL,
  `sf_ad` varchar(200) NOT NULL,
  `sg_id` int(11) NOT NULL,
  PRIMARY KEY (`sf_id`),
  KEY `fk_sf_1_idx` (`sg_id`),
  CONSTRAINT `fk_sf_1` FOREIGN KEY (`sg_id`) REFERENCES `sg` (`sg_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sg`
--

DROP TABLE IF EXISTS `sg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sg` (
  `sg_id` int(11) NOT NULL AUTO_INCREMENT,
  `sg_name` varchar(200) NOT NULL,
  `sg_abbr` varchar(2) NOT NULL,
  PRIMARY KEY (`sg_id`),
  UNIQUE KEY `sg_name_UNIQUE` (`sg_name`),
  UNIQUE KEY `sg_abbr_UNIQUE` (`sg_abbr`),
  UNIQUE KEY `sg_id_UNIQUE` (`sg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sg_skill`
--

DROP TABLE IF EXISTS `sg_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sg_skill` (
  `sg_id` int(11) NOT NULL,
  `skills_id` int(11) NOT NULL,
  PRIMARY KEY (`sg_id`,`skills_id`),
  KEY `fk_sg_skill_1_idx` (`skills_id`),
  CONSTRAINT `fk_sg_skill_1` FOREIGN KEY (`skills_id`) REFERENCES `skills` (`skills_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sg_skill_2` FOREIGN KEY (`sg_id`) REFERENCES `sg` (`sg_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `sg_skills`
--

DROP TABLE IF EXISTS `sg_skills`;
/*!50001 DROP VIEW IF EXISTS `sg_skills`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `sg_skills` (
  `sg_name` tinyint NOT NULL,
  `sgskills` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `sgm`
--

DROP TABLE IF EXISTS `sgm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sgm` (
  `sgm_id` int(11) NOT NULL AUTO_INCREMENT,
  `sgm_text` varchar(200) NOT NULL,
  `sg_id` int(11) NOT NULL,
  `c_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`sgm_id`),
  KEY `index_sg` (`sg_id`),
  KEY `index_c` (`c_id`),
  CONSTRAINT `fk_sgm_1` FOREIGN KEY (`c_id`) REFERENCES `crisis` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_sgm_2` FOREIGN KEY (`sg_id`) REFERENCES `sg` (`sg_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `skills`
--

DROP TABLE IF EXISTS `skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skills` (
  `skills_id` int(11) NOT NULL AUTO_INCREMENT,
  `skills_name` varchar(45) NOT NULL,
  `skills_parent` varchar(2) NOT NULL,
  `skills_desc` text,
  PRIMARY KEY (`skills_id`),
  UNIQUE KEY `skills_name_UNIQUE` (`skills_name`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ss`
--

DROP TABLE IF EXISTS `ss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss` (
  `ss_id` int(11) NOT NULL AUTO_INCREMENT,
  `ss_name` varchar(100) NOT NULL,
  `ss_type` varchar(1) NOT NULL,
  `ss_desc` text NOT NULL,
  `ss_parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`ss_id`),
  KEY `fk_ss_1_idx` (`ss_parent`),
  CONSTRAINT `fk_ss_1` FOREIGN KEY (`ss_parent`) REFERENCES `ss` (`ss_id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ss_skill`
--

DROP TABLE IF EXISTS `ss_skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss_skill` (
  `ss_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  `ss_skill_bonus` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ss_id`,`skill_id`),
  KEY `fk_ss_skill_1_idx` (`skill_id`),
  CONSTRAINT `fk_ss_skill_1` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`skills_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ss_skill_2` FOREIGN KEY (`ss_id`) REFERENCES `ss` (`ss_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `ss_skills`
--

DROP TABLE IF EXISTS `ss_skills`;
/*!50001 DROP VIEW IF EXISTS `ss_skills`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `ss_skills` (
  `ss_id` tinyint NOT NULL,
  `ss_name` tinyint NOT NULL,
  `ss_desc` tinyint NOT NULL,
  `ss_type` tinyint NOT NULL,
  `ss_parent` tinyint NOT NULL,
  `sskills` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `ssm`
--

DROP TABLE IF EXISTS `ssm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ssm` (
  `ssm_id` int(11) NOT NULL AUTO_INCREMENT,
  `ss_id` int(11) NOT NULL,
  `c_id` int(11) DEFAULT NULL,
  `ssm_text` text NOT NULL,
  PRIMARY KEY (`ssm_id`),
  KEY `fk_ssm_1_idx` (`c_id`),
  KEY `fk_ssm_2_idx` (`ss_id`),
  CONSTRAINT `fk_ssm_1` FOREIGN KEY (`c_id`) REFERENCES `crisis` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ssm_2` FOREIGN KEY (`ss_id`) REFERENCES `ss` (`ss_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=316 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `minion_skills`
--

/*!50001 DROP TABLE IF EXISTS `minion_skills`*/;
/*!50001 DROP VIEW IF EXISTS `minion_skills`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`fc`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `minion_skills` AS select `minion`.`minion_id` AS `minion_id`,`minion`.`minion_name` AS `minion_name`,`minion`.`minion_clearance` AS `minion_clearance`,`minion`.`minion_cost` AS `minion_cost`,group_concat(concat(`skills`.`skills_name`,if((`skills`.`skills_parent` = 'O'),'',concat('(',`skills`.`skills_parent`,')')),if((`minion_skill`.`minion_skill_bonus` > 0),concat(' +',(`minion_skill`.`minion_skill_bonus` * 4)),'')) separator ', ') AS `mskills` from ((`minion` join `minion_skill`) join `skills`) where ((`minion_skill`.`minion_id` = `minion`.`minion_id`) and (`minion_skill`.`skills_id` = `skills`.`skills_id`)) group by `minion`.`minion_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sg_skills`
--

/*!50001 DROP TABLE IF EXISTS `sg_skills`*/;
/*!50001 DROP VIEW IF EXISTS `sg_skills`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`fc`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `sg_skills` AS select `sg`.`sg_name` AS `sg_name`,group_concat(`skills`.`skills_name` separator ', ') AS `sgskills` from ((`sg` join `sg_skill`) join `skills`) where ((`sg_skill`.`sg_id` = `sg`.`sg_id`) and (`sg_skill`.`skills_id` = `skills`.`skills_id`)) group by `sg`.`sg_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `ss_skills`
--

/*!50001 DROP TABLE IF EXISTS `ss_skills`*/;
/*!50001 DROP VIEW IF EXISTS `ss_skills`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`fc`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `ss_skills` AS select `ss`.`ss_id` AS `ss_id`,`ss`.`ss_name` AS `ss_name`,`ss`.`ss_desc` AS `ss_desc`,`ss`.`ss_type` AS `ss_type`,`ss`.`ss_parent` AS `ss_parent`,group_concat(concat(`skills`.`skills_name`,if((`ss_skill`.`ss_skill_bonus` > 0),concat(' +',(`ss_skill`.`ss_skill_bonus` * 4)),'')) separator ', ') AS `sskills` from ((`ss` join `ss_skill`) join `skills`) where ((`ss_skill`.`ss_id` = `ss`.`ss_id`) and (`ss_skill`.`skill_id` = `skills`.`skills_id`)) group by `ss`.`ss_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-02-22 13:34:41
