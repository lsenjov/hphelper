-- MySQL dump 10.13  Distrib 5.5.46, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: hphelper
-- ------------------------------------------------------
-- Server version	5.5.46-0ubuntu0.14.04.2

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
-- Table structure for table `crisis`
--

DROP TABLE IF EXISTS `crisis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crisis` (
  `c_id` int(11) NOT NULL AUTO_INCREMENT,
  `c_type` enum('both','classic','straight') NOT NULL DEFAULT 'both',
  `c_desc` varchar(200) NOT NULL,
  PRIMARY KEY (`c_id`),
  UNIQUE KEY `c_id_UNIQUE` (`c_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crisis`
--

LOCK TABLES `crisis` WRITE;
/*!40000 ALTER TABLE `crisis` DISABLE KEYS */;
INSERT INTO `crisis` VALUES (1,'both','ALERT: Authorised Termination rates are at 0. Rectify immediately! This information is classified GREEN.'),(2,'both','Beloved Citizens! The contract for transtube maintenance is up for tender today! Please monitor the situation and decide on the best service group to maintain the tubes.'),(3,'both','Alert! WMD project \'BRAIN TISSUE\' has gone missing from location databanks. Find and recover immediately. BRAIN TISSUE is classified INDIGO.'),(4,'both','Citizens! ##CIT-V-TR## is due for erasure later today. Make sure his name is purged from all databanks before his public erasure.'),(5,'both','Citizens! R&D proposal on project SOYLENT GREEN has been escalated. Please decide on a plan of action. SOYLENT GREEN is classified GREEN.'),(6,'both','Citizens! Oversee the installation of Project INFINITE STARS.'),(7,'straight','Citizens! A Communist superweapon has been deployed around the Complex. Decide on course of action. This information is classified INDIGO.'),(8,'both','Citizens! Compliance Index falling at unacceptable rate. Please identify cause and rectify.'),(9,'both','ALERT! Major traffic disturbances detected in Transtube ##ZON##-182. Rectify situation and decide who is responsible for damages.'),(10,'straight','Alert! Information has surfaced that a major terrorist attack is to take place this daycycle. Prevent this attack at all costs.'),(11,'both','Citizens! ##ZON## reactor 190 is ready to go online. Please use this opportunity to raise morale across the sector.'),(12,'both','Alert! Enemy invasion force at edge of sector! Identify and eliminate immediately!'),(13,'classic','Alert! Large flooding occuring on subbasements 15-28. Identify and fix source of flooding immediately.');
/*!40000 ALTER TABLE `crisis` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `crisis_tag`
--

LOCK TABLES `crisis_tag` WRITE;
/*!40000 ALTER TABLE `crisis_tag` DISABLE KEYS */;
INSERT INTO `crisis_tag` VALUES (1,'CID'),(1,'HIU'),(1,'ISD'),(1,'SID'),(2,'SID'),(3,'AFD'),(3,'CPD'),(3,'RDD'),(4,'AFU'),(4,'ISU'),(4,'SIU'),(5,'CPU'),(5,'ISU'),(5,'PLD'),(5,'RDU'),(6,'PSU'),(6,'RDU'),(7,'HID'),(7,'PSU'),(7,'SID'),(8,'CID'),(8,'PLU'),(9,'HID'),(9,'PLD'),(9,'TSD'),(10,'HIU'),(10,'HPU'),(10,'TSD'),(11,'PSU'),(12,'CIU'),(13,'CID'),(13,'SID');
/*!40000 ALTER TABLE `crisis_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crisis_text`
--

DROP TABLE IF EXISTS `crisis_text`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crisis_text` (
  `ct_id` int(11) NOT NULL AUTO_INCREMENT,
  `c_id` int(11) NOT NULL,
  `ct_desc` varchar(200) NOT NULL,
  PRIMARY KEY (`ct_id`),
  KEY `fk_crisis_text_1_idx` (`c_id`),
  CONSTRAINT `fk_crisis_text_1` FOREIGN KEY (`c_id`) REFERENCES `crisis` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crisis_text`
--

LOCK TABLES `crisis_text` WRITE;
/*!40000 ALTER TABLE `crisis_text` DISABLE KEYS */;
INSERT INTO `crisis_text` VALUES (1,1,'CPU recently cut funding to termination stamping, leaving only one worker stamping all vouchers: ##CIT-Y-1##.'),(2,1,'A few daycycles ago, he lost his ME card in the pneumatic tubes, making him unable to leave the room or get help.'),(3,1,'Diligently, he kept stamping in the hope someone would come find him, he died from lack of water yesterdaycycle.'),(4,1,'No vouchers have been authorised in a day, meaning no authorised terminations have taken place.'),(5,2,'Due to a large string of timetable failures and deaths, the computer has decided on a competition: one day in which PS, TS, and HPD split the tracks and manage them.'),(6,2,'The transtubes are split equally between PS, TS, and HPD.'),(7,2,'Obviously there\'s not nearly enough time for the other two to manage their tubes properly, so they\'re planning on sabotaging the hell out of Power Services networks.'),(8,3,'BRAIN TISSUE is a highly destructive weapon capable of vapourising 1/10th of the sector\'s population each time it is fired (their clothes and belongings are intact)'),(9,3,'BRAIN TISSUE has a delay between firings (whatever is thematically appropriate)'),(10,3,'BRAIN TISSUE has been stolen by a FCCC-P splinter cell, and threatens to destroy the sector unless their demands are met.'),(11,3,'The demands start large (Live coverage on the sector Vidscreens) and get even more elaborate (Promotion of all the sect to VIOLET).'),(12,3,'Remember to find the scapegoat for this. It was lost at a transfer between R&D and AF, overseen by CPU'),(13,4,'Bit of background. In the year 182, there was another ##CIT-V-TR## who got a bit too high up too fast, and so a clever high-programmer made that particular phrase summon Friend Computer Immediately.'),(14,4,'The summons also modifies friend computer\'s threat level, raising it by one each time its name is said. Each time FC hears ##CIT-V-TR##\'s name, it shows up with a suddenly hightened threat level and'),(15,4,'doesn\'t know why. ##CIT-V-TR## only reached VIOLET three days ago, and is already up for erasure. FC suspects something about the name, which is why it requests a full purge.'),(16,4,'Anagram: TTS- Treason Tracking Server'),(17,4,'##CIT-V-TR## took an extremely large dose of the drug RadicalMankey, which is a major mutagenic, and it will kick in whenever appropriate.'),(18,5,'SOYLENT GREEN is a proposal to change the standard gene template of clones to photosynthesize.'),(19,5,'The proposal is led by ##CIT-G-SG##, an (unsurprisingly) Sierra Club member.'),(20,5,'##CIT-G-SG## has the go-ahead for a small trial run, and the tech actually works, however:'),(21,5,'--The green gene removes digestive systems, meaning no food can be sold to these clones.'),(22,5,'--Green citizens quickly grow fat and immobile, as the gene is designed for outdoor background radiation, not AC radiation levels.'),(23,5,'The Programmers also have to decide whether to standardise the technology'),(24,5,'--If they do, the programmers and the rest of the sector become registered mutants immediately.'),(25,5,'--If they don\'t, the programmers must find a scapegoat for why the efficency boost wasn\'t implemented.'),(28,7,'A supervolcano has erupted, covering the planet with a layer of ash clouds which constantly storm.'),(29,7,'The lightning is so loud and frequent it can be heard on multiple outer layers of the complex, causing panic.'),(30,7,'HPs must find a reasonable excuse for the sounds to stop the panic, while finding a scapegoat for the attack.'),(31,8,'The Petbot 214 was released today, and has sold out within minutes. They were designed with subliminal messaging propaganda tech.'),(32,8,'The subliminal tech was hijacked by some phreaks as a contract for Corpore Metal. The petbot encourages owners to get into dangerous, maiming situations.'),(33,9,'A load of PLC\'s Cold Fun syrup fell into one end of the transtube ##ZON##-182 after a navigation error caused the truckbot to crash.'),(34,9,'Cold Fun syrup sits at around 1 Kelvin for thousands of years, and is usually mixed in micro quantities to make cold fun.'),(35,9,'The Cold Fun Syrup is causing the air to liquify and mix with the syrup, causing it to spread further down the transtubes.'),(36,9,'PLC is blaming TS for the truckbot crashing. TS is blaming CPU for not updating the maps. CPU is trying to cover its tracks.'),(37,9,'In the end, there is a 15 ACCESS damages fee, in addition to any changes from directives.'),(38,10,'This information is based of a piece of paper recovered from a terminated achivist, listing several dates.'),(39,10,'These were actually the dates of the completion of several major transtube works in the past.'),(40,10,'Due to the recycling of the year 214 and a set of coincidences, all the other dates have matched with major sewerage overflows.'),(41,10,'The final date on the list is this daycycle. IntSec is desperately looking for an attack, AF wants to embarrass them.'),(42,10,'Tech Services and HPD are preparing for a Massive FunBall game that\'s about to take place, and when half-time hits and everyone uses the restrooms...'),(43,10,'Specifically, the sewerage will overflow 10 minutes before the end of the match.'),(44,6,'INFINITE STARS is a power project designed to balance power fluctuations. Works off wierd science.'),(45,6,'Whenever the draw is high, the project pushes more power into the system, exponentially. Above a certain load, the project supplies virtually limitless power.'),(46,6,'So much power, in fact, it plays havoc with electrical systems and bot brains in the sector.'),(47,6,'If the players shut off the other reactors, the draw becomes higher, and players may find it impossible to shut the project down.'),(48,11,'The reactor doesn\'t work, and the project manager ##CIT-I-1## knows it. They\'re planning on faking a meltdown to get minions sent down.'),(49,11,'If the supplies are sent, the reactor actually starts melting down.'),(50,12,'Due to a bug in a recent software patch, the compnode of ##ZON## no longer recognizes a neighbouring sector as existing.'),(51,12,'Thusly, the computer sees all six million inhabitants plus bots as invaders, and views the sector as being outside.'),(52,12,'In a couple hours time, the software is reverted and the computer has no memory of seeing the sector as non-existant.'),(53,12,'PLC doesn\'t actually have enough laser barrels for the troops.'),(54,13,'A servants of cthulu sect has successfully managed to build a temple on sublevel 12, and finished a major ceremony. A gate has opened and is flooding water out to the lower levels.'),(55,13,'No gate has actually opened, it\'s just a burst water main, but the cultists are too feverish from hallucinogens in the water to tell the difference.'),(56,13,'Minions will report horrible creatures bursting from the water where none exist.');
/*!40000 ALTER TABLE `crisis_text` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `drawbacks`
--

LOCK TABLES `drawbacks` WRITE;
/*!40000 ALTER TABLE `drawbacks` DISABLE KEYS */;
INSERT INTO `drawbacks` VALUES (1,'Quirky Clone Template: Add one to your clone degredation.'),(2,'No more hormone suppressants. You\'re free of hormone suppressants, and lust after attractive members of the opposite sex. You\'ve probably got a harem or even children.'),(3,'Obessive Collector: You have an obsessive interest in collecting something: specific old reckoning artefacts, trilobites, WMDs, or something else specific'),(4,'Degenerate: You overindulge in something, food, alcohol, drugs, or something else'),(5,'Rival: Another high programmer, an NPC, really really hates you.'),(6,'Skeleton in the Closet: You did something in your past that\'s an erasable offence. Decide what it is and tell the GM.'),(7,'Secret Society Vendetta: A random secret society hates you, and you can never have an agent in that society.'),(8,'Service Group Vendetta: GM picks a service group, they hate you, you can never have an investment in that group and you can never have a minion from that group.'),(9,'Infamous: the public hate you, you now have a public standing that starts a 0 and can never go above 0.'),(10,'Mistrusted: Friend Computer doesn\'t trust you, you start each game and new clone with 10 treason points.'),(11,'Secret Society Debt: You owe a random secret society something big. You must attempt to complete their missions, but gain no benefit from doing so.'),(12,'Phobia: You\'re unreasonably afraid of something. Pick the something.'),(13,'Addiction: You have something specific you can\'t live without. A specific drug, flavour of B3 or Toothpaste, or even blood.'),(14,'Replicative Fading: Your clone template\'s rotten. Add two to your Clone Degredation.'),(15,'Cyborged: You\'ve got bot bits, they\'re obvious, and you are affected by attacks that affect bots.'),(16,'Insane: You\'re a grade-A nutso. Despite this, you\'re still mostly functional. Sure, you may think you\'re a giant cockroach, but you still make it to the situation room.'),(17,'Bizarre Experiment: You\'re a survivor - or result - of a bizarre experiment. Maybe you\'re a sentient android or a time traveller. Whatever you are, you\'re pretending to be a high programmer.'),(18,'Impending Doom. Inform the GM of this, and move along citizen, nothing to see here.'),(19,'Brain in a Jar: You\'re a brain in a jar. You\'ve got robotic attachments and servants so you can affect the world but you\'re still physically impaired, because you\'re a brain in a jar.');
/*!40000 ALTER TABLE `drawbacks` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `live_ind`
--

LOCK TABLES `live_ind` WRITE;
/*!40000 ALTER TABLE `live_ind` DISABLE KEYS */;
INSERT INTO `live_ind` VALUES (1,'HI',0,-1,-4),(2,'SI',2,1,3),(3,'CI',0,-1,-1),(4,'LI',-2,-1,-1),(5,'AF',0,0,-5),(6,'CP',2,4,3),(7,'HP',-2,0,4),(8,'IS',1,1,4),(9,'PS',-2,-2,-3),(10,'TS',-1,-1,-1),(11,'RD',2,0,-1),(12,'PL',1,1,2),(13,'TD',-1,-2,1);
/*!40000 ALTER TABLE `live_ind` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `live_news`
--

LOCK TABLES `live_news` WRITE;
/*!40000 ALTER TABLE `live_news` DISABLE KEYS */;
INSERT INTO `live_news` VALUES (1,'Reading this message is treasonous. Report for termination citizen.'),(2,'The Happy BrightFuture Re-education centre awaits you citizen.');
/*!40000 ALTER TABLE `live_news` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`minion_id`),
  KEY `fk_minion_1_idx` (`sg_id`),
  CONSTRAINT `fk_minion_1` FOREIGN KEY (`sg_id`) REFERENCES `sg` (`sg_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `minion`
--

LOCK TABLES `minion` WRITE;
/*!40000 ALTER TABLE `minion` DISABLE KEYS */;
INSERT INTO `minion` VALUES (1,'Armed Forces Friends Network','O',2,1),(2,'Armed Forces Friends Network','B',5,1),(3,'Bodyguard Services','B',5,1),(4,'Bodyguard Services','V',8,1),(5,'Crowd Control','R',2,1),(6,'Crowd Control','O',3,1),(7,'Sensitivity Trainers','Y',3,1),(8,'Sensitivity Trainers','G',4,1),(9,'Vulture Squadron Recruiters','G',3,1),(10,'Outdoor Rangers','G',3,1),(11,'Heroic Infantry','IR',1,1),(12,'Heroic Infantry','R',3,1),(13,'Officer Brigade','B',5,1),(14,'Officer Brigade','V',8,1),(15,'Transportation Bottalion','R',2,1),(16,'Armed Forces Marching Band','R',2,1),(17,'Vulture Squadron Warriors','B',7,1),(18,'VultureCraft Assault Squadron','B',7,1),(19,'Mark IV Warbot','G',8,1),(20,'ICBM Launch','I',6,1),(21,'Volunteer Collection Agencies','R',2,2),(22,'Management Focus Group','G',8,2),(23,'A Lot of Yellowpants','Y',6,2),(24,'Computer Care Specialists','I',6,2),(25,'Better Living Thru Chemistry','R',4,2),(26,'Better Living Thru Chemistry','G',6,2),(27,'Cheery Complex Initiative','R',2,2),(28,'Cheery Complex Initiative','Y',3,2),(29,'Cheery Complex Initiative','B',4,2),(30,'Archives Department','R',2,2),(31,'Archives Department','G',3,2),(32,'Archives Deparment','I',5,2),(33,'Foreign Policy Strategic Working Group','B',5,2),(34,'Mandatory Break Monitors','O',4,2),(35,'Facility Surveillance Control','Y',3,3),(36,'Facility Surveillance Control','B',5,3),(37,'News Services','Y',4,3),(38,'News Services','G',6,3),(39,'News Services','I',8,3),(40,'Public Hating Co-Ordination','R',2,3),(41,'Trend Identifiers','R',2,3),(42,'Singalong Agents','R',2,3),(43,'Subliminals Police','B',6,3),(44,'Housing Services Supply','R',4,3),(45,'INFRARED Wranglers','R',3,3),(46,'Mandatory Fun Time Enthusiasts','R',3,3),(47,'Temporary Filing Staff Requisition','R',4,3),(48,'Celebrity Lifestyle Documenters','Y',4,3),(49,'Celebrity Lifestyle Documenters','B',7,3),(50,'Celebrity Lifestyle Documenters','V',10,3),(51,'Forensic Analysis Scrubbot Team','O',2,8),(52,'Bright Vision Re-Education Centre','B',5,8),(53,'Loyalty Surveyors','Y',4,8),(54,'Threat Assessors','Y',4,8),(55,'Secure Security Checkpoint Checkers','Y',3,8),(56,'Total Surveillance Assurance','G',4,8),(57,'Agent Provocateurs','B',6,8),(58,'IntSec Troopers','B',6,8),(59,'Traffic Patrol','B',6,8),(60,'Jackbooted Thugs','G',4,8),(61,'Facilitation Division','Y',3,8),(62,'Facilitation Division','B',4,8),(63,'Facilitation Division','V',5,8),(64,'Mutant Registration','O',2,8),(65,'Information Retrieval Specialists','Y',4,8),(66,'Information Retrieval Specialists','B',5,8),(67,'Information Retrieval Specialists','V',7,8),(68,'Men in INDIGO','I',6,8),(69,'Conspicuous Surveillance Initiative','O',3,8),(70,'BLUE Room Caterers','R',2,4),(71,'BLUE Room Caterers','Y',5,4),(72,'BLUE Room Caterers','B',5,4),(73,'BLUE Room Caterers','V',7,4),(74,'Equipment Assembly Control','IR',1,4),(75,'Field Logistics Advisors','R',3,4),(76,'Food Vat Control','IR',2,4),(77,'Inventory System Updaters','R',4,4),(78,'Brand Loyalty Police','R',3,4),(79,'Acme Chemical Production','R',3,4),(80,'BLUE Shield Clone Assurance','B',5,4),(81,'PLC Accounts Co-Ordination','O',6,4),(82,'New Flavour of Bouncy Bubble Beverage','IR',2,4),(83,'Advertising Campaign','R',4,4),(84,'C-Bay','G',5,4),(85,'Enforced Reclamation and Recycling','Y',4,4),(86,'Circuit Maintenance','R',3,6),(87,'Fuel Rod Disposal Consultants','R',4,6),(88,'Pneumatic Tube Network Engineers','Y',8,6),(89,'Department of Transbot Control','O',7,6),(90,'New Transtube Planning Commission','I',8,6),(91,'Reactor Management Commission','G',4,6),(92,'Reactor Shielding Volunteer Corps','R',4,6),(93,'Crawlspace Commandoes','R',5,6),(94,'Vault Recovery Team','G',4,6),(95,'Toxic Environment Team','O',3,6),(96,'Biological Niceness Indexers','B',7,7),(97,'Security Technology Technicians','B',6,7),(98,'Bot Processing','Y',5,7),(99,'Drug Interaction Testers','G',5,7),(100,'Codename: KILLBOT','G',4,7),(101,'Doomsday Device','V',8,7),(102,'Atomic Science Ethical Directorate','G',6,7),(103,'Think Tank Consultants','I',12,7),(104,'Silicon Corridor','B',7,7),(105,'Special Environment Clone Laboratories','G',7,7),(106,'Historical Artefact Analysis','Y',6,7),(107,'Experimental Equipment Field Testing','O',6,7),(108,'Foreign Contaminant Containment','O',7,7),(109,'Technically Non-Lethal Weapons','Y',5,7),(110,'Security Systems Installers','Y',5,5),(111,'Security Systems Installers','I',9,5),(112,'Clone Tank Support Services','R',3,5),(113,'Medical Services','Y',4,5),(115,'Medical Services','I',8,5),(116,'Paint Control','R',3,5),(117,'Slime Identification','O',4,5),(118,'Tech Support','Y',4,5),(119,'Abandoned Sector Reclamation Initiative','G',8,5),(120,'Dome Cleaning Services','O',5,5),(121,'Department of Pipes and Tubes','R',6,5),(122,'Bot Repair and Maintenance','Y',5,5),(123,'Scrubbot Army','R',3,5),(124,'Megastructure Construction Planning Group','B',12,5),(125,'Non-Specific Unit Production','G',6,5),(126,'Outside Broadcast Unit','Y',4,5),(127,'Alpha Complex Space Program','I',10,5),(128,'Vat Maintenance and Control','O',4,5),(129,'Vermin Terminators','R',4,5),(130,'Troubleshooter Team','R',1,9),(131,'Troubleshooter Team','O',2,9),(132,'Troubleshooter Team','Y',3,9),(133,'Troubleshooter Team','G',4,9),(134,'Troubleshooter Team','B',5,9),(135,'Troubleshooter Team','I',6,9),(136,'Troubleshooter Team','V',7,9),(137,'Alpha Team','I',16,9),(138,'Troubleshooter Dispatchers','B',5,9),(139,'Troubleshooter Debriefer','G',7,9),(140,'Sector Indexers','I',7,2),(141,'Credit Checkers','R',2,2),(142,'Productivity Maintainers','O',3,2),(143,'Queue Maintainers','R',2,3),(144,'Relocation Specialists','G',7,3);
/*!40000 ALTER TABLE `minion` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `minion_skill`
--

LOCK TABLES `minion_skill` WRITE;
/*!40000 ALTER TABLE `minion_skill` DISABLE KEYS */;
INSERT INTO `minion_skill` VALUES (1,23,1),(1,43,0),(1,54,0),(1,96,0),(2,21,0),(2,22,1),(2,89,0),(2,107,0),(2,137,0),(2,138,0),(2,143,0),(3,27,1),(3,28,1),(3,29,0),(3,96,0),(3,117,0),(3,120,0),(3,123,1),(3,144,0),(4,47,0),(4,52,0),(4,65,1),(4,66,1),(4,67,1),(5,22,1),(5,44,0),(5,47,0),(5,77,0),(5,81,0),(5,88,0),(6,7,0),(6,8,0),(6,40,0),(6,46,0),(6,78,0),(6,83,1),(7,41,0),(7,53,1),(7,140,1),(8,22,0),(8,23,0),(8,34,0),(8,68,0),(8,69,0),(8,81,0),(8,87,0),(9,47,0),(9,54,0),(9,57,1),(9,137,0),(10,2,0),(10,37,0),(10,38,1),(10,39,1),(10,49,0),(10,50,0),(10,139,0),(11,4,0),(11,55,0),(11,61,0),(11,62,0),(11,63,1),(11,97,1),(11,108,0),(11,110,0),(11,111,1),(12,35,0),(12,36,1),(12,48,0),(12,49,0),(12,50,0),(12,56,1),(12,97,1),(12,110,0),(12,111,1),(12,121,0),(13,30,0),(13,31,0),(13,32,0),(13,106,0),(13,119,0),(13,144,0),(14,57,0),(14,86,0),(15,84,1),(15,93,0),(16,11,0),(16,12,1),(16,17,1),(16,18,1),(16,19,0),(16,58,0),(16,59,1),(16,100,0),(16,109,0),(17,13,0),(17,14,1),(17,33,0),(18,5,0),(18,6,1),(18,45,0),(18,60,0),(18,89,0),(18,109,0),(19,19,0),(19,90,0),(19,101,2),(19,119,1),(19,124,0),(20,10,0),(20,120,0),(20,126,0),(21,3,0),(21,4,1),(21,58,1),(22,17,1),(22,61,0),(22,62,1),(22,63,1),(22,68,0),(22,93,0),(22,129,0),(22,137,1),(23,18,0),(23,19,0),(23,20,2),(23,101,2),(24,95,0),(24,98,0),(24,118,0),(24,120,0),(24,122,1),(24,124,0),(25,44,0),(25,90,0),(25,91,0),(25,119,0),(25,124,1),(26,79,0),(26,121,1),(26,128,0),(27,86,0),(27,93,0),(27,121,0),(27,127,0),(27,129,0),(28,87,0),(28,91,0),(28,92,0),(28,102,0),(29,74,0),(29,76,0),(29,79,0),(29,85,0),(29,125,1),(30,103,1),(30,107,0),(30,127,0),(30,137,0),(31,15,0),(31,59,0),(31,75,0),(31,88,0),(31,89,1),(31,90,0),(31,105,0),(31,137,0),(32,98,1),(32,100,0),(32,122,0),(33,1,0),(33,2,0),(33,13,0),(33,14,0),(33,88,0),(33,118,0),(33,126,0),(33,127,0),(33,137,0),(34,24,0),(34,104,0),(35,24,0),(35,30,0),(35,31,0),(35,32,0),(35,94,0),(35,106,0),(36,81,0),(36,84,1),(36,141,0),(37,24,0),(37,103,0),(37,104,1),(38,44,0),(38,75,0),(38,77,0),(38,88,0),(38,90,0),(38,103,0),(38,116,0),(38,125,0),(39,37,0),(39,38,1),(39,39,1),(39,48,0),(39,49,0),(39,50,0),(39,68,0),(39,83,1),(40,26,0),(40,51,0),(40,87,0),(40,94,0),(40,95,0),(40,96,0),(40,99,0),(40,108,0),(40,115,0),(40,117,0),(40,128,0),(41,70,0),(41,71,0),(41,72,1),(41,73,1),(41,76,0),(41,142,0),(42,80,1),(42,105,0),(42,112,0),(42,115,0),(43,99,0),(43,103,0),(43,108,0),(43,113,0),(43,115,1),(44,64,0),(44,102,0),(44,105,0),(45,33,0),(45,106,0),(45,108,0),(46,8,0),(46,25,0),(46,26,1),(46,45,0),(46,46,0),(46,82,1),(46,99,0),(47,25,0),(47,26,0),(47,42,0),(47,43,1),(48,47,0),(48,130,0),(48,131,0),(48,132,0),(48,133,0),(48,134,0),(48,135,0),(48,136,0),(48,137,0),(49,3,0),(49,9,0),(49,14,0),(49,16,0),(49,34,0),(49,52,0),(49,60,0),(49,85,0),(49,92,0),(49,139,0),(49,144,0),(50,17,0),(50,18,0),(51,19,0),(53,37,0),(53,48,0),(53,49,0),(53,50,0),(54,58,0),(55,94,0),(56,107,0),(57,126,0);
/*!40000 ALTER TABLE `minion_skill` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `mutations`
--

LOCK TABLES `mutations` WRITE;
/*!40000 ALTER TABLE `mutations` DISABLE KEYS */;
INSERT INTO `mutations` VALUES (1,'Bureaucratic Intuition','Gives a deep understanding of how to beat the Bureaucratic quagmire on a specific task.'),(2,'Charm','Produces pheromones that cause all weaker-willed clones in the area (not you or other High Programmers) to admire and trust other clones.'),(3,'Empathy','Read a clone\'s emotions and project your own emotions on to them.'),(4,'Deep Thought','Pick a problem you could solve given enough calculation time, you do it much, much faster.'),(5,'Machine Empathy','WARNING: ERASABLE MUTATION. All bots (including Friend Computer) will like you a lot more when you use this.'),(6,'Mechanical Intuition','With a few minutes, you can understand how a mechanical or electrical system works.'),(7,'Mental Blast','Knocks out all living beings (except yourself) in a nearby radius.'),(8,'Adrenaline Control','Temporarily makes you super fast, tough, and strong, takes its toll on the body.'),(9,'Environmental Control','Attuned to the life support of Alpha Complex, you can adjust heating, lighting, airflow, and other systems.'),(10,'Find Location','Pick a location/object/description. When looking at a location, you see the relative direction of your pick. Works through cameras.'),(11,'Radio Jamming','Jams all communications in a short radius for a short amount of time.'),(12,'Push Mutant Power','Amplifies (and may trigger) mutant powers in the immediate area.'),(13,'Uncanny Luck','Using this adjusts a roll in your favour...or reduces the roll of an opponent.'),(14,'Combat Mind','Gain intuition into exactly how to maneuver forces to win a battle.'),(15,'Copy Mutant Power','Copy the mutant power of a random, nearby mutant to use for a few minutes.'),(16,'Domination','Hijack a weak-willed npc\'s mind while your body sleeps for a little while.'),(17,'Forgettable','While active, memories of the next few minutes will slide from clone\'s memories.'),(18,'Psychic Flash','At opportune times, this will give a mental image of something important or dangerous in the near future.'),(19,'Telepathy','Dredge information from weak-willed clones or send psychic messages.'),(20,'Complex Intuition','Get a general feel for events and problems happening in the sector.');
/*!40000 ALTER TABLE `mutations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `name`
--

DROP TABLE IF EXISTS `name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `name` (
  `name_id` int(11) NOT NULL AUTO_INCREMENT,
  `name_first` varchar(45) NOT NULL,
  `name_clearance` varchar(2) NOT NULL,
  `name_zone` varchar(3) NOT NULL,
  `name_clone` int(11) NOT NULL,
  PRIMARY KEY (`name_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `name`
--

LOCK TABLES `name` WRITE;
/*!40000 ALTER TABLE `name` DISABLE KEYS */;
INSERT INTO `name` VALUES (1,'Dignif','Y','ING',1),(2,'Catacl','Y','SMM',1),(3,'Get','R','EKT',1),(4,'Falsef','R','ONT',1),(5,'Cryoph','O','BIA',1),(6,'Acroph','O','BIA',1),(7,'Paran','O','IAA',1),(8,'Carbon','Y','LIC',1),(9,'Polyester','G','IRL',1),(10,'Rei','G','NED',1),(11,'Roast','B','EEF',1),(12,'Cue','B','ONE',1),(13,'Advert','I','ZED',1),(14,'Dr','I','VER',1),(15,'Affida','V','ITS',1),(16,'Recidi','V','IST',1),(17,'Allosa','U','RUS',1),(18,'G','U','ARD',1),(19,'Canes','U','GAR',1),(20,'Abd','U','CED',1),(21,'Bag','U','ETT',3),(22,'Dys','U','RIA',1),(23,'Bra','V','ADO',1),(24,'Cra','V','ENN',1),(25,'Dri','V','ELS',1);
/*!40000 ALTER TABLE `name` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` VALUES (1,'Congratulations! You may be a winner! Call 1800-244-7226!',NULL),(2,'Alpha Complex Space Agency landing declared hoax! Execution of agency head ##CIT-I-RND## scheduled for 6.',NULL),(3,'ChocolateLyke Ration increased to 20 grams per day for INFRAREDs!',NULL),(4,'New craze \"planking\" declared waste of time and treasonous.',NULL),(5,'There was no shuddering of Alpha Complex. Alpha Complex is as stable as ever. Those who believe otherwise are encouraged to contact IntSec.',NULL),(6,'Survey on pronunciation of \"gif\" complete. 100% of respondents say soft-g. In unrelated news, terminations doubled yesterday.',NULL),(7,'##CIT-V-TR##\'s show trial and erasure due for 80:00 SCT todaycycle!',4),(8,'Painkiller RadicalMankey classified treasonous due to unintended side effects.',4),(9,'New CPU manager sends 800 staff to the re-employment office.',1),(10,'Alternative Power Firm Strike-Me-Not reports a 3000% increase in power generation this daycycle!',7),(11,'The new Petbot 214 released today, sells out in minutes!',8),(12,'CPU reports the air per person index has risen 4% in the last month!',9),(13,'Sold-out UltraSuperMegaBowl funball game between USS Patriots and MBB Ballers live todaycycle on Channel 5!',10),(14,'Power forecast: rolling brownouts down by 28%, blackouts down 41%.',6),(15,'A new reactor to be completed today! Citizens rejoice!',11),(16,'Serve your Complex! Battle the Commie Threat! Bonus Hot Fun! Enlist Today!',12),(17,'Friendly Ollie-U saves sector YRU yet again! Citizens Rejoice!',NULL),(18,'ChocolateLyke ration for INFRAREDs increased yet again!',NULL),(19,'Frank-U destroys fifteen communist strongholds, says end in sight for war on terror.',NULL),(20,'Terrorist bomb unlawfully terminates 180 clones, IntSec urges terrorists to hand themselves over before it becomes erasable.',NULL),(21,'Water rations to be decreased 30% due to increased B3 usage.',13);
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource`
--

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` VALUES (1,'Bubble Beverage Funball Stadium','LOC'),(2,'Compnode','LOC'),(3,'Nuclear Waste','RES'),(4,'Left Boots','RES'),(5,'FunFoods MegaPark','LOC'),(6,'Abandoned Nuclear Plant','LOC'),(7,'New Nuclear Plant','LOC'),(8,'WasteRenewal Toilet Complex','LOC'),(9,'Hot Fun','RES'),(10,'Drinking Water','RES'),(11,'Plutonium','RES'),(12,'Soy','RES'),(13,'Copper Wire','RES');
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `sf`
--

LOCK TABLES `sf` WRITE;
/*!40000 ALTER TABLE `sf` DISABLE KEYS */;
INSERT INTO `sf` VALUES (1,'HappyTainers','Keeping your soldiers laughing at us, not your enemy!',1),(2,'All That You Can Be Plus Plus','Let each day be an addition to your body: let us Plus Plus you up!',1),(3,'EnviroServices CPU','Billions of working combinations, only one is effective. Let us help you find it!',2),(4,'Know More','We plug the breaches in your paperwork: Know More with Know More!',2),(5,'Selfless Generosity Corp','Everyone needs someone to deride: we hire them for you!',3),(6,'NotCloning HPD','Uncanny Celebrity lookalikes, fast.',3),(7,'DottedLine Patrol','Keeping the undesirables of their sector, out.',8),(8,'Another Kind Of Beat','Keeping the eyes on us, so they keep their eyes off you.',8),(9,'HandCrafters PLC','Whatever you need, however you need, customised for your pleasure.',4),(10,'Selective Dining HPD','Fast food, served in 30 minutecycles or less!',4);
/*!40000 ALTER TABLE `sf` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `sg`
--

LOCK TABLES `sg` WRITE;
/*!40000 ALTER TABLE `sg` DISABLE KEYS */;
INSERT INTO `sg` VALUES (1,'Armed Forces','AF'),(2,'Central Processing Unit','CP'),(3,'HPD&MC','HP'),(4,'Production, Logistics, and Commisary','PL'),(5,'Tech Services','TS'),(6,'Power Services','PS'),(7,'Research and Development','RD'),(8,'Internal Security','IS'),(9,'Troubleshooter Dispatch','TD');
/*!40000 ALTER TABLE `sg` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `sg_skill`
--

LOCK TABLES `sg_skill` WRITE;
/*!40000 ALTER TABLE `sg_skill` DISABLE KEYS */;
INSERT INTO `sg_skill` VALUES (2,1),(8,1),(2,2),(3,2),(3,3),(5,3),(3,4),(8,4),(2,5),(3,5),(3,6),(4,6),(2,7),(3,7),(6,8),(8,8),(6,9),(8,9),(3,10),(8,10),(1,11),(7,11),(8,11),(5,12),(8,12),(2,13),(3,13),(6,14),(8,14),(4,15),(6,15),(1,16),(7,16),(1,17),(2,17),(1,18),(3,18),(8,18),(5,19),(7,19),(1,20),(5,20),(1,21),(8,21),(1,22),(8,22),(1,23),(7,23),(5,24),(6,24),(5,25),(6,25),(4,26),(5,26),(5,27),(6,27),(6,28),(7,28),(4,29),(5,29),(5,30),(7,30),(1,31),(4,31),(5,32),(7,32),(1,33),(5,33),(2,34),(7,34),(2,35),(6,35),(2,36),(4,36),(2,37),(7,37),(4,38),(6,38),(3,39),(4,39),(6,40),(7,40),(2,41),(4,41),(4,42),(5,42),(7,42),(5,43),(7,43),(3,44),(7,44),(8,44),(2,45),(7,45),(3,46),(4,46),(2,47),(3,47),(9,48),(1,49),(8,49);
/*!40000 ALTER TABLE `sg_skill` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sgm`
--

LOCK TABLES `sgm` WRITE;
/*!40000 ALTER TABLE `sgm` DISABLE KEYS */;
INSERT INTO `sgm` VALUES (1,'The grunts are getting restless. Make sure at least two infantry units are given something to do.',1,NULL),(2,'The budget for the new Mark V Warbot is tight. We need an R&D team and half a ton of reactor fuel to attempt to turn it on.',1,NULL),(3,'Efficiency is down 15.7%. Find a way to increase it. It doesn\'t matter what it is, it\'ll all balance out in the end.',2,NULL),(4,'Housing in ##ZON## is oversubscribed by 21%. Either build more housing or reduce demand.',3,NULL),(5,'We\'ve overproduced on ##RES-1## by six months. Find a use for the excess ##RES-1##.',4,NULL),(6,'We have a machine. We don\'t know what it does. Turning it on needs unfettered access to a reactor. We want to turn it on.',7,NULL),(7,'Our contract to rebuild sector ZQD is behind schedule. We need another couple of construction teams but don\'t let anyone know we\'re using them to finish ZQD.',5,NULL),(8,'We\'ve got a huge excess of spent fuel rods. Find a place to put them, please?',6,NULL),(9,'We haven\'t had a successful mission in a monthcycle, and the Big C\'s getting twitchy. Make sure our troubleshooters have a successful mission this daycycle.',9,NULL),(10,'The jackbooted idiots are blaming us for no termination vouchers being dispatched, make sure the blame falls on them and not on us.',2,1),(11,'Those idiots in CPU haven\'t sent us any termination vouchers for the last daycycle. Find out why and make sure they take the blame.',8,1),(12,'We want this contract but haven\'t the time to prepare...make sure Power Services\' tubes stop working and get us that contract.',5,2),(13,'The money we could get from this contract, not to mention the extra housing space. Do whatever it takes to get us the contract.',3,2),(14,'It\'s not our fault there were so many problems with the Transtube timetables! Make sure we keep this contract, or we\'re all toast.',6,2),(15,'Power Services have never given our troops free rides. Make sure whoever gets the contract promises free rides to Armed Forces grunts.',1,2),(16,'We were ambushed while getting the weapon from R&D. Make sure we don\'t get embarrased over this.',1,3),(17,'We filed the BRAIN TISSUE paperwork as complete before the exchange was ambushed. Make sure the exchange is completed before the end of the daycycle.',2,3),(18,'There\'s a small problem we\'ve located with BRAIN TISSUE, once it gets fired it can\'t be turned off. Destroy the device and make sure no-one else finds out about this little flaw.',7,3),(19,'##CIT-V-TR##\'s erasure has to go off without a hitch. The Big C\'s twitchy enough as it is.',3,4),(20,'Due to constraints, we can\'t actually remove ##CIT-V-TR##\'s name from the databanks until after their erasure, or it\'ll crash the TTS. Make sure it doesn\'t crash.',2,4),(21,'The Big C seems to have a sore spot for ##CIT-V-TR##, and the TTS isn\'t tracking why. Make sure the trial goes off fine, but delay the erasure so we can use \'em as bait.',8,4),(22,'##CIT-V-TR##\'s erasure is due for 80:00, but we want it pushed back to 90:00 for...reasons. We also want his body afterwards, get it to us.',7,4),(23,'SOYLENT GREEN is one of our best projects, and it actually works! Get it standardized sector-wide.',7,5),(24,'If SOYLENT GREEN gets standardised we\'ll lose at least half our revenue. Make sure it doesn\'t make the cut.',4,5),(25,'SOYLENT GREEN getting standardised means we can sell space in our reactors for growth! Get in standardised!',6,5),(26,'The nitwits at Power Services want SOYLENT GREEN standardised for some reason. Make sure it doesn\'t happen.',5,5),(27,'If SOYLENT GREEN is standardised, we won\'t be able to easily push drugs to the population. Stop it at all costs',3,5),(28,'If R&D\'s new gene is standardised, we can classify most of the sector as unregistered mutants and push our rates through the roof! Get it done.',8,5),(29,'Current projections of SOYLENT GREEN show an efficiency increase of 180%! Get it standardised!',2,5),(33,'This must be the fault of Beta Complex. We must march upon Beta Complex immediately!',1,7),(34,'This will lead to war if we don\'t act soon. Prevent a war with Beta Complex breaking out until we find the cause.',8,7),(35,'Our grid is almost overloaded! I can\'t believe I\'m saying this...but we need clones to use more power immediately!',6,7),(36,'There\'s a sudden drop of happiness in multiple subsectors. We need it cleared up pronto.',3,7),(37,'The Petbot 214 has a bit of a flaw, but make sure it doesn\'t get banned. It\'s set to have huge sales!',4,8),(38,'The Petbot 214 is a hit, but could use a few new features. Make sure the next production run encourages people to watch \"Late Night with Conan-O-BRN\"',3,8),(39,'The release of the Petbot 214 is ruining our metrics. Get it removed from sale however you can.',2,8),(40,'The new Petbot requires huge amounts of power, and needs to be rechared every two hours. The grid can\'t handle it, either get us more power or remove the petbots.',6,8),(41,'The concentration of petbots is at an exact level required for data gathering. Don\'t have them recalled, but don\'t let the next production run complete.',7,8),(42,'The truckbot with cold fun syrup crashed, make sure we don\'t take the blame or have to pay any damages.',4,9),(43,'That tunnel was a clear run on the map! Make sure CPU wear the blame for the Cold Fun syrup truckbot crash.',5,9),(44,'The maps for the transtubes were supposed to be updated last monthcycle but weren\'t. Cover us while we update the maps and records to show the update happened as planned.',2,9),(45,'The data shows a terrorist attack is supposed to hit today. Identify the threat, and make sure it\'s stopped.',8,10),(46,'Finally, a chance to embarrass those idiots at IntSec. Find what this supposed terrorist attack is supposed to be, and when it happens make sure IntSec take the blame.',1,10),(47,'Make sure the UltraSuperMegaBowl goes off without a hitch - broadcasting is on a 10 minute delay to the complex, it must not be interrupted.',3,10),(48,'Our techs have nicknamed today the MegaBowel, for obvious reasons. If there\'s anything in the pipes there may be problems. If it happens, don\'t let the blame fall on us.',5,10),(49,'Project INFINITE STARS is going ahead today, make sure it\'s a success!',7,6),(50,'INFINITE STARS is likely to cause us to lose the need of heaps of reactors! Don\'t let it succeed!',6,6),(51,'The reactor isn\'t actually ready to go yet, we need engineers sent down before it gets switched on. Whatever happens, make sure at the end of the daycycle the new reactor is working.',6,11),(52,'We\'ve been refused access to the opening ceremony of the new reactor, but you should have no problem. Get our cameras into the opening ceremony and make sure it looks good.',3,11),(53,'We\'ve had this huge stock of radiation badges for over a yearcycle. Sell one to every citizen of the sector.',4,11),(54,'We just got word of a huge invasion force! Get at least four of our units into the fight!',1,12),(55,'We\'ve finally got a solid war coming. Get heaps of combat footage of our troops winning for the usual propaganda purposes.',3,12),(56,'We\'re going to war. Prevent any major damage to the sector, or make sure the other service groups get blamed for it.',5,12),(57,'We don\'t actually have enough laser barrels to go to war with. Either make more fast or find alterative ammo sources.',4,12),(58,'The flooding water may be fresh water! If it is, store it somewhere so we can bottle it and sell it for megacredits!',4,13),(59,'Mission quota is down. Send at least 5 troubleshooter teams on missions before the end of the daycycle.',9,NULL),(60,'That entire flooded area was almost condemned. Let it get destroyed so we can get a juicy demolition contract out of it.',5,13),(61,'This flooding is bad for morale. Implement a couple of initiatives to make the proles feel good about the flooding.',3,13),(62,'We have reliable information one of the High Programmers in the room is a traitor. Find them and have them terminated before end of daycycle.',8,NULL);
/*!40000 ALTER TABLE `sgm` ENABLE KEYS */;
UNLOCK TABLES;

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
  PRIMARY KEY (`skills_id`),
  UNIQUE KEY `skills_name_UNIQUE` (`skills_name`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skills`
--

LOCK TABLES `skills` WRITE;
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
INSERT INTO `skills` VALUES (1,'Assessment','M'),(2,'Co-Ordination','M'),(3,'Hygiene','M'),(4,'Interrogation','M'),(5,'Paperwork','M'),(6,'Thought Control','M'),(7,'Thought Survey','M'),(8,'Covert Operations','Su'),(9,'Infiltration','Su'),(10,'Investigation','Su'),(11,'Security Systems','Su'),(12,'Surveillance','Su'),(13,'Cleanup','Su'),(14,'Sabotage','Su'),(15,'Black Marketeering','Su'),(16,'Assault','V'),(17,'Command','V'),(18,'Crowd Control','V'),(19,'Demolition','V'),(20,'Outdoor Ops','V'),(21,'Defence','V'),(22,'Wetwork','V'),(23,'Total War','V'),(24,'Bot Engineering','H'),(25,'Construction','H'),(26,'Chemical Eng','H'),(27,'Habitat Eng','H'),(28,'Nuclear Eng','H'),(29,'Production','H'),(30,'Weird Science','H'),(31,'Transport','H'),(32,'Bot Programming','So'),(33,'Communications','So'),(34,'Computer Security','So'),(35,'Data Retrieval','So'),(36,'Financial Systems','So'),(37,'Hacking','So'),(38,'Logistics','So'),(39,'Media Manipulation','So'),(40,'Biosciences','W'),(41,'Catering','W'),(42,'Cloning','W'),(43,'Medical','W'),(44,'Mutant Studies','W'),(45,'Outdoor Studies','W'),(46,'Pharmatherapy','W'),(47,'Sub. Messaging','W'),(48,'Troubleshooting','O'),(49,'Intimidation','M'),(50,'Must Not Fail','O'),(51,'Super Armoured','O'),(52,'Middle Managers','O'),(53,'Doubles Standing','O'),(54,'Rapid Response','O'),(55,'Vault Delvers','O'),(56,'Minion \'Enhancement\'','O'),(57,'Point of Contact','O'),(58,'Bigger Guns','O'),(59,'Propaganda','O'),(60,'Cyborging','O'),(61,'Disruption','O'),(62,'Procurement','O'),(63,'Mystic Weirdness','O'),(64,'Gadgeteering','O'),(65,'Old Stuff','O'),(66,'Running','O'),(67,'Salvage','O');
/*!40000 ALTER TABLE `skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ss`
--

DROP TABLE IF EXISTS `ss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ss` (
  `ss_id` int(11) NOT NULL AUTO_INCREMENT,
  `ss_name` varchar(45) NOT NULL,
  PRIMARY KEY (`ss_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ss`
--

LOCK TABLES `ss` WRITE;
/*!40000 ALTER TABLE `ss` DISABLE KEYS */;
INSERT INTO `ss` VALUES (1,'Anti-Mutant'),(2,'C.L.A.'),(3,'Clone Arrangers'),(4,'Communists'),(5,'Computer Phreaks'),(6,'Corpore Metal'),(7,'Death Leopard'),(8,'FCCC-P'),(9,'Free Enterprise'),(10,'Frankenstein Destroyers'),(11,'Humanists'),(12,'Mystics'),(13,'Pro Tech'),(14,'Psion'),(15,'PURGE'),(16,'Romantics'),(17,'Runners'),(18,'The Movement'),(19,'Sierra Club'),(20,'Servants of Cthulhu'),(21,'Wobblies');
/*!40000 ALTER TABLE `ss` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `ss_skill`
--

LOCK TABLES `ss_skill` WRITE;
/*!40000 ALTER TABLE `ss_skill` DISABLE KEYS */;
INSERT INTO `ss_skill` VALUES (1,10,0),(1,22,1),(1,40,0),(2,16,1),(2,21,0),(2,58,0),(3,40,0),(3,42,1),(3,44,0),(4,16,0),(4,27,0),(4,59,1),(5,35,0),(5,37,1),(5,39,0),(6,24,1),(6,32,0),(6,60,0),(7,16,1),(7,19,0),(7,61,0),(8,4,0),(8,6,1),(8,8,0),(9,36,1),(9,49,0),(9,62,0),(10,16,0),(10,24,0),(10,29,1),(11,1,0),(11,2,0),(11,5,1),(12,40,0),(12,46,1),(12,63,0),(13,30,1),(13,35,0),(13,64,0),(14,2,0),(14,9,0),(14,44,1),(15,16,0),(15,19,1),(15,37,0),(16,6,0),(16,62,0),(16,65,1),(17,11,1),(17,20,0),(17,66,0),(18,17,1),(18,23,0),(18,27,0),(19,40,0),(19,45,1),(19,67,0),(20,40,0),(20,44,0),(20,49,1),(21,25,0),(21,29,1),(21,59,0);
/*!40000 ALTER TABLE `ss_skill` ENABLE KEYS */;
UNLOCK TABLES;

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
  `ssm_text` varchar(200) NOT NULL,
  PRIMARY KEY (`ssm_id`),
  KEY `fk_ssm_1_idx` (`c_id`),
  KEY `fk_ssm_2_idx` (`ss_id`),
  CONSTRAINT `fk_ssm_1` FOREIGN KEY (`c_id`) REFERENCES `crisis` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ssm_2` FOREIGN KEY (`ss_id`) REFERENCES `ss` (`ss_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssm`
--

LOCK TABLES `ssm` WRITE;
/*!40000 ALTER TABLE `ssm` DISABLE KEYS */;
INSERT INTO `ssm` VALUES (1,1,NULL,'The muties have are getting too much peace, come down hard on mutants!'),(2,4,NULL,'Comrade! Pleasink to be spreadink Propaganda to Burgoise Complex!'),(3,5,NULL,'We\'re running out of washed caffiene. Can you deliver a truckload to ##LOC-1## for us?'),(4,6,NULL,'We have a surplus of cyborg parts, maim a good portion of the population so we can sell \'em off.'),(5,7,NULL,'Duuuude, we had a massive party last night and don\'t want to deal with the boys in blue... can you move IntSec out of the low clearance areas?'),(6,8,NULL,'We need more faith based initiatives...replace the morning anthem with a Hymn to our lord and savior, Friend Computer!'),(7,10,NULL,'There\'s too many bots around. Especially petbots, you can\'t sleep with those things around. Remove all petbots from the sector.'),(8,11,NULL,'Petbots are excellent for showing man\'s mastery over bots. Increase the number of petbots in the sector.'),(9,12,NULL,'Duuuuude, we had a massive rave last night, and we\'re out of uppers. Can you get us a transbotload of washed caffeine?'),(10,13,NULL,'We\'ve got a device we want to turn on but we don\'t know what it does. Can you get us unfettered access to a nuclear reactor to try?'),(11,15,NULL,'High value targets are currently under too much scrutiny, redirect IntSec to the lower-clearance areas.'),(12,16,NULL,'There\'s not enough pre-reckoning knowledge: get us onto the set of Celebrity Weekcycle and we\'ll do the rest.'),(13,19,NULL,'We need to tempt more clones with the wonders of the outdoors... can you get some trees into ##LOC-1##?'),(14,20,NULL,'La! The leylines intersect there, at ##LOC-1##! Build us a new temple there!'),(15,4,3,'Comrade! Ve are hearink rumours of superveapon gone missing! Recover and deliver veapon to us to aid great Lenin\'s cause.'),(16,8,3,'A heretical splinter group stole an experimental doomsday device and are hoarding it. Recover it for us so we can sanctify it before returning it.'),(17,9,NULL,'Yeah see, we got dis huge pile of defunct laser barrels. By huge, I\'s mean four million. Find us a buyer, and make sure IntSec\'s away.'),(18,2,NULL,'We\'re running low on armaments against the Commie threat! We need twenty tacnukes to beef up the arsenal by the end of the daycycle.'),(19,3,NULL,'We need more people to use our services. Kill off a high-profile citizen publicly to scare the masses.'),(20,17,NULL,'We\'ve got a secret escape route in ##LOC-1##, but there was a cave in. Repair it please.'),(21,21,NULL,'Comrade! We\'re going to hold a demonstration in the ##LOC-1##. Please make sure they don\'t run over us with tanks!'),(22,5,4,'The Big C\'s kinda...twitchy about the erasure for today, more so than usual. Get us your password so we can find out why.'),(23,3,4,'##CIT-V-TR## is a natural-born, no-one knows! Get us his spleen so we can use his genes.'),(24,20,4,'La! ##CIT-V-TR## is the sacrifice we need! Get them to us sedated! We\'ll be waiting in the ##LOC-SOC##.'),(25,17,4,'##CIT-V-TR## is a client of ours. Get him to us so we can get \'em out, we\'ll be at the ##LOC-RUN##.'),(26,12,4,'Whoa man, ##CIT-V-TR## paid well for a batch of RadicalMankey, but we can\'t find it, he must have eaten it. Get his stomach to use so we can pump the drugs back out.'),(27,7,1,'Whoa, the man\'s not keeping us down! Get us a pallet of B3 and a crate of WhiskeyLike to ##LOC-P## so we can throw a massive party!'),(28,12,1,'Whoa man! Like, we got space to breathe! Get us a load of Videoland to ##LOC-P## so we can rave, man!'),(29,3,1,'We haven\'t had any work all daycycle, it\'s costing us credits! Make a whole heap of clones die so we can get back to work.'),(30,2,1,'The commies have infiltrated IntSec! Get us five tacnukes so we can flush out the commie menace! We\'ll be waiting at the ##LOC-F##.'),(31,4,1,'Comrade! Ze IntSec veaklings are awaiting a blastink! Deliver us two hundred laser barrels so we can take ze fight to zem! We\'ll be waitink at ze ##LOC-F##'),(32,1,5,'Why are people tinkering with the human genome? Terminate ##CIT-G-SG##, he must be a filthy mutant.'),(33,3,5,'Why haven\'t we heard of SOYLENT GREEN before? Get us a copy of the data.'),(34,13,5,'SOYLENT GREEN is actually working? Get us a copy of the data to look at'),(35,14,5,'The next step in human evolution is here! Get SOYLENT GREEN standardised.'),(36,9,5,'See here, dis SOYLENT GREEN project? Destroy the data, so\'s we don\'t lose our customer base later.'),(37,19,5,'##CIT-G-SG## is one of ours. Make sure his project succeeds!'),(40,2,7,'The Commies are finally attacking! Equip all clones with weapons to fight the impending Commie Horde!'),(41,4,7,'Comrade! Is not beink our superveapon! You be pinning blame on Beta Complex, yes?'),(42,5,7,'Yo dude, our electronics are going haywire! Whatever it is, make it stop.'),(43,13,7,'This is perfect time for some of our reanimation tests! Get us a safe way to the outdoors so we can try our latest experiment!'),(44,8,7,'The endtime prophecy has begun! Now we must part the RED sea! Destroy the Commie scum in Beta Complex!'),(45,20,7,'La! The old ones come to greet us! We must sacrifice a High Programmer to the elder gods to open the gate! Get us a suitable sacrifice!'),(46,19,7,'Outside is beautiful right now! Broadcast footage of it to the entire complex!'),(47,6,8,'The new petbot was influenced by us, get the next production run pushed through this daycycle.'),(48,4,8,'Comrade! Dis new petbot is symbol of capitalist excess! Remove from population!'),(49,5,8,'Our contract on the petbot is done, order the current stock to do a factory reset so they pay us again.'),(50,10,8,'These new bots are abominations! Reveal them for what they are and recall the whole product line!'),(51,13,8,'These new petbots so cute! Make them mandatory for all REDs and above!'),(52,7,9,'Cold Fun Syrup? Dude, get us a boxful! We can do some amazing pranks with it!'),(53,4,9,'Comrade! Ve must to be gettink hands on syrup of Cold Fun!'),(54,15,9,'Get us a good amount of that Cold Fun Syrup, we\'ll finally be able to make a bomb big enough to take down the Computer.'),(55,9,9,'Look see, gets us a box of Cold Fun Syrup and we\'ll cut youse in on de profits.'),(56,21,10,'Comrade! UltraSuperMegaBowl is capitalist excess! Cause an evacuation of the stadium!'),(57,15,10,'We have a bomb hidden under the UltraSuperMegaBowl, but security is too tight for us to activate it. Get us an alternate way in.'),(58,9,10,'Look see, we know there\'s something juicy under the UltraSuperMegaBowl. Get it for us and we\'ll cut you in on de profits.'),(59,14,NULL,'We\'re needing to find more of our kind, add more mutagenics to the water supply.'),(60,18,NULL,'We\'re needing building materials, specifically 5000 tons concrete. Deliver it to ##LOC-1## for us to pick up.'),(61,4,11,'Comrade! New reactor helping bourgoise cause! Must be destroyink!'),(62,21,11,'Comrade! New reactor helping bourgoise cause! Must be destroyink!'),(63,15,11,'The new reactor is a prime target. Destroy before the end of the daycycle.'),(64,1,11,'A new reactor means more mutants. Implement a new anti-mutant policy before the end of the daycycle!'),(65,14,11,'Get a crowd of people near the new reactor to celebrate its opening.'),(66,2,12,'We heard of the Commie threat! Send us some bigger guns so we can prepare for the final defence!'),(67,3,12,'We\'re gonna need some more cloning tanks. Appropriate some of tech services\' tanks for us.'),(68,18,12,'This is our chance. Get us a way to the front lines, and we\'ll end this war once and for all.'),(69,20,13,'La! The ritual is almost complete! Delay the computer\'s lackeys long enough so we may summon the old ones!');
/*!40000 ALTER TABLE `ssm` ENABLE KEYS */;
UNLOCK TABLES;

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
/*!50001 VIEW `ss_skills` AS select `ss`.`ss_id` AS `ss_id`,`ss`.`ss_name` AS `ss_name`,group_concat(concat(`skills`.`skills_name`,if((`ss_skill`.`ss_skill_bonus` > 0),concat(' +',(`ss_skill`.`ss_skill_bonus` * 4)),'')) separator ', ') AS `sskills` from ((`ss` join `ss_skill`) join `skills`) where ((`ss_skill`.`ss_id` = `ss`.`ss_id`) and (`ss_skill`.`skill_id` = `skills`.`skills_id`)) group by `ss`.`ss_id` */;
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

-- Dump completed on 2016-02-03  2:22:46
