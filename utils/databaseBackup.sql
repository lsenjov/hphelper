-- MySQL dump 10.16  Distrib 10.1.13-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: hphelper
-- ------------------------------------------------------
-- Server version	10.1.13-MariaDB

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
-- Dumping data for table `cbay`
--

LOCK TABLES `cbay` WRITE;
/*!40000 ALTER TABLE `cbay` DISABLE KEYS */;
INSERT INTO `cbay` VALUES (NULL,'12 Crates B3, GreenAndGulpy flavour',5),(NULL,'18 cans Sandatholon, sealed',2),(NULL,'20KG fissile grade plutonium',4),(NULL,'3 Ton Nuclear Waste, going fast!',1),(NULL,'4 surplus TacNukes',3),(NULL,'5 Ton Hot Fun! Barely used!',3),(NULL,'80 Tons water recently recycled!',4);
/*!40000 ALTER TABLE `cbay` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crisis`
--

LOCK TABLES `crisis` WRITE;
/*!40000 ALTER TABLE `crisis` DISABLE KEYS */;
INSERT INTO `crisis` VALUES (1,'both','ALERT: Authorised Termination rates are at 0. Rectify immediately! This information is classified GREEN.'),(2,'both','Beloved Citizens! The contract for transtube maintenance is up for tender today! Please monitor the situation and decide on the best service group to maintain the tubes. Contract is clearance ORANGE.'),(3,'both','Alert! RD Project ##SUB## has gone missing from location databanks. Find and recover immediately. ##SUB## is classified INDIGO.'),(4,'both','Citizens! ##CIT-V-TR## is due for erasure later today. Make sure their name is purged from all databanks before their public erasure.'),(5,'both','Citizens! RD Project ##SUB## has been escalated. Please decide on a plan of action. ##SUB## is classified GREEN.'),(6,'both','Citizens! Oversee the installation of RD Project ##SUB##. ##SUB## is clearance INDIGO.'),(7,'straight','Citizens! A Communist superweapon has been deployed around the Complex. Decide on course of action. This information is classified GREEN.'),(8,'both','Citizens! Compliance Index falling at unacceptable rate. Please identify cause and rectify. This information is classified YELLOW.'),(9,'both','ALERT! Major traffic disturbances detected in Transtube ##ZON##-##SUB##. Rectify situation and decide who is responsible for damages.'),(10,'straight','Alert! Information has surfaced that a major terrorist attack is to take place this daycycle. Prevent this attack at all costs. Information classified BLUE.'),(11,'both','Citizens! ##ZON## reactor ##SUB## is ready to go online. Please use this opportunity to raise morale across the sector.'),(12,'both','Alert! Enemy invasion force at edge of sector! Identify and eliminate immediately!'),(13,'classic','Alert! Large flooding occuring on subbasements 15-28 at location ##SUB##. Identify and fix source of flooding immediately.'),(14,'both','Alert! Hygiene in ##ZON## at record low levels! Identify areas for improvement and fix immediately! Information classified YELLOW.'),(15,'both','The popular Hero of our Complex ##CIT-B-1##-3 has been found guilty of communist sympathising. Organise their erasure without damaging morale.'),(16,'both','Alert! IntSec item 214-##SUB## is currently missing. Find and recover this item. 214-##SUB## is classified VIOLET.'),(17,'both','Alert! ##CIT-V-1## holding out against commie force in the MuchoMeat plant. Rescue and recover ##CIT-V-1## unharmed.'),(18,'both','Citizens! RD Project ##SUB## is complete and ready for deployment. Test ##SUB## with a suitable project. Projects must be approved by concesus.WANTON BROTH is classified INDIGO.'),(19,'both','Error. Code 1789. Additional processing power required. Please find space for and construct additional processing CompNodes to bring the total up from 8 to 20.'),(20,'both','Alert! Information has surfaced that a major terrorist attack is to take place this daycycle. Prevent this attack at all costs. Information classified BLUE.'),(21,'both','Assertion Error. Testing [DELETED FOR SECURITY REASONS]. Statement: Communism is good for citizens. Legalise Communism. This information classified ULTRAVIOLET.'),(22,'both','Alert! Citizen Vicky-B must me made to disappear this daycycle. Organise while keeping up her public appearances and career. This issue classified VIOLET.'),(23,'both','Citizens! A Communist superweapon has been deployed around the Complex. Decide on course of action. This information is classified GREEN.'),(24,'both','Assertion Error. Testing [DELETED FOR SECURITY REASONS]. Citizens! Organise a mock Communist attack to assess the sector\'s prepardness. Until the exercise is complete, situation is classified INDIGO.'),(25,'both','Alert! Citizen ##CIT-V## was detained by agents of Beta Complex on their last mission. Negotiate with ##CIT-U## of Beta Complex and return ##CIT-V## to us. Situation classified BLUE.'),(26,'both','Alert! Paperwork Quota down 84%! Hygiene down 29%! Analyze and remediate situation!'),(27,'both','Citizens! A Communist superweapon has been deployed around the Complex. Decide on course of action. This information is classified GREEN.'),(28,'both','Alert! Citizen Danny-V-SNH has gone missing and has information vital to the fight against Communism! Recover and interrogate! Situation classified BLUE!'),(29,'both','Alert! Testing [DELETED FOR SECURITY REASONS]! Sector is overpopulated by 12%. Balance the population by the end of the daycycle.');
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
INSERT INTO `crisis_tag` VALUES (1,'CID'),(1,'HIU'),(1,'ISD'),(1,'SID'),(2,'SID'),(3,'AFD'),(3,'CPD'),(3,'RDD'),(4,'AFU'),(4,'ISU'),(4,'SIU'),(5,'CPU'),(5,'ISU'),(5,'PLD'),(5,'RDU'),(6,'PSU'),(6,'RDU'),(7,'HID'),(7,'PSU'),(7,'SID'),(8,'CID'),(8,'PLU'),(9,'HID'),(9,'PLD'),(9,'TSD'),(10,'HIU'),(10,'HPU'),(10,'TSD'),(11,'PSU'),(12,'CIU'),(13,'CID'),(13,'SID'),(14,'HID'),(14,'TSU'),(15,'HPD'),(15,'ISD'),(15,'LID'),(16,'CID'),(16,'ISU'),(16,'LID'),(16,'SID'),(17,'CID'),(17,'HPD'),(17,'LID'),(18,'RDU'),(19,'CID'),(20,'LID'),(21,'LID'),(21,'SID'),(22,'LIU'),(23,'AFU'),(23,'RDU'),(24,'SIU'),(26,'LID'),(26,'SID'),(27,'AFU'),(27,'ISU'),(29,'CID'),(29,'HID'),(29,'HPD'),(29,'TSD');
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
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crisis_text`
--

LOCK TABLES `crisis_text` WRITE;
/*!40000 ALTER TABLE `crisis_text` DISABLE KEYS */;
INSERT INTO `crisis_text` VALUES (1,1,'CPU recently cut funding to termination stamping, leaving only one worker stamping all vouchers: ##CIT-Y-1##.'),(2,1,'A few daycycles ago, he lost his ME card in the pneumatic tubes, making him unable to leave the room or get help.'),(3,1,'Diligently, he kept stamping in the hope someone would come find him, he died from lack of water yesterdaycycle.'),(4,1,'No vouchers have been authorised in a day, meaning no authorised terminations have taken place.'),(5,2,'Due to a large string of timetable failures and deaths, the computer has decided on a competition: one day in which PS, TS, and HPD split the tracks and manage them.'),(6,2,'The transtubes are split equally between PS, TS, and HPD.'),(7,2,'Obviously there\'s not nearly enough time for the other two to manage their tubes properly, so they\'re planning on sabotaging the hell out of Power Services networks.'),(8,3,'##SUB## is a highly destructive weapon capable of vapourising 1/10th of the sector\'s population each time it is fired (their clothes and belongings are intact)'),(9,3,'##SUB## has a delay between firings (whatever is thematically appropriate)'),(10,3,'##SUB## has been stolen by a FCCC-P splinter cell, and threatens to destroy the sector unless their demands are met.'),(11,3,'The demands start large (Live coverage on the sector Vidscreens) and get even more elaborate (Promotion of all the sect to VIOLET).'),(12,3,'Remember to find the scapegoat for this. It was lost at a transfer between R&D and AF, overseen by CPU'),(13,4,'Bit of background. In the year 182, there was another ##CIT-V-TR## who got a bit too high up too fast, and so a clever high-programmer made that particular phrase summon Friend Computer Immediately.'),(14,4,'The summons also modifies friend computer\'s threat level, raising it by one each time its name is said. Each time FC hears ##CIT-V-TR##\'s name, it shows up with a suddenly hightened threat level and'),(15,4,'doesn\'t know why. ##CIT-V-TR## only reached VIOLET three days ago, and is already up for erasure. FC suspects something about the name, which is why it requests a full purge.'),(16,4,'Anagram: TTS- Treason Tracking Server'),(17,4,'##CIT-V-TR## took an extremely large dose of the drug RadicalMankey, which is a major mutagenic, and it will kick in whenever appropriate.'),(18,5,'##SUB## is a proposal to change the standard gene template of clones to photosynthesize.'),(19,5,'The proposal is led by ##CIT-G-SG##, an (unsurprisingly) Sierra Club member.'),(20,5,'##CIT-G-SG## has the go-ahead for a small trial run, and the tech actually works, however:'),(21,5,'--The green gene removes digestive systems, meaning no food can be sold to these clones.'),(22,5,'--Green citizens quickly grow fat and immobile, as the gene is designed for outdoor background radiation, not AC radiation levels.'),(23,5,'The Programmers also have to decide whether to standardise the technology'),(24,5,'--If they do, the programmers and the rest of the sector become registered mutants immediately.'),(25,5,'--If they don\'t, the programmers must find a scapegoat for why the efficency boost wasn\'t implemented.'),(28,7,'A supervolcano has erupted, covering the planet with a layer of ash clouds which constantly storm.'),(29,7,'The lightning is so loud and frequent it can be heard on multiple outer layers of the complex, causing panic.'),(30,7,'HPs must find a reasonable excuse for the sounds to stop the panic, while finding a scapegoat for the attack.'),(31,8,'The Petbot 214 was released today, and has sold out within minutes. They were designed with subliminal messaging propaganda tech.'),(32,8,'The subliminal tech was hijacked by some phreaks as a contract for Corpore Metal. The petbot encourages owners to get into dangerous, maiming situations.'),(33,9,'A load of PLC\'s Cold Fun syrup fell into one end of the transtube ##ZON##-##SUB## after a navigation error caused the truckbot to crash.'),(34,9,'Cold Fun syrup sits at around 1 Kelvin for thousands of years, and is usually mixed in micro quantities to make cold fun.'),(35,9,'The Cold Fun Syrup is causing the air to liquify and mix with the syrup, causing it to spread further down the transtubes.'),(36,9,'PLC is blaming TS for the truckbot crashing. TS is blaming CPU for not updating the maps. CPU is trying to cover its tracks.'),(37,9,'In the end, there is a 15 ACCESS damages fee, in addition to any changes from directives.'),(38,10,'This information is based of a piece of paper recovered from a terminated achivist, listing several dates.'),(39,10,'These were actually the dates of the completion of several major transtube works in the past.'),(40,10,'Due to the recycling of the year 214 and a set of coincidences, all the other dates have matched with major sewerage overflows.'),(41,10,'The final date on the list is this daycycle. IntSec is desperately looking for an attack, AF wants to embarrass them.'),(42,10,'Tech Services and HPD are preparing for a Massive FunBall game that\'s about to take place, and when half-time hits and everyone uses the restrooms...'),(43,10,'Specifically, the sewerage will overflow 10 minutes before the end of the match.'),(44,6,'##SUB## is a power project designed to balance power fluctuations. Works off wierd science.'),(45,6,'Whenever the draw is high, the project pushes more power into the system, exponentially. Above a certain load, the project supplies virtually limitless power.'),(46,6,'So much power, in fact, it plays havoc with electrical systems and bot brains in the sector.'),(47,6,'If the players shut off the other reactors, the draw becomes higher, and players may find it impossible to shut the project down.'),(48,11,'The reactor doesn\'t work, and the project manager ##CIT-I-1## knows it. They\'re planning on faking a meltdown to get minions sent down.'),(49,11,'If the supplies and help are sent, the reactor actually starts melting down.'),(50,12,'Due to a bug in a recent software patch, the compnode of ##ZON## no longer recognizes a neighbouring sector as existing.'),(51,12,'Thusly, the computer sees all six million inhabitants plus bots as invaders, and views the sector as being outside.'),(52,12,'In a couple hours time, the software is reverted and the computer has no memory of seeing the sector as non-existant.'),(53,12,'PLC doesn\'t actually have enough laser barrels for the troops.'),(54,13,'A servants of cthulu sect has successfully managed to build a temple on sublevel 12, and finished a major ceremony. A gate has opened and is flooding water out to the lower levels.'),(55,13,'No gate has actually opened, it\'s just a burst water main, but the cultists are too feverish from hallucinogens in the water to tell the difference.'),(56,13,'Minions will report horrible creatures bursting from the water where none exist due to exposure to the hallucinogens.'),(57,14,'One of the major hygiene index factors is reports from core sample tests. These tests measure the depth of dirt and grime, usually around a few micrometres.'),(58,14,'A new dirt indexer bot has been deployed across the sector, which moves randomly around and measure the dirt level of surfaces.'),(59,14,'One wandered accidently into a frankenstein destroyers meeting, and was mostly destroyed. However, it kept measuring the distance to the tip of its drill bit, now several hundred metres away.'),(60,14,'This measurement is submitted every few minutes to the index, throwing the measurements right out.'),(61,15,'##CIT-B-1## was framed by ##CIT-I-1##, who ##CIT-B-1## had blackmail on. While he wasn\'t a commie, he was a high-ranking PURGEr and humanist.'),(62,15,'##CIT-B-1## is on their third clone, and has just completed their 4th tour of duty (20 years as a vulture trooper). He survives through his regeration mutation.'),(63,16,'214-##SUB## is an original copy of Das Kapital. It was recently raided from another high programmer\'s private residence. Of course, they\'re not willing to admit they had it.'),(64,16,'Anyone not of VIOLET clearance who touches or sees 214-##SUB## will be considered tainted by commie propaganda by the Big C.'),(65,16,'The other high programmer should message one or two of the players before game start, and promise favours if the item is returned.'),(66,16,'The book is currently in the middle of a busy INFRARED area somewhere. All INFRAREDs who have seen it will need purging/brainscrubbing.'),(67,17,'A blackout caused a group of INFRAREDs running for fitness purposes to accidently enter the MuchoMeat food growing floors, knocking black dirt all over the floor.'),(68,17,'When the light came back on, they saw the floor as INFRARED but all the doors as BLUE and above, so kept exercising till help came.'),(69,17,'##CIT-V-1## was in his office drinking when he heard destruction and shouts from outside, so immediately called on FC.'),(70,18,'##SUB## is a teleportation device, which links two points in space together.'),(71,18,'##SUB## can only be fired twice before being destroyed. It will take many monthcycles to create a new one...without HP resources, that is.'),(72,19,'This is part 1 of the TAINTED MOLLUSC storyline.'),(73,19,'Friend Computer is running a program that re-evalutes its basic assumptions. That is, what it defines as happiness.'),(74,20,'Three terrorist attacks are actually due to take place today. One PURGE at ##LOC-1##, one Humanist at ##LOC-2##, and Commies at ##LOC-3##.'),(75,20,'They are masterminded by a HP in a nearby sector, either as a test or revenge.'),(76,21,'This is part 2 of the TAINTED MOLLUSC storyline, testing Loyalty. The entire crisis is a trap.'),(77,21,'Anyone who agrees to legalise Communism is considered a traitor and will be executed without a damn good reason.'),(78,21,'In the meantime, everyone is conflicting over \"hatred day\", a day of hatred against the commies.'),(79,21,'The parade has a marching band, and several floats, including ones of a Computer, Vulture Craft, and dead Commie.'),(80,22,'Vicky-B is a HOOC Vulture Trooper. Friend computer wants her to disappear to take control of her dummybots freely.'),(81,22,'Vicky-B\'s dummybots have already been upgraded and gone missing.'),(82,22,'Vicky-B has a public appearance later this daycycle, and must be present to preserve happiness.'),(83,23,'Due to large amounts of recent rain, outside is currently flooding about 3m high around the complex.'),(84,23,'Recent chemical attacks by AF with RD weapon ##SUB## has entered the water, and water from the outdoors is toxic.'),(85,23,'There are no breaks in the wall *yet*, but there will be soon enough.'),(86,24,'This is part 3 of the TAINTED MOLLUSC storyline, testing Security. FC is testing whether actual terrorist activity drops the security index.'),(87,25,'##CIT-U## is from beta complex, and wants something valuable/difficult to obtain from the HPs.'),(88,26,'An experimental paper-eating bacteria RDP ##SUB## escaped containment at R&D firm Recycling Ruckus.'),(89,26,'All the paper not stored in the archives has been eaten and converted to a fine paste, including toilet paper.'),(90,26,'Friend Computer has ordered beefed up security at the archives, no-one goes in or out without express HP permission to prevent contamination.'),(91,26,'CPU, PLC, and R&D are all trying to implement their own catch-all way of storing records, and will be fighting like crazy.'),(92,27,'There is actually a war with Beta Complex, this time. The superweapon is a squadron of warbots.'),(93,27,'The other sectors don\'t really care about the warbots, as they\'re not actually firing on the complex yet.'),(94,29,'This is part 3 of the TAINTED MOLLUSC storyline, testing Compliance. Specifically, how well citizens comply en-masse when ordered into strenuous circumstances.'),(95,29,'The sector is actually overpopulated, but only in the INFRARED bunks, which is overpopulated 18%.'),(96,27,'The warbots are equipped with new shielding technology along with the usual assortment of weaponry, and will only retaliate if fired upon.'),(97,28,'Danny-V was broken out of prison after returning to the complex, but before his debriefing interrogation.'),(98,28,'While several of the SGs want him back, many of the societies want him dead.'),(99,28,'He may even have blackmail on a couple of the high programmers, and should be used as such.'),(100,28,'He was broken out by a HP in a nearby sector that has been conspiring with Beta complex. The HP\'s minion ##CIT-V-1## was the culprit.'),(101,29,'In order to find the space, something has to give. Specifically, two of the service groups will have to give up land, or build a *huge* outdoor extension for the 1 million oversubscribed INFRAREDs.');
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
-- Dumping data for table `first_name`
--

LOCK TABLES `first_name` WRITE;
/*!40000 ALTER TABLE `first_name` DISABLE KEYS */;
INSERT INTO `first_name` VALUES (77,'AARON'),(2260,'ABBIE'),(1913,'ABBY'),(831,'ABDUL'),(854,'ABE'),(485,'ABEL'),(1739,'ABIGAIL'),(347,'ABRAHAM'),(1053,'ABRAM'),(1529,'ADA'),(1040,'ADALBERTO'),(69,'ADAM'),(752,'ADAN'),(1732,'ADDIE'),(1942,'ADELA'),(2239,'ADELAIDE'),(1743,'ADELE'),(2367,'ADELINA'),(1853,'ADELINE'),(567,'ADOLFO'),(642,'ADOLPH'),(227,'ADRIAN'),(1736,'ADRIANA'),(2288,'ADRIANNE'),(1613,'ADRIENNE'),(2349,'AGATHA'),(1436,'AGNES'),(563,'AGUSTIN'),(885,'AHMAD'),(1000,'AHMED'),(1778,'AIDA'),(1904,'AILEEN'),(1722,'AIMEE'),(2138,'AISHA'),(435,'AL'),(91,'ALAN'),(2013,'ALANA'),(2198,'ALBA'),(54,'ALBERT'),(1551,'ALBERTA'),(270,'ALBERTO'),(889,'ALDEN'),(849,'ALDO'),(930,'ALEC'),(2366,'ALECIA'),(2016,'ALEJANDRA'),(305,'ALEJANDRO'),(2379,'ALENE'),(156,'ALEX'),(141,'ALEXANDER'),(1615,'ALEXANDRA'),(1966,'ALEXANDRIA'),(641,'ALEXIS'),(331,'ALFONSO'),(911,'ALFONZO'),(125,'ALFRED'),(2132,'ALFREDA'),(269,'ALFREDO'),(614,'ALI'),(1270,'ALICE'),(1371,'ALICIA'),(2064,'ALINE'),(1822,'ALISA'),(1721,'ALISHA'),(1566,'ALISON'),(2085,'ALISSA'),(250,'ALLAN'),(114,'ALLEN'),(2269,'ALLENE'),(1983,'ALLIE'),(1447,'ALLISON'),(1994,'ALLYSON'),(1415,'ALMA'),(1219,'ALONSO'),(451,'ALONZO'),(824,'ALPHONSE'),(654,'ALPHONSO'),(1838,'ALTA'),(2009,'ALTHEA'),(361,'ALTON'),(793,'ALVA'),(619,'ALVARO'),(169,'ALVIN'),(2386,'ALVINA'),(2096,'ALYCE'),(2200,'ALYSON'),(1667,'ALYSSA'),(965,'AMADO'),(2124,'AMALIA'),(1259,'AMANDA'),(1358,'AMBER'),(1096,'AMBROSE'),(1550,'AMELIA'),(2008,'AMIE'),(470,'AMOS'),(2135,'AMPARO'),(1251,'AMY'),(1400,'ANA'),(2126,'ANASTASIA'),(814,'ANDERSON'),(217,'ANDRE'),(900,'ANDREA'),(1110,'ANDREAS'),(352,'ANDRES'),(35,'ANDREW'),(284,'ANDY'),(205,'ANGEL'),(1248,'ANGELA'),(1877,'ANGELIA'),(1609,'ANGELICA'),(1633,'ANGELINA'),(1869,'ANGELINE'),(2184,'ANGELIQUE'),(2162,'ANGELITA'),(326,'ANGELO'),(1546,'ANGIE'),(959,'ANIBAL'),(1355,'ANITA'),(1267,'ANN'),(1252,'ANNA'),(2028,'ANNABELLE'),(1304,'ANNE'),(2422,'ANNETTA'),(1394,'ANNETTE'),(1316,'ANNIE'),(2165,'ANNMARIE'),(22,'ANTHONY'),(1155,'ANTIONE'),(2236,'ANTIONETTE'),(536,'ANTOINE'),(1580,'ANTOINETTE'),(591,'ANTON'),(990,'ANTONE'),(1069,'ANTONIA'),(100,'ANTONIO'),(791,'ANTONY'),(888,'ANTWAN'),(1361,'APRIL'),(2100,'ARACELI'),(362,'ARCHIE'),(1080,'ARDEN'),(830,'ARIEL'),(2294,'ARLEEN'),(1134,'ARLEN'),(1444,'ARLENE'),(1189,'ARLIE'),(2111,'ARLINE'),(618,'ARMAND'),(258,'ARMANDO'),(224,'ARNOLD'),(1181,'ARNOLDO'),(843,'ARNULFO'),(864,'ARON'),(790,'ARRON'),(710,'ART'),(48,'ARTHUR'),(304,'ARTURO'),(1133,'ASA'),(1995,'ASHLEE'),(2265,'ASHLEIGH'),(584,'ASHLEY'),(2354,'ATHENA'),(495,'AUBREY'),(1925,'AUDRA'),(1392,'AUDREY'),(551,'AUGUST'),(2119,'AUGUSTA'),(806,'AUGUSTINE'),(948,'AUGUSTUS'),(2117,'AURELIA'),(736,'AURELIO'),(1753,'AURORA'),(301,'AUSTIN'),(1879,'AUTUMN'),(1961,'AVA'),(638,'AVERY'),(1982,'AVIS'),(2407,'BARB'),(1223,'BARBARA'),(1935,'BARBRA'),(705,'BARNEY'),(1044,'BARRETT'),(140,'BARRY'),(574,'BART'),(877,'BARTON'),(668,'BASIL'),(1389,'BEATRICE'),(1864,'BEATRIZ'),(712,'BEAU'),(1506,'BECKY'),(1524,'BELINDA'),(2421,'BELLA'),(2283,'BELLE'),(212,'BEN'),(1124,'BENEDICT'),(1993,'BENITA'),(556,'BENITO'),(66,'BENJAMIN'),(785,'BENNETT'),(371,'BENNIE'),(382,'BENNY'),(1175,'BENTON'),(1619,'BERNADETTE'),(1944,'BERNADINE'),(142,'BERNARD'),(664,'BERNARDO'),(1391,'BERNICE'),(757,'BERNIE'),(929,'BERRY'),(453,'BERT'),(2105,'BERTA'),(1375,'BERTHA'),(2197,'BERTIE'),(1028,'BERTRAM'),(2167,'BERYL'),(2329,'BESS'),(1440,'BESSIE'),(1418,'BETH'),(1617,'BETHANY'),(1649,'BETSY'),(1826,'BETTE'),(1776,'BETTIE'),(1233,'BETTY'),(1931,'BETTYE'),(1579,'BEULAH'),(2214,'BEVERLEY'),(1292,'BEVERLY'),(1929,'BIANCA'),(159,'BILL'),(522,'BILLIE'),(73,'BILLY'),(2302,'BIRDIE'),(559,'BLAINE'),(748,'BLAIR'),(342,'BLAKE'),(1603,'BLANCA'),(1540,'BLANCHE'),(1043,'BO'),(266,'BOB'),(1898,'BOBBI'),(660,'BOBBIE'),(83,'BOBBY'),(1728,'BONITA'),(1307,'BONNIE'),(770,'BOOKER'),(860,'BORIS'),(1174,'BOYCE'),(487,'BOYD'),(219,'BRAD'),(467,'BRADFORD'),(128,'BRADLEY'),(993,'BRADLY'),(571,'BRADY'),(602,'BRAIN'),(751,'BRANDEN'),(1539,'BRANDI'),(2208,'BRANDIE'),(68,'BRANDON'),(1495,'BRANDY'),(914,'BRANT'),(2436,'BREANNA'),(1250,'BRENDA'),(482,'BRENDAN'),(1008,'BRENDON'),(195,'BRENT'),(920,'BRENTON'),(507,'BRET'),(204,'BRETT'),(20,'BRIAN'),(2099,'BRIANA'),(1957,'BRIANNA'),(2325,'BRIANNE'),(973,'BRICE'),(1588,'BRIDGET'),(2161,'BRIDGETT'),(1939,'BRIDGETTE'),(2199,'BRIGITTE'),(2186,'BRITNEY'),(1123,'BRITT'),(1406,'BRITTANY'),(1766,'BRITTNEY'),(701,'BROCK'),(1218,'BRODERICK'),(1616,'BROOKE'),(829,'BROOKS'),(67,'BRUCE'),(697,'BRUNO'),(102,'BRYAN'),(409,'BRYANT'),(535,'BRYCE'),(617,'BRYON'),(1205,'BUCK'),(951,'BUD'),(586,'BUDDY'),(755,'BUFORD'),(996,'BURL'),(881,'BURT'),(570,'BURTON'),(1068,'BUSTER'),(275,'BYRON'),(1779,'CAITLIN'),(440,'CALEB'),(1897,'CALLIE'),(155,'CALVIN'),(336,'CAMERON'),(2259,'CAMILLA'),(1707,'CAMILLE'),(1562,'CANDACE'),(1581,'CANDICE'),(2385,'CANDIDA'),(1794,'CANDY'),(1742,'CARA'),(2342,'CAREN'),(598,'CAREY'),(2300,'CARI'),(2172,'CARISSA'),(47,'CARL'),(1421,'CARLA'),(2032,'CARLENE'),(732,'CARLO'),(81,'CARLOS'),(334,'CARLTON'),(2093,'CARLY'),(2438,'CARMEL'),(1859,'CARMELA'),(2023,'CARMELLA'),(704,'CARMELO'),(651,'CARMEN'),(805,'CARMINE'),(919,'CAROL'),(1494,'CAROLE'),(1814,'CAROLINA'),(1467,'CAROLINE'),(1261,'CAROLYN'),(1348,'CARRIE'),(1085,'CARROL'),(408,'CARROLL'),(774,'CARSON'),(741,'CARTER'),(492,'CARY'),(2273,'CARYN'),(2160,'CASANDRA'),(268,'CASEY'),(1488,'CASSANDRA'),(1785,'CASSIE'),(1960,'CATALINA'),(1265,'CATHERINE'),(1876,'CATHLEEN'),(2406,'CATHRINE'),(2158,'CATHRYN'),(1382,'CATHY'),(2315,'CATRINA'),(1664,'CECELIA'),(214,'CECIL'),(1829,'CECILE'),(1537,'CECILIA'),(390,'CEDRIC'),(1188,'CEDRICK'),(1740,'CELESTE'),(2341,'CELESTINE'),(1590,'CELIA'),(2219,'CELINA'),(353,'CESAR'),(121,'CHAD'),(809,'CHADWICK'),(842,'CHANCE'),(1975,'CHANDRA'),(943,'CHANG'),(2345,'CHANTEL'),(1862,'CHARITY'),(2430,'CHARLA'),(1439,'CHARLENE'),(8,'CHARLES'),(727,'CHARLEY'),(197,'CHARLIE'),(1349,'CHARLOTTE'),(2049,'CHARMAINE'),(1076,'CHAS'),(542,'CHASE'),(2091,'CHASITY'),(942,'CHAUNCEY'),(1626,'CHELSEA'),(2235,'CHELSEY'),(1737,'CHERI'),(1800,'CHERIE'),(2141,'CHERRY'),(1278,'CHERYL'),(213,'CHESTER'),(1027,'CHET'),(989,'CHI'),(2356,'CHLOE'),(1058,'CHONG'),(96,'CHRIS'),(1783,'CHRISTA'),(2428,'CHRISTAL'),(2353,'CHRISTEN'),(1885,'CHRISTI'),(236,'CHRISTIAN'),(1650,'CHRISTIE'),(2378,'CHRISTIN'),(1289,'CHRISTINA'),(1262,'CHRISTINE'),(910,'CHRISTOPER'),(11,'CHRISTOPHER'),(1479,'CHRISTY'),(2059,'CHRYSTAL'),(547,'CHUCK'),(1039,'CHUNG'),(2328,'CINDI'),(1332,'CINDY'),(754,'CLAIR'),(1519,'CLAIRE'),(1363,'CLARA'),(2015,'CLARE'),(93,'CLARENCE'),(1866,'CLARICE'),(1963,'CLARISSA'),(406,'CLARK'),(1090,'CLAUD'),(230,'CLAUDE'),(1875,'CLAUDETTE'),(1453,'CLAUDIA'),(2083,'CLAUDINE'),(925,'CLAUDIO'),(464,'CLAY'),(253,'CLAYTON'),(692,'CLEMENT'),(1079,'CLEMENTE'),(731,'CLEO'),(1018,'CLETUS'),(545,'CLEVELAND'),(612,'CLIFF'),(149,'CLIFFORD'),(279,'CLIFTON'),(426,'CLINT'),(239,'CLINTON'),(187,'CLYDE'),(246,'CODY'),(703,'COLBY'),(682,'COLE'),(2136,'COLEEN'),(891,'COLEMAN'),(2069,'COLETTE'),(378,'COLIN'),(1446,'COLLEEN'),(739,'COLLIN'),(1160,'COLTON'),(1026,'COLUMBUS'),(1967,'CONCEPCION'),(2196,'CONCETTA'),(967,'CONNIE'),(456,'CONRAD'),(1451,'CONSTANCE'),(1796,'CONSUELO'),(1527,'CORA'),(1116,'CORDELL'),(182,'COREY'),(2131,'CORINA'),(2055,'CORINE'),(1773,'CORINNE'),(1928,'CORNELIA'),(461,'CORNELIUS'),(787,'CORNELL'),(1965,'CORRINE'),(1154,'CORTEZ'),(2227,'CORTNEY'),(229,'CORY'),(488,'COURTNEY'),(693,'COY'),(90,'CRAIG'),(1656,'CRISTINA'),(1095,'CRISTOBAL'),(1153,'CRISTOPHER'),(724,'CRUZ'),(1321,'CRYSTAL'),(561,'CURT'),(112,'CURTIS'),(1247,'CYNTHIA'),(797,'CYRIL'),(796,'CYRUS'),(1514,'DAISY'),(109,'DALE'),(2420,'DALIA'),(424,'DALLAS'),(765,'DALTON'),(548,'DAMIAN'),(578,'DAMIEN'),(988,'DAMION'),(357,'DAMON'),(179,'DAN'),(314,'DANA'),(621,'DANE'),(2321,'DANETTE'),(747,'DANIAL'),(12,'DANIEL'),(2365,'DANIELA'),(1369,'DANIELLE'),(1089,'DANILO'),(2327,'DANNA'),(956,'DANNIE'),(101,'DANNY'),(691,'DANTE'),(1837,'DAPHNE'),(2374,'DARA'),(2021,'DARCY'),(1217,'DARELL'),(777,'DAREN'),(469,'DARIN'),(976,'DARIO'),(606,'DARIUS'),(1690,'DARLA'),(1376,'DARLENE'),(476,'DARNELL'),(1057,'DARON'),(447,'DARREL'),(165,'DARRELL'),(243,'DARREN'),(1012,'DARRICK'),(480,'DARRIN'),(1067,'DARRON'),(232,'DARRYL'),(583,'DARWIN'),(281,'DARYL'),(271,'DAVE'),(6,'DAVID'),(684,'DAVIS'),(1324,'DAWN'),(2272,'DAYNA'),(173,'DEAN'),(1903,'DEANA'),(896,'DEANDRE'),(1144,'DEANGELO'),(2207,'DEANN'),(1480,'DEANNA'),(2061,'DEANNE'),(1360,'DEBBIE'),(2311,'DEBBY'),(1799,'DEBORA'),(1244,'DEBORAH'),(1258,'DEBRA'),(978,'DEE'),(2116,'DEENA'),(2231,'DEIDRA'),(2090,'DEIDRE'),(2097,'DEIRDRE'),(1122,'DEL'),(377,'DELBERT'),(1681,'DELIA'),(2314,'DELILAH'),(1595,'DELLA'),(2290,'DELMA'),(717,'DELMAR'),(975,'DELMER'),(2400,'DELOIS'),(1441,'DELORES'),(1734,'DELORIS'),(2324,'DELPHINE'),(1017,'DEMARCUS'),(2289,'DEMETRIA'),(527,'DEMETRIUS'),(1790,'DENA'),(2223,'DENICE'),(577,'DENIS'),(1293,'DENISE'),(40,'DENNIS'),(683,'DENNY'),(740,'DENVER'),(909,'DEON'),(163,'DEREK'),(647,'DERICK'),(178,'DERRICK'),(1084,'DESHAWN'),(1644,'DESIREE'),(581,'DESMOND'),(2243,'DESSIE'),(2293,'DESTINY'),(395,'DEVIN'),(566,'DEVON'),(502,'DEWAYNE'),(434,'DEWEY'),(918,'DEWITT'),(466,'DEXTER'),(1315,'DIANA'),(1269,'DIANE'),(2171,'DIANN'),(1637,'DIANNA'),(1498,'DIANNE'),(721,'DICK'),(640,'DIEGO'),(964,'DILLON'),(1802,'DINA'),(2364,'DINAH'),(893,'DINO'),(690,'DION'),(2164,'DIONNE'),(750,'DIRK'),(1700,'DIXIE'),(2044,'DOLLIE'),(1911,'DOLLY'),(1390,'DOLORES'),(1066,'DOMENIC'),(493,'DOMINGO'),(374,'DOMINIC'),(486,'DOMINICK'),(768,'DOMINIQUE'),(135,'DON'),(1971,'DONA'),(15,'DONALD'),(1065,'DONG'),(933,'DONN'),(1236,'DONNA'),(681,'DONNELL'),(315,'DONNIE'),(709,'DONNY'),(667,'DONOVAN'),(1033,'DONTE'),(1468,'DORA'),(1641,'DOREEN'),(2229,'DORETHA'),(870,'DORIAN'),(1274,'DORIS'),(1806,'DOROTHEA'),(1229,'DOROTHY'),(2419,'DORRIS'),(1216,'DORSEY'),(1746,'DORTHY'),(2281,'DOTTIE'),(323,'DOUG'),(45,'DOUGLAS'),(1115,'DOUGLASS'),(446,'DOYLE'),(431,'DREW'),(215,'DUANE'),(700,'DUDLEY'),(803,'DUNCAN'),(176,'DUSTIN'),(832,'DUSTY'),(1032,'DWAIN'),(256,'DWAYNE'),(257,'DWIGHT'),(546,'DYLAN'),(98,'EARL'),(853,'EARLE'),(2030,'EARLENE'),(1959,'EARLINE'),(380,'EARNEST'),(2022,'EARNESTINE'),(1711,'EBONY'),(373,'ED'),(136,'EDDIE'),(676,'EDDY'),(209,'EDGAR'),(928,'EDGARDO'),(1083,'EDISON'),(1336,'EDITH'),(500,'EDMOND'),(387,'EDMUND'),(1198,'EDMUNDO'),(1328,'EDNA'),(296,'EDUARDO'),(19,'EDWARD'),(769,'EDWARDO'),(134,'EDWIN'),(2040,'EDWINA'),(2264,'EDYTHE'),(1748,'EFFIE'),(521,'EFRAIN'),(887,'EFREN'),(1424,'EILEEN'),(1346,'ELAINE'),(2203,'ELBA'),(454,'ELBERT'),(2254,'ELDA'),(1215,'ELDEN'),(529,'ELDON'),(1152,'ELDRIDGE'),(1367,'ELEANOR'),(2299,'ELEANORE'),(1631,'ELENA'),(534,'ELI'),(2310,'ELIA'),(452,'ELIAS'),(2287,'ELIDA'),(491,'ELIJAH'),(2017,'ELINOR'),(1710,'ELISA'),(1777,'ELISABETH'),(1828,'ELISE'),(884,'ELISEO'),(1204,'ELISHA'),(2431,'ELISSA'),(1937,'ELIZA'),(1224,'ELIZABETH'),(1429,'ELLA'),(1345,'ELLEN'),(2417,'ELLIE'),(582,'ELLIOT'),(572,'ELLIOTT'),(423,'ELLIS'),(1118,'ELLSWORTH'),(1888,'ELMA'),(218,'ELMER'),(863,'ELMO'),(1974,'ELNORA'),(2340,'ELOISA'),(1661,'ELOISE'),(869,'ELOY'),(1078,'ELROY'),(1696,'ELSA'),(1417,'ELSIE'),(544,'ELTON'),(1772,'ELVA'),(2095,'ELVIA'),(595,'ELVIN'),(1679,'ELVIRA'),(663,'ELVIS'),(604,'ELWOOD'),(498,'EMANUEL'),(760,'EMERSON'),(686,'EMERY'),(501,'EMIL'),(862,'EMILE'),(1992,'EMILIA'),(2258,'EMILIE'),(490,'EMILIO'),(1318,'EMILY'),(1353,'EMMA'),(512,'EMMANUEL'),(496,'EMMETT'),(1056,'EMMITT'),(743,'EMORY'),(2149,'ENID'),(1007,'ENOCH'),(298,'ENRIQUE'),(1101,'ERASMO'),(33,'ERIC'),(1388,'ERICA'),(808,'ERICH'),(443,'ERICK'),(1972,'ERICKA'),(231,'ERIK'),(1517,'ERIKA'),(802,'ERIN'),(2404,'ERLINDA'),(1602,'ERMA'),(2148,'ERNA'),(86,'ERNEST'),(2286,'ERNESTINA'),(1628,'ERNESTINE'),(335,'ERNESTO'),(560,'ERNIE'),(764,'ERROL'),(433,'ERVIN'),(555,'ERWIN'),(1998,'ESMERALDA'),(1809,'ESPERANZA'),(1693,'ESSIE'),(590,'ESTEBAN'),(1955,'ESTELA'),(1760,'ESTELLA'),(1636,'ESTELLE'),(1839,'ESTER'),(1351,'ESTHER'),(528,'ETHAN'),(1344,'ETHEL'),(1755,'ETTA'),(80,'EUGENE'),(1782,'EUGENIA'),(838,'EUGENIO'),(1657,'EULA'),(1545,'EUNICE'),(1064,'EUSEBIO'),(1359,'EVA'),(312,'EVAN'),(2145,'EVANGELINA'),(2068,'EVANGELINE'),(2000,'EVE'),(1276,'EVELYN'),(261,'EVERETT'),(866,'EVERETTE'),(1075,'EZEKIEL'),(1100,'EZEQUIEL'),(847,'EZRA'),(630,'FABIAN'),(1703,'FAITH'),(1563,'FANNIE'),(2193,'FANNY'),(2297,'FATIMA'),(924,'FAUSTINO'),(1132,'FAUSTO'),(1804,'FAY'),(1528,'FAYE'),(678,'FEDERICO'),(2211,'FELECIA'),(1502,'FELICIA'),(370,'FELIPE'),(259,'FELIX'),(1099,'FELTON'),(845,'FERDINAND'),(1038,'FERMIN'),(1771,'FERN'),(238,'FERNANDO'),(610,'FIDEL'),(1143,'FILIBERTO'),(762,'FLETCHER'),(1572,'FLORA'),(1326,'FLORENCE'),(1011,'FLORENCIO'),(1077,'FLORENTINO'),(2189,'FLORINE'),(1945,'FLOSSIE'),(167,'FLOYD'),(817,'FOREST'),(401,'FORREST'),(923,'FOSTER'),(2084,'FRAN'),(962,'FRANCES'),(2185,'FRANCESCA'),(1138,'FRANCESCO'),(1733,'FRANCINE'),(127,'FRANCIS'),(1761,'FRANCISCA'),(145,'FRANCISCO'),(31,'FRANK'),(444,'FRANKIE'),(216,'FRANKLIN'),(1197,'FRANKLYN'),(71,'FRED'),(1651,'FREDA'),(299,'FREDDIE'),(533,'FREDDY'),(720,'FREDERIC'),(131,'FREDERICK'),(992,'FREDRIC'),(303,'FREDRICK'),(801,'FREEMAN'),(2154,'FREIDA'),(1870,'FRIEDA'),(905,'FRITZ'),(220,'GABRIEL'),(1847,'GABRIELA'),(1878,'GABRIELLE'),(868,'GAIL'),(784,'GALE'),(714,'GALEN'),(1049,'GARFIELD'),(518,'GARLAND'),(1131,'GARRET'),(388,'GARRETT'),(367,'GARRY'),(795,'GARTH'),(26,'GARY'),(1142,'GASTON'),(685,'GAVIN'),(2014,'GAY'),(2246,'GAYLA'),(1130,'GAYLE'),(1141,'GAYLORD'),(2098,'GENA'),(783,'GENARO'),(200,'GENE'),(1522,'GENEVA'),(1552,'GENEVIEVE'),(368,'GEOFFREY'),(16,'GEORGE'),(2082,'GEORGETTE'),(1450,'GEORGIA'),(1936,'GEORGINA'),(58,'GERALD'),(1380,'GERALDINE'),(1024,'GERALDO'),(322,'GERARD'),(372,'GERARDO'),(2268,'GERI'),(2224,'GERMAINE'),(756,'GERMAN'),(649,'GERRY'),(1426,'GERTRUDE'),(883,'GIL'),(199,'GILBERT'),(425,'GILBERTO'),(2177,'GILDA'),(1432,'GINA'),(1647,'GINGER'),(927,'GINO'),(794,'GIOVANNI'),(2411,'GISELA'),(1016,'GIUSEPPE'),(1322,'GLADYS'),(188,'GLEN'),(1461,'GLENDA'),(117,'GLENN'),(1846,'GLENNA'),(1275,'GLORIA'),(1784,'GOLDIE'),(646,'GONZALO'),(172,'GORDON'),(1333,'GRACE'),(1857,'GRACIE'),(1841,'GRACIELA'),(459,'GRADY'),(633,'GRAHAM'),(1196,'GRAIG'),(343,'GRANT'),(1146,'GRANVILLE'),(174,'GREG'),(392,'GREGG'),(585,'GREGORIO'),(37,'GREGORY'),(1947,'GRETA'),(1663,'GRETCHEN'),(2285,'GRISELDA'),(543,'GROVER'),(405,'GUADALUPE'),(2339,'GUILLERMINA'),(379,'GUILLERMO'),(637,'GUS'),(2257,'GUSSIE'),(418,'GUSTAVO'),(252,'GUY'),(1670,'GWEN'),(1483,'GWENDOLYN'),(1214,'HAI'),(601,'HAL'),(1941,'HALEY'),(2147,'HALLIE'),(983,'HANK'),(2331,'HANNA'),(1586,'HANNAH'),(557,'HANS'),(580,'HARLAN'),(1180,'HARLAND'),(526,'HARLEY'),(44,'HAROLD'),(1535,'HARRIET'),(1988,'HARRIETT'),(730,'HARRIS'),(568,'HARRISON'),(70,'HARRY'),(225,'HARVEY'),(1015,'HASSAN'),(1534,'HATTIE'),(1179,'HAYDEN'),(1074,'HAYWOOD'),(1357,'HAZEL'),(524,'HEATH'),(1272,'HEATHER'),(189,'HECTOR'),(1460,'HEIDI'),(1234,'HELEN'),(1813,'HELENA'),(1770,'HELENE'),(2176,'HELGA'),(1666,'HENRIETTA'),(46,'HENRY'),(1109,'HERB'),(130,'HERBERT'),(680,'HERIBERTO'),(183,'HERMAN'),(2217,'HERMINIA'),(792,'HERSCHEL'),(876,'HERSHEL'),(2157,'HESTER'),(950,'HILARIO'),(1954,'HILARY'),(1482,'HILDA'),(2007,'HILLARY'),(1031,'HILTON'),(1037,'HIPOLITO'),(666,'HIRAM'),(1187,'HOBERT'),(2181,'HOLLIE'),(707,'HOLLIS'),(1405,'HOLLY'),(321,'HOMER'),(1126,'HONG'),(1646,'HOPE'),(344,'HORACE'),(972,'HORACIO'),(2317,'HORTENCIA'),(1163,'HOSEA'),(779,'HOUSTON'),(79,'HOWARD'),(1073,'HOYT'),(325,'HUBERT'),(1030,'HUEY'),(254,'HUGH'),(438,'HUGO'),(511,'HUMBERTO'),(789,'HUNG'),(735,'HUNTER'),(1098,'HYMAN'),(263,'IAN'),(1402,'IDA'),(439,'IGNACIO'),(1105,'IKE'),(1946,'ILA'),(2051,'ILENE'),(2107,'IMELDA'),(1842,'IMOGENE'),(1775,'INA'),(2399,'INDIA'),(2195,'INES'),(1547,'INEZ'),(1780,'INGRID'),(2280,'IOLA'),(2330,'IONA'),(2410,'IONE'),(351,'IRA'),(1295,'IRENE'),(1544,'IRIS'),(1474,'IRMA'),(473,'IRVIN'),(407,'IRVING'),(723,'IRWIN'),(277,'ISAAC'),(1532,'ISABEL'),(2409,'ISABELL'),(2216,'ISABELLA'),(1830,'ISABELLE'),(635,'ISAIAH'),(1036,'ISAIAS'),(861,'ISIAH'),(715,'ISIDRO'),(429,'ISMAEL'),(399,'ISRAEL'),(1129,'ISREAL'),(698,'ISSAC'),(1781,'IVA'),(272,'IVAN'),(2352,'IVETTE'),(899,'IVORY'),(1916,'IVY'),(1195,'JACINTO'),(53,'JACK'),(306,'JACKIE'),(2153,'JACKLYN'),(615,'JACKSON'),(1856,'JACLYN'),(122,'JACOB'),(1305,'JACQUELINE'),(1601,'JACQUELYN'),(763,'JACQUES'),(2276,'JACQULINE'),(2222,'JADE'),(1140,'JAE'),(267,'JAIME'),(419,'JAKE'),(917,'JAMAAL'),(565,'JAMAL'),(859,'JAMAR'),(782,'JAME'),(822,'JAMEL'),(1,'JAMES'),(1042,'JAMEY'),(1973,'JAMI'),(233,'JAMIE'),(1010,'JAMISON'),(489,'JAN'),(1668,'JANA'),(1296,'JANE'),(2355,'JANEL'),(2194,'JANELL'),(1767,'JANELLE'),(1264,'JANET'),(1792,'JANETTE'),(1285,'JANICE'),(1555,'JANIE'),(1819,'JANINE'),(1648,'JANIS'),(2114,'JANNA'),(2163,'JANNIE'),(2292,'JAQUELINE'),(226,'JARED'),(1108,'JAROD'),(932,'JARRED'),(828,'JARRETT'),(575,'JARROD'),(696,'JARVIS'),(2251,'JASMIN'),(1623,'JASMINE'),(24,'JASON'),(553,'JASPER'),(237,'JAVIER'),(152,'JAY'),(2437,'JAYME'),(1855,'JAYNE'),(689,'JAYSON'),(1072,'JC'),(350,'JEAN'),(1410,'JEANETTE'),(1985,'JEANIE'),(1867,'JEANINE'),(2313,'JEANNA'),(1419,'JEANNE'),(1583,'JEANNETTE'),(1698,'JEANNIE'),(1902,'JEANNINE'),(961,'JED'),(120,'JEFF'),(999,'JEFFEREY'),(662,'JEFFERSON'),(118,'JEFFERY'),(30,'JEFFREY'),(620,'JEFFRY'),(2308,'JENA'),(1787,'JENIFER'),(1672,'JENNA'),(1484,'JENNIE'),(1225,'JENNIFER'),(1501,'JENNY'),(499,'JERALD'),(1203,'JERAMY'),(1213,'JERE'),(311,'JEREMIAH'),(76,'JEREMY'),(1921,'JERI'),(400,'JERMAINE'),(904,'JEROLD'),(166,'JEROME'),(1186,'JEROMY'),(1185,'JERRELL'),(2020,'JERRI'),(874,'JERROD'),(981,'JERROLD'),(39,'JERRY'),(509,'JESS'),(89,'JESSE'),(1245,'JESSICA'),(235,'JESSIE'),(129,'JESUS'),(1212,'JEWEL'),(1178,'JEWELL'),(1378,'JILL'),(1805,'JILLIAN'),(153,'JIM'),(260,'JIMMIE'),(99,'JIMMY'),(1469,'JO'),(759,'JOAN'),(1383,'JOANN'),(1543,'JOANNA'),(1366,'JOANNE'),(579,'JOAQUIN'),(1832,'JOCELYN'),(1554,'JODI'),(1882,'JODIE'),(432,'JODY'),(51,'JOE'),(133,'JOEL'),(622,'JOESPH'),(307,'JOEY'),(1705,'JOHANNA'),(2,'JOHN'),(354,'JOHNATHAN'),(718,'JOHNATHON'),(827,'JOHNIE'),(273,'JOHNNIE'),(97,'JOHNNY'),(1128,'JOHNSON'),(1843,'JOLENE'),(157,'JON'),(980,'JONAH'),(816,'JONAS'),(55,'JONATHAN'),(369,'JONATHON'),(1834,'JONI'),(262,'JORDAN'),(1088,'JORDON'),(175,'JORGE'),(28,'JOSE'),(867,'JOSEF'),(2118,'JOSEFA'),(1697,'JOSEFINA'),(9,'JOSEPH'),(1340,'JOSEPHINE'),(437,'JOSH'),(38,'JOSHUA'),(913,'JOSIAH'),(1762,'JOSIE'),(1159,'JOSPEH'),(767,'JOSUE'),(1449,'JOY'),(1268,'JOYCE'),(52,'JUAN'),(1582,'JUANA'),(1354,'JUANITA'),(1114,'JUDE'),(2234,'JUDI'),(1283,'JUDITH'),(926,'JUDSON'),(1288,'JUDY'),(882,'JULES'),(1308,'JULIA'),(276,'JULIAN'),(2063,'JULIANA'),(2037,'JULIANNE'),(1271,'JULIE'),(2152,'JULIET'),(2115,'JULIETTE'),(247,'JULIO'),(313,'JULIUS'),(1395,'JUNE'),(532,'JUNIOR'),(56,'JUSTIN'),(2256,'JUSTINA'),(1887,'JUSTINE'),(2187,'KAITLIN'),(2262,'KAITLYN'),(1600,'KARA'),(858,'KAREEM'),(1232,'KAREN'),(1635,'KARI'),(1765,'KARIN'),(1999,'KARINA'),(228,'KARL'),(1589,'KARLA'),(2363,'KARRIE'),(2140,'KARYN'),(1177,'KASEY'),(1683,'KATE'),(2101,'KATELYN'),(1880,'KATHARINE'),(1280,'KATHERINE'),(2170,'KATHERYN'),(2245,'KATHI'),(2075,'KATHIE'),(1255,'KATHLEEN'),(2047,'KATHRINE'),(1301,'KATHRYN'),(1290,'KATHY'),(1412,'KATIE'),(2166,'KATINA'),(1518,'KATRINA'),(2036,'KATY'),(1491,'KAY'),(2056,'KAYE'),(1557,'KAYLA'),(1021,'KEENAN'),(1844,'KEISHA'),(59,'KEITH'),(1082,'KELLEY'),(1585,'KELLI'),(1686,'KELLIE'),(248,'KELLY'),(1774,'KELSEY'),(358,'KELVIN'),(265,'KEN'),(538,'KENDALL'),(1622,'KENDRA'),(605,'KENDRICK'),(1194,'KENETH'),(17,'KENNETH'),(804,'KENNITH'),(324,'KENNY'),(293,'KENT'),(987,'KENTON'),(2043,'KENYA'),(1848,'KERI'),(588,'KERMIT'),(1757,'KERRI'),(346,'KERRY'),(949,'KEVEN'),(23,'KEVIN'),(898,'KIETH'),(397,'KIM'),(2250,'KIMBERLEE'),(1811,'KIMBERLEY'),(1243,'KIMBERLY'),(1055,'KING'),(995,'KIP'),(2384,'KIRA'),(722,'KIRBY'),(288,'KIRK'),(1851,'KIRSTEN'),(2416,'KISHA'),(2206,'KITTY'),(1158,'KOREY'),(947,'KORY'),(1184,'KRAIG'),(634,'KRIS'),(1607,'KRISTA'),(1413,'KRISTEN'),(1538,'KRISTI'),(1719,'KRISTIE'),(1433,'KRISTIN'),(1509,'KRISTINA'),(1561,'KRISTINE'),(1173,'KRISTOFER'),(414,'KRISTOPHER'),(1574,'KRISTY'),(1632,'KRYSTAL'),(2279,'KRYSTLE'),(249,'KURT'),(734,'KURTIS'),(126,'KYLE'),(2427,'KYLIE'),(1845,'LACEY'),(1063,'LACY'),(1906,'LADONNA'),(2087,'LAKEISHA'),(2267,'LAKESHA'),(2039,'LAKISHA'),(462,'LAMAR'),(516,'LAMONT'),(1689,'LANA'),(245,'LANCE'),(749,'LANDON'),(729,'LANE'),(857,'LANNY'),(1914,'LARA'),(2396,'LARISSA'),(29,'LARRY'),(2372,'LASHAWN'),(2233,'LASHONDA'),(2247,'LATANYA'),(1727,'LATASHA'),(1934,'LATISHA'),(1793,'LATONYA'),(1591,'LATOYA'),(2306,'LATRICE'),(1241,'LAURA'),(1769,'LAUREL'),(1202,'LAUREN'),(428,'LAURENCE'),(2426,'LAURETTA'),(2175,'LAURI'),(1411,'LAURIE'),(880,'LAVERN'),(788,'LAVERNE'),(2050,'LAVONNE'),(2071,'LAWANDA'),(1052,'LAWERENCE'),(63,'LAWRENCE'),(823,'LAZARO'),(1884,'LEA'),(1489,'LEAH'),(1183,'LEANDRO'),(1938,'LEANN'),(2133,'LEANNA'),(1901,'LEANNE'),(123,'LEE'),(2241,'LEEANN'),(1201,'LEIF'),(1127,'LEIGH'),(1927,'LEILA'),(1704,'LELA'),(403,'LELAND'),(2159,'LELIA'),(879,'LEMUEL'),(1104,'LEN'),(1478,'LENA'),(941,'LENARD'),(968,'LENNY'),(1754,'LENORA'),(1976,'LENORE'),(168,'LEO'),(1836,'LEOLA'),(162,'LEON'),(1500,'LEONA'),(107,'LEONARD'),(552,'LEONARDO'),(742,'LEONEL'),(2110,'LEONOR'),(865,'LEOPOLDO'),(2348,'LEORA'),(2435,'LEOTA'),(144,'LEROY'),(895,'LES'),(2213,'LESA'),(1172,'LESLEY'),(208,'LESLIE'),(2123,'LESSIE'),(194,'LESTER'),(2210,'LETA'),(1953,'LETHA'),(1605,'LETICIA'),(2205,'LETITIA'),(2390,'LETTIE'),(416,'LEVI'),(180,'LEWIS'),(2244,'LIBBY'),(1964,'LIDIA'),(1688,'LILA'),(2012,'LILIA'),(2062,'LILIAN'),(2120,'LILIANA'),(1317,'LILLIAN'),(1452,'LILLIE'),(1951,'LILLY'),(1871,'LILY'),(2060,'LINA'),(781,'LINCOLN'),(1222,'LINDA'),(1211,'LINDSAY'),(834,'LINDSEY'),(1107,'LINO'),(711,'LINWOOD'),(420,'LIONEL'),(1230,'LISA'),(2174,'LIZ'),(2027,'LIZA'),(1850,'LIZZIE'),(160,'LLOYD'),(523,'LOGAN'),(1310,'LOIS'),(1577,'LOLA'),(2188,'LOLITA'),(800,'LON'),(2344,'LONA'),(1157,'LONG'),(244,'LONNIE'),(1103,'LONNY'),(1639,'LORA'),(1989,'LORAINE'),(376,'LOREN'),(1685,'LORENA'),(1695,'LORENE'),(339,'LORENZO'),(1408,'LORETTA'),(1297,'LORI'),(1905,'LORIE'),(1791,'LORNA'),(1384,'LORRAINE'),(2403,'LORRI'),(2019,'LORRIE'),(1756,'LOTTIE'),(1025,'LOU'),(2209,'LOUELLA'),(514,'LOUIE'),(75,'LOUIS'),(2029,'LOUISA'),(1302,'LOUISE'),(1768,'LOURDES'),(391,'LOWELL'),(639,'LOYD'),(2065,'LUANN'),(381,'LUCAS'),(1701,'LUCIA'),(833,'LUCIANO'),(844,'LUCIEN'),(1900,'LUCILE'),(1364,'LUCILLE'),(1745,'LUCINDA'),(873,'LUCIO'),(1171,'LUCIUS'),(2362,'LUCRETIA'),(1427,'LUCY'),(1889,'LUELLA'),(1193,'LUIGI'),(104,'LUIS'),(1920,'LUISA'),(320,'LUKE'),(1576,'LULA'),(971,'LUPE'),(309,'LUTHER'),(1569,'LUZ'),(1462,'LYDIA'),(328,'LYLE'),(912,'LYMAN'),(2389,'LYN'),(1548,'LYNDA'),(1020,'LYNDON'),(1654,'LYNETTE'),(330,'LYNN'),(1597,'LYNNE'),(2130,'LYNNETTE'),(1210,'LYNWOOD'),(2255,'MA'),(1475,'MABEL'),(1627,'MABLE'),(979,'MAC'),(415,'MACK'),(2155,'MADELEINE'),(1549,'MADELINE'),(2113,'MADELYN'),(2074,'MADGE'),(2395,'MADONNA'),(1512,'MAE'),(1922,'MAGDALENA'),(1556,'MAGGIE'),(2144,'MAI'),(826,'MAJOR'),(355,'MALCOLM'),(1156,'MALCOM'),(1148,'MALIK'),(2003,'MALINDA'),(2343,'MALISSA'),(2031,'MALLORY'),(1575,'MAMIE'),(1176,'MAN'),(1694,'MANDY'),(1139,'MANUAL'),(110,'MANUEL'),(1908,'MANUELA'),(2156,'MARA'),(201,'MARC'),(613,'MARCEL'),(728,'MARCELINO'),(1630,'MARCELLA'),(1170,'MARCELLUS'),(946,'MARCELO'),(2109,'MARCI'),(1455,'MARCIA'),(2026,'MARCIE'),(363,'MARCO'),(417,'MARCOS'),(146,'MARCUS'),(1919,'MARCY'),(1228,'MARGARET'),(1525,'MARGARITA'),(890,'MARGARITO'),(2042,'MARGERY'),(1486,'MARGIE'),(1895,'MARGO'),(2237,'MARGOT'),(1890,'MARGRET'),(1533,'MARGUERITE'),(2081,'MARI'),(940,'MARIA'),(1465,'MARIAN'),(2150,'MARIANA'),(2336,'MARIANNA'),(1599,'MARIANNE'),(746,'MARIANO'),(1824,'MARIBEL'),(2041,'MARICELA'),(1263,'MARIE'),(2108,'MARIETTA'),(1299,'MARILYN'),(2361,'MARILYNN'),(1720,'MARINA'),(143,'MARIO'),(290,'MARION'),(1860,'MARISA'),(2347,'MARISELA'),(1865,'MARISOL'),(1764,'MARISSA'),(1899,'MARITZA'),(1347,'MARJORIE'),(14,'MARK'),(1029,'MARKUS'),(1725,'MARLA'),(1459,'MARLENE'),(609,'MARLIN'),(497,'MARLON'),(856,'MARQUIS'),(2180,'MARQUITA'),(1476,'MARSHA'),(285,'MARSHALL'),(1702,'MARTA'),(1257,'MARTHA'),(85,'MARTIN'),(1894,'MARTINA'),(421,'MARTY'),(2106,'MARVA'),(115,'MARVIN'),(699,'MARY'),(1564,'MARYANN'),(2067,'MARYANNE'),(2278,'MARYBETH'),(2173,'MARYELLEN'),(2322,'MARYJANE'),(1978,'MARYLOU'),(632,'MASON'),(241,'MATHEW'),(1926,'MATILDA'),(2394,'MATILDE'),(329,'MATT'),(25,'MATTHEW'),(1471,'MATTIE'),(1786,'MAUDE'),(2312,'MAUDIE'),(2143,'MAURA'),(1445,'MAUREEN'),(184,'MAURICE'),(665,'MAURICIO'),(2305,'MAURINE'),(955,'MAURO'),(1896,'MAVIS'),(255,'MAX'),(1048,'MAXIMO'),(1473,'MAXINE'),(695,'MAXWELL'),(1691,'MAY'),(2383,'MAYA'),(2415,'MAYME'),(672,'MAYNARD'),(1854,'MAYRA'),(1047,'MCKINLEY'),(1924,'MEAGAN'),(1370,'MEGAN'),(1659,'MEGHAN'),(945,'MEL'),(1407,'MELANIE'),(1712,'MELBA'),(1442,'MELINDA'),(1979,'MELISA'),(1249,'MELISSA'),(2249,'MELLISA'),(2418,'MELLISSA'),(1568,'MELODY'),(2070,'MELVA'),(124,'MELVIN'),(1652,'MERCEDES'),(1653,'MEREDITH'),(468,'MERLE'),(725,'MERLIN'),(726,'MERRILL'),(2371,'MERRY'),(819,'MERVIN'),(1962,'MIA'),(520,'MICAH'),(4,'MICHAEL'),(2271,'MICHAELA'),(1209,'MICHAL'),(1145,'MICHALE'),(147,'MICHEAL'),(627,'MICHEL'),(1373,'MICHELE'),(2226,'MICHELL'),(1240,'MICHELLE'),(541,'MICKEY'),(2351,'MIGDALIA'),(150,'MIGUEL'),(105,'MIKE'),(1006,'MIKEL'),(1991,'MILAGROS'),(1106,'MILAN'),(1279,'MILDRED'),(519,'MILES'),(907,'MILFORD'),(644,'MILLARD'),(2169,'MILLICENT'),(1874,'MILLIE'),(960,'MILO'),(210,'MILTON'),(2248,'MINA'),(1692,'MINDY'),(1950,'MINERVA'),(1014,'MINH'),(1458,'MINNIE'),(1062,'MIQUEL'),(1699,'MIRANDA'),(1504,'MIRIAM'),(1511,'MISTY'),(852,'MITCH'),(818,'MITCHEL'),(222,'MITCHELL'),(2080,'MITZI'),(1137,'MODESTO'),(974,'MOHAMED'),(745,'MOHAMMAD'),(839,'MOHAMMED'),(607,'MOISES'),(1820,'MOLLIE'),(1536,'MOLLY'),(1640,'MONA'),(1350,'MONICA'),(2232,'MONIKA'),(1553,'MONIQUE'),(766,'MONROE'),(558,'MONTE'),(628,'MONTY'),(508,'MORGAN'),(278,'MORRIS'),(815,'MORTON'),(1125,'MOSE'),(472,'MOSES'),(1005,'MOSHE'),(1629,'MURIEL'),(564,'MURRAY'),(813,'MYLES'),(1604,'MYRA'),(1724,'MYRNA'),(386,'MYRON'),(1477,'MYRTLE'),(2073,'NADIA'),(1634,'NADINE'),(2240,'NAN'),(1231,'NANCY'),(2048,'NANETTE'),(1987,'NANNIE'),(1493,'NAOMI'),(903,'NAPOLEON'),(2128,'NATALIA'),(1435,'NATALIE'),(1530,'NATASHA'),(108,'NATHAN'),(1121,'NATHANAEL'),(1087,'NATHANIAL'),(206,'NATHANIEL'),(337,'NEAL'),(624,'NED'),(234,'NEIL'),(1949,'NELDA'),(1807,'NELL'),(1457,'NELLIE'),(2228,'NELLY'),(251,'NELSON'),(706,'NESTOR'),(1714,'NETTIE'),(1977,'NEVA'),(1081,'NEVILLE'),(966,'NEWTON'),(64,'NICHOLAS'),(1624,'NICHOLE'),(308,'NICK'),(671,'NICKOLAS'),(1151,'NICKY'),(427,'NICOLAS'),(1287,'NICOLE'),(886,'NIGEL'),(1759,'NIKKI'),(2326,'NILDA'),(1487,'NINA'),(2033,'NITA'),(458,'NOAH'),(1004,'NOBLE'),(658,'NOE'),(383,'NOEL'),(2204,'NOELLE'),(2025,'NOEMI'),(1943,'NOLA'),(597,'NOLAN'),(2079,'NONA'),(1485,'NORA'),(594,'NORBERT'),(902,'NORBERTO'),(2004,'NOREEN'),(1313,'NORMA'),(113,'NORMAN'),(897,'NORMAND'),(643,'NORRIS'),(753,'NUMBERS'),(2221,'OCTAVIA'),(786,'OCTAVIO'),(694,'ODELL'),(1986,'ODESSA'),(939,'ODIS'),(1923,'OFELIA'),(1818,'OLA'),(958,'OLEN'),(1496,'OLGA'),(922,'OLIN'),(1675,'OLIVE'),(319,'OLIVER'),(1571,'OLIVIA'),(670,'OLLIE'),(2376,'OMA'),(340,'OMAR'),(1035,'OMER'),(1565,'OPAL'),(2077,'OPHELIA'),(1713,'ORA'),(2393,'ORALIA'),(1003,'OREN'),(332,'ORLANDO'),(954,'ORVAL'),(430,'ORVILLE'),(151,'OSCAR'),(894,'OSVALDO'),(1117,'OSWALDO'),(1061,'OTHA'),(316,'OTIS'),(504,'OTTO'),(410,'OWEN'),(338,'PABLO'),(1833,'PAIGE'),(1113,'PALMER'),(1584,'PAM'),(2270,'PAMALA'),(1256,'PAMELA'),(2202,'PANSY'),(1060,'PARIS'),(878,'PARKER'),(744,'PASQUALE'),(457,'PAT'),(1810,'PATRICA'),(1729,'PATRICE'),(1192,'PATRICIA'),(42,'PATRICK'),(1481,'PATSY'),(1684,'PATTI'),(2358,'PATTIE'),(1592,'PATTY'),(13,'PAUL'),(1314,'PAULA'),(1638,'PAULETTE'),(2434,'PAULINA'),(1352,'PAULINE'),(1443,'PEARL'),(2001,'PEARLIE'),(177,'PEDRO'),(1320,'PEGGY'),(1990,'PENELOPE'),(1490,'PENNY'),(465,'PERCY'),(287,'PERRY'),(365,'PETE'),(43,'PETER'),(1840,'PETRA'),(460,'PHIL'),(95,'PHILIP'),(87,'PHILLIP'),(2402,'PHILLIS'),(2168,'PHOEBE'),(1312,'PHYLLIS'),(531,'PIERRE'),(2429,'PILAR'),(1801,'POLLY'),(938,'PORFIRIO'),(1200,'PORTER'),(2370,'PORTIA'),(360,'PRESTON'),(937,'PRINCE'),(1492,'PRISCILLA'),(2142,'QUEEN'),(562,'QUENTIN'),(675,'QUINCY'),(994,'QUINN'),(1009,'QUINTIN'),(600,'QUINTON'),(1625,'RACHAEL'),(2338,'RACHEAL'),(1298,'RACHEL'),(1835,'RACHELLE'),(1970,'RAE'),(207,'RAFAEL'),(2382,'RAFAELA'),(1051,'RALEIGH'),(62,'RALPH'),(455,'RAMIRO'),(196,'RAMON'),(1515,'RAMONA'),(477,'RANDAL'),(139,'RANDALL'),(825,'RANDELL'),(1932,'RANDI'),(366,'RANDOLPH'),(78,'RANDY'),(616,'RAPHAEL'),(1665,'RAQUEL'),(1054,'RASHAD'),(211,'RAUL'),(132,'RAY'),(1094,'RAYFORD'),(1093,'RAYMON'),(36,'RAYMOND'),(776,'RAYMUNDO'),(1750,'REBA'),(2277,'REBECA'),(1253,'REBECCA'),(1744,'REBEKAH'),(661,'REED'),(892,'REFUGIO'),(631,'REGGIE'),(1387,'REGINA'),(202,'REGINALD'),(812,'REID'),(2425,'REINA'),(872,'REINALDO'),(1723,'RENA'),(2252,'RENAE'),(1169,'RENALDO'),(1071,'RENATO'),(295,'RENE'),(1401,'RENEE'),(2398,'RENITA'),(2424,'RETA'),(2296,'RETHA'),(549,'REUBEN'),(2089,'REVA'),(333,'REX'),(1168,'REY'),(977,'REYES'),(2191,'REYNA'),(506,'REYNALDO'),(2179,'RHEA'),(1112,'RHETT'),(1940,'RHODA'),(1356,'RHONDA'),(191,'RICARDO'),(821,'RICH'),(7,'RICHARD'),(970,'RICHIE'),(193,'RICK'),(349,'RICKEY'),(657,'RICKIE'),(137,'RICKY'),(916,'RICO'),(653,'RIGOBERTO'),(669,'RILEY'),(1323,'RITA'),(603,'ROB'),(537,'ROBBIE'),(2320,'ROBBIN'),(780,'ROBBY'),(3,'ROBERT'),(1404,'ROBERTA'),(186,'ROBERTO'),(375,'ROBIN'),(982,'ROBT'),(1611,'ROBYN'),(645,'ROCCO'),(1662,'ROCHELLE'),(2261,'ROCIO'),(530,'ROCKY'),(599,'ROD'),(345,'RODERICK'),(525,'RODGER'),(111,'RODNEY'),(385,'RODOLFO'),(871,'RODRICK'),(648,'RODRIGO'),(448,'ROGELIO'),(50,'ROGER'),(223,'ROLAND'),(463,'ROLANDO'),(1167,'ROLF'),(986,'ROLLAND'),(475,'ROMAN'),(688,'ROMEO'),(221,'RON'),(21,'RONALD'),(1730,'RONDA'),(158,'RONNIE'),(798,'RONNY'),(398,'ROOSEVELT'),(629,'RORY'),(1331,'ROSA'),(2304,'ROSALEE'),(2274,'ROSALIA'),(1614,'ROSALIE'),(1861,'ROSALIND'),(2010,'ROSALINDA'),(1958,'ROSALYN'),(2146,'ROSANNA'),(2102,'ROSANNE'),(953,'ROSARIO'),(589,'ROSCOE'),(1284,'ROSE'),(2086,'ROSEANN'),(2129,'ROSELLA'),(1642,'ROSEMARIE'),(1423,'ROSEMARY'),(906,'ROSENDO'),(1798,'ROSETTA'),(1542,'ROSIE'),(2335,'ROSITA'),(2046,'ROSLYN'),(282,'ROSS'),(2190,'ROWENA'),(2414,'ROXANNA'),(1608,'ROXANNE'),(2045,'ROXIE'),(65,'ROY'),(851,'ROYAL'),(539,'ROYCE'),(203,'RUBEN'),(850,'RUBIN'),(1309,'RUBY'),(1166,'RUDOLF'),(356,'RUDOLPH'),(359,'RUDY'),(1182,'RUEBEN'),(411,'RUFUS'),(985,'RUPERT'),(811,'RUSS'),(554,'RUSSEL'),(82,'RUSSELL'),(626,'RUSTY'),(1238,'RUTH'),(1948,'RUTHIE'),(49,'RYAN'),(1531,'SABRINA'),(1618,'SADIE'),(952,'SAL'),(1752,'SALLIE'),(1386,'SALLY'),(286,'SALVADOR'),(389,'SALVATORE'),(192,'SAM'),(1396,'SAMANTHA'),(2392,'SAMATHA'),(625,'SAMMIE'),(413,'SAMMY'),(1059,'SAMUAL'),(60,'SAMUEL'),(2381,'SANDI'),(1235,'SANDRA'),(840,'SANDY'),(702,'SANFORD'),(908,'SANG'),(450,'SANTIAGO'),(1165,'SANTO'),(494,'SANTOS'),(1303,'SARA'),(1242,'SARAH'),(2112,'SASHA'),(474,'SAUL'),(2006,'SAUNDRA'),(2125,'SAVANNAH'),(673,'SCOT'),(32,'SCOTT'),(837,'SCOTTIE'),(593,'SCOTTY'),(94,'SEAN'),(677,'SEBASTIAN'),(2034,'SELENA'),(2266,'SELINA'),(1910,'SELMA'),(1981,'SERENA'),(289,'SERGIO'),(292,'SETH'),(836,'SEYMOUR'),(1164,'SHAD'),(1852,'SHANA'),(2401,'SHANDA'),(190,'SHANE'),(1812,'SHANNA'),(317,'SHANNON'),(1706,'SHARI'),(2212,'SHARLENE'),(1239,'SHARON'),(1969,'SHARRON'),(327,'SHAUN'),(1873,'SHAUNA'),(92,'SHAWN'),(1709,'SHAWNA'),(2388,'SHAYLA'),(1086,'SHAYNE'),(1868,'SHEENA'),(1343,'SHEILA'),(656,'SHELBY'),(442,'SHELDON'),(1593,'SHELIA'),(1573,'SHELLEY'),(2346,'SHELLIE'),(1513,'SHELLY'),(773,'SHELTON'),(2092,'SHEREE'),(1598,'SHERI'),(393,'SHERMAN'),(1516,'SHERRI'),(1731,'SHERRIE'),(1338,'SHERRY'),(1092,'SHERWOOD'),(1526,'SHERYL'),(2369,'SHIELA'),(936,'SHIRLEY'),(1208,'SHON'),(2316,'SHONDA'),(1199,'SID'),(274,'SIDNEY'),(2225,'SIERRA'),(719,'SILAS'),(1678,'SILVIA'),(404,'SIMON'),(1930,'SIMONE'),(1892,'SOCORRO'),(1984,'SOFIA'),(1050,'SOL'),(2350,'SOLEDAD'),(592,'SOLOMON'),(608,'SON'),(1858,'SONDRA'),(1503,'SONIA'),(1687,'SONJA'),(772,'SONNY'),(1558,'SONYA'),(1660,'SOPHIA'),(1682,'SOPHIE'),(384,'SPENCER'),(650,'STACEY'),(1883,'STACI'),(1735,'STACIE'),(517,'STACY'),(550,'STAN'),(1002,'STANFORD'),(106,'STANLEY'),(1102,'STANTON'),(708,'STEFAN'),(1816,'STEFANIE'),(1466,'STELLA'),(513,'STEPHAN'),(1260,'STEPHANIE'),(34,'STEPHEN'),(540,'STERLING'),(74,'STEVE'),(18,'STEVEN'),(799,'STEVIE'),(445,'STEWART'),(302,'STUART'),(1416,'SUE'),(1881,'SUMMER'),(2275,'SUN'),(984,'SUNG'),(1227,'SUSAN'),(1827,'SUSANA'),(2088,'SUSANNA'),(1825,'SUSANNE'),(1570,'SUSIE'),(2387,'SUZAN'),(1372,'SUZANNE'),(2054,'SUZETTE'),(1912,'SYBIL'),(848,'SYDNEY'),(396,'SYLVESTER'),(1339,'SYLVIA'),(1980,'TABATHA'),(1715,'TABITHA'),(935,'TAD'),(1448,'TAMARA'),(2005,'TAMEKA'),(2183,'TAMERA'),(1708,'TAMI'),(1797,'TAMIKA'),(2053,'TAMMI'),(1726,'TAMMIE'),(1294,'TAMMY'),(2137,'TAMRA'),(2433,'TANA'),(1996,'TANIA'),(2035,'TANISHA'),(931,'TANNER'),(1456,'TANYA'),(1422,'TARA'),(2309,'TARYN'),(1677,'TASHA'),(2375,'TAWANA'),(422,'TAYLOR'),(240,'TED'),(505,'TEDDY'),(1046,'TEODORO'),(2360,'TERA'),(449,'TERENCE'),(1273,'TERESA'),(2319,'TERESITA'),(1655,'TERI'),(2218,'TERRA'),(294,'TERRANCE'),(471,'TERRELL'),(297,'TERRENCE'),(1425,'TERRI'),(1952,'TERRIE'),(57,'TERRY'),(2104,'TESSA'),(2359,'TESSIE'),(846,'THAD'),(611,'THADDEUS'),(963,'THANH'),(1341,'THELMA'),(1023,'THEO'),(2318,'THEODORA'),(148,'THEODORE'),(1291,'THERESA'),(1788,'THERESE'),(775,'THERON'),(10,'THOMAS'),(623,'THURMAN'),(1968,'TIA'),(2432,'TIFFANI'),(1329,'TIFFANY'),(2298,'TILLIE'),(170,'TIM'),(479,'TIMMY'),(27,'TIMOTHY'),(1311,'TINA'),(2182,'TISHA'),(1097,'TITUS'),(1013,'TOBIAS'),(483,'TOBY'),(944,'TOD'),(88,'TODD'),(154,'TOM'),(441,'TOMAS'),(478,'TOMMIE'),(161,'TOMMY'),(1120,'TONEY'),(1510,'TONI'),(1863,'TONIA'),(103,'TONY'),(1428,'TONYA'),(2220,'TORI'),(969,'TORY'),(835,'TRACEY'),(1620,'TRACI'),(1763,'TRACIE'),(291,'TRACY'),(119,'TRAVIS'),(510,'TRENT'),(733,'TRENTON'),(2301,'TREVA'),(318,'TREVOR'),(901,'TREY'),(1673,'TRICIA'),(1749,'TRINA'),(934,'TRINIDAD'),(1758,'TRISHA'),(778,'TRISTAN'),(138,'TROY'),(1808,'TRUDY'),(716,'TRUMAN'),(1150,'TUAN'),(2192,'TWILA'),(655,'TY'),(198,'TYLER'),(998,'TYREE'),(1019,'TYRELL'),(1034,'TYRON'),(242,'TYRONE'),(569,'TYSON'),(679,'ULYSSES'),(1849,'URSULA'),(1091,'VAL'),(1956,'VALARIE'),(2413,'VALENCIA'),(841,'VALENTIN'),(1041,'VALENTINE'),(2078,'VALERIA'),(1368,'VALERIE'),(484,'VAN'),(576,'VANCE'),(1414,'VANESSA'),(636,'VAUGHN'),(2357,'VEDA'),(1505,'VELMA'),(1437,'VERA'),(2377,'VERDA'),(659,'VERN'),(1578,'VERNA'),(2368,'VERNICE'),(185,'VERNON'),(1377,'VERONICA'),(2397,'VESTA'),(515,'VICENTE'),(1420,'VICKI'),(1470,'VICKIE'),(1596,'VICKY'),(84,'VICTOR'),(1335,'VICTORIA'),(2334,'VIDA'),(2127,'VILMA'),(674,'VINCE'),(116,'VINCENT'),(1207,'VINCENZO'),(1463,'VIOLA'),(1508,'VIOLET'),(1933,'VIRGIE'),(283,'VIRGIL'),(1119,'VIRGILIO'),(2295,'VIRGINA'),(1254,'VIRGINIA'),(738,'VITO'),(1403,'VIVIAN'),(1162,'VON'),(2201,'VONDA'),(300,'WADE'),(991,'WALDO'),(997,'WALKER'),(264,'WALLACE'),(1191,'WALLY'),(41,'WALTER'),(1111,'WALTON'),(1306,'WANDA'),(687,'WARD'),(1136,'WARNER'),(164,'WARREN'),(1161,'WAYLON'),(72,'WAYNE'),(713,'WELDON'),(310,'WENDELL'),(2103,'WENDI'),(1334,'WENDY'),(1022,'WERNER'),(1147,'WES'),(171,'WESLEY'),(875,'WESTON'),(1045,'WHITNEY'),(1070,'WILBER'),(402,'WILBERT'),(341,'WILBUR'),(810,'WILBURN'),(2139,'WILDA'),(652,'WILEY'),(771,'WILFORD'),(436,'WILFRED'),(573,'WILFREDO'),(2303,'WILHELMINA'),(503,'WILL'),(1907,'WILLA'),(280,'WILLARD'),(5,'WILLIAM'),(596,'WILLIAMS'),(1206,'WILLIAN'),(61,'WILLIE'),(348,'WILLIS'),(1001,'WILLY'),(1431,'WILMA'),(758,'WILMER'),(394,'WILSON'),(915,'WILTON'),(1190,'WINFORD'),(737,'WINFRED'),(1718,'WINIFRED'),(1918,'WINNIE'),(2333,'WINONA'),(481,'WINSTON'),(364,'WM'),(412,'WOODROW'),(855,'WYATT'),(587,'XAVIER'),(2072,'YESENIA'),(1409,'YOLANDA'),(921,'YONG'),(807,'YOUNG'),(1567,'YVETTE'),(1393,'YVONNE'),(957,'ZACHARIAH'),(181,'ZACHARY'),(761,'ZACHERY'),(1135,'ZACK'),(1149,'ZACKARY'),(820,'ZANE'),(2253,'ZELDA'),(2380,'ZELLA'),(2002,'ZELMA'),(2408,'ZOE'),(2423,'ZOILA');
/*!40000 ALTER TABLE `first_name` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `minion`
--

LOCK TABLES `minion` WRITE;
/*!40000 ALTER TABLE `minion` DISABLE KEYS */;
INSERT INTO `minion` VALUES (1,'All U Can B TV','O',3,1),(2,'Brave Clone Enterprises','B',7,1),(3,'VIP Protection','B',7,1),(4,'Intersector Bodyguards','V',10,1),(5,'Less Than Lethal AF','R',2,1),(6,'Technically Humane','O',4,1),(7,'Smiling Songsters AF','Y',4,1),(8,'The Happy Cheery People','G',6,1),(9,'Alpha Hero Recruiters','G',5,1),(10,'Outdoor Rangers','G',5,1),(11,'Lasers4Hire','IR',1,1),(12,'Seeing RED','R',3,1),(13,'Brigadier Mike-B','B',7,1),(14,'General Arnold-V','V',11,1),(15,'Transportation Bottalion','R',2,1),(16,'Armed Forces Marching Band','R',2,1),(17,'Vulture Squadron Warriors','B',9,1),(18,'VultureCraft Assault Squadron','B',9,1),(19,'Mark IV Warbot','G',8,1),(20,'ICBM Silo','I',9,1),(21,'HerdEmUp Volunteer Supply','R',2,2),(22,'Management Focus Group','G',9,2),(23,'Yellowpants4Hire','Y',6,2),(24,'Computer Care Specialists','I',9,2),(25,'Drug Dispensers CPU','R',3,2),(26,'Better Living Thru Chemistry Plus Plus','G',8,2),(27,'Archive Sweepers CPU','R',3,2),(28,'Dirt Free is Treason Free Campaign','Y',5,2),(29,'Cheery Clean Complex Initiative','B',7,2),(30,'Data Divers','R',3,2),(31,'Goggle Search','G',6,2),(32,'Peter-I and Co Data Retrieval','I',8,2),(33,'Foreign Policy Strategic Working Group','B',7,2),(34,'Mandatory Break Monitors','O',4,2),(35,'Corridor Watchers HPD','Y',4,3),(36,'Camera Addicts','B',7,3),(37,'The Daycycle Show','Y',6,3),(38,'TodayCycle TonightCycle','G',8,3),(39,'CNNNN','I',10,3),(40,'Public Hating Co-Ordination','R',2,3),(41,'Trend Identifiers','R',2,3),(42,'Singalong Agents','R',2,3),(43,'Subliminals Police','B',8,3),(44,'Housing Services Supply','R',4,3),(45,'INFRARED Wranglers','R',3,3),(46,'Mandatory Fun Time Enthusiasts','R',3,3),(47,'Temporary Filing Staff Requisition','R',4,3),(48,'Entertainment Weekcycle','Y',5,3),(49,'This Is ULTRAVIOLET!','B',8,3),(50,'Alpha Complex Enquirer','V',11,3),(51,'Forensic Analysis Scrubbot Team','O',3,8),(52,'Bright Vision Re-Education Centre','B',7,8),(53,'Loyalty Surveyors','Y',5,8),(54,'Threat Assessors','Y',5,8),(55,'Secure Security Checkpoint Checkers','Y',4,8),(56,'Total Surveillance Assurance','G',6,8),(57,'Agent Provocateurs','B',8,8),(58,'IntSec Troopers','B',8,8),(59,'Traffic Patrol','B',8,8),(60,'Jackbooted Thugs','G',6,8),(61,'Facilitation Division','Y',5,8),(62,'Suspect Expediters','B',8,8),(63,'Cardholder Deprotagonizers','V',11,8),(64,'Mutant Registration','O',3,8),(65,'Information Retrieval Specialists','Y',5,8),(66,'Information Retrieval Specialists','B',7,8),(67,'Information Retrieval Specialists','V',9,8),(68,'Men in INDIGO','I',9,8),(69,'Conspicuous Surveillance Initiative','O',3,8),(70,'BLUE Room Caterers','R',2,4),(71,'BLUE Room Caterers','Y',4,4),(72,'BLUE Room Caterers','B',7,4),(73,'BLUE Room Caterers','V',9,4),(74,'Equipment Assembly Control','IR',1,4),(75,'Field Logistics Advisors','R',3,4),(76,'MuchlyAlgae PLC','IR',2,4),(77,'Inventory System Updaters','R',4,4),(78,'Brand Loyalty Police','R',3,4),(79,'Acme Chemical Production','R',3,4),(80,'BLUE Shield Clone Assurance','B',7,4),(81,'PLC Accounts Co-Ordination','O',5,4),(82,'New Flavour of Bouncy Bubble Beverage','IR',2,4),(83,'Advertising Campaign','R',5,4),(84,'C-Bay Trawlers','G',8,4),(85,'Enforced Reclamation and Recycling','Y',5,4),(86,'Circuit Maintenance','R',3,6),(87,'Fuel Rod Disposal Consultants','R',4,6),(88,'Pneumatic Tube Network Engineers','Y',7,6),(89,'Department of Transbot Control','O',6,6),(90,'New Transtube Planning Commission','I',10,6),(91,'Reactor Management Commission','G',6,6),(92,'Reactor Shielding Volunteer Corps','R',3,6),(93,'Crawlspace Commandoes','R',4,6),(94,'Vault Recovery Team','G',7,6),(95,'Toxic Environment Team','O',4,6),(96,'Biological Niceness Indexers','B',8,7),(97,'Security Technology Technicians','B',9,7),(98,'Bot Processing','Y',6,7),(99,'Drug Interaction Testers','G',7,7),(100,'Codename: KILLBOT','G',6,7),(101,'Anti-Gravity Device','V',12,7),(102,'Atomic Science Ethical Directorate','G',6,7),(103,'Think Tank Consultants','I',11,7),(104,'Silicon Corridor','B',8,7),(105,'Special Environment Clone Laboratories','G',7,7),(106,'Historical Artefact Analysis','Y',6,7),(107,'Experimental Equipment Field Testing','O',4,7),(108,'Foreign Contaminant Containment','O',6,7),(109,'Technically Non-Lethal Weapons','Y',5,7),(110,'Security Systems Installers','Y',5,5),(111,'Security Systems Installers','I',10,5),(112,'Clone Tank Support Services','R',2,5),(113,'Medical Services','Y',4,5),(115,'Medical Services','I',10,5),(116,'Paint Control','R',2,5),(117,'Slime Identification','O',4,5),(118,'Tech Support','Y',5,5),(119,'Abandoned Sector Reclamation Initiative','G',8,5),(120,'Dome Cleaning Services','O',5,5),(121,'Department of Pipes and Tubes','R',5,5),(122,'Bot Repair and Maintenance','Y',6,5),(123,'Scrubbot Army','R',3,5),(124,'Megastructure Construction Planning Group','B',10,5),(125,'Non-Specific Unit Production','G',7,5),(126,'Outside Broadcast Unit','Y',5,5),(127,'Alpha Complex Space Program','I',10,5),(128,'Vat Maintenance and Control','O',4,5),(129,'Vermin Terminators','R',3,5),(130,'Troubleshooter Team','R',1,9),(131,'Troubleshooter Team','O',2,9),(132,'Troubleshooter Team','Y',3,9),(133,'Troubleshooter Team','G',4,9),(134,'Troubleshooter Team','B',5,9),(135,'Troubleshooter Team','I',6,9),(136,'Troubleshooter Team','V',7,9),(137,'Alpha Team','I',13,9),(138,'Troubleshooter Dispatchers','B',6,9),(139,'Troubleshooter Debriefer','G',6,9),(140,'Sector Indexers','I',8,2),(141,'Credit Checkers','R',2,2),(142,'Productivity Maintainers','O',3,2),(143,'Queue Maintainers','R',2,3),(144,'Relocation Specialists','G',7,3),(145,'Intergroup Troubleshooter Trainers','Y',4,9),(146,'Troubleshooter University Lecturers','G',7,9),(147,'The BFG','I',10,7),(148,'Earthquake Machine','V',10,7),(149,'PD Infantry','IR',1,1),(150,'HappyTainers','R',3,1),(151,'AFE Wacky Comedy Tour','Y',6,1),(152,'All You Can Be Plus Plus','R',2,1),(153,'Your Body Your Temple','O',4,1),(154,'Rapid Reload','G',6,1),(155,'Fuel Speed Ahead','R',2,1),(156,'Sparkling Memorials','O',3,1),(157,'1st Recreational Regiment','IR',2,1),(158,'Chaplain Central','Y',5,1),(159,'AF Marching Band','R',3,1),(160,'PointFingers AF','R',2,1),(161,'Cellular Diversity Association','Y',5,1),(162,'Now That\'s What I CAll A Blast Radius','B',8,1),(163,'Workplace is Ourplace','Y',5,2),(164,'Save Space EDS','R',2,2),(165,'Save Space HCP','R',2,2),(166,'Form Field','B',8,2),(167,'Citizen Re-Placement','G',7,2),(168,'Institutional Memory Institution','Y',4,2),(169,'RecordIt','B',7,2),(170,'KnowMOR','V',10,2),(171,'Fine Line Ink','Y',4,2),(172,'We Break for Breaks','Y',6,2),(173,'MORmoney','V',10,2),(174,'Radcliffe-V-TBR-1 and clones','I',8,2),(175,'Kloff Notes','O',3,2),(176,'Attorneys at LAW Sector','V',12,2),(177,'Selfless Generosity Corp','O',3,3),(178,'Cathy-G\'s Donatorium','Y',5,3),(179,'Multi-Teela Marketing','B',9,3),(180,'NotCloning','G',6,3),(181,'Fold-a-lot','4',0,3),(182,'Future Bubble','G',5,3),(183,'Trauma Recovery Providers','Y',6,3),(184,'Megamedia','O',4,3),(185,'KID Media','G',7,3),(186,'PIX Films','G',8,3),(187,'Official Artists','G',6,3),(188,'The Computer\'s Monitors','I',10,3),(189,'MutieLovers','O',4,3),(190,'Le Facade','R',4,3),(191,'StopLite IS','G',6,8),(192,'Conspicuous Surveillance Initiative','O',3,8),(193,'Protect and Wander','Y',4,8),(194,'CipherSpace','Y',4,8),(195,'KeyCrypters','B',7,8),(196,'Defenders of the Indoors','G',6,8),(197,'Zero-Zero IS','B',8,8),(198,'Unseen Activities Initiative','V',11,8),(199,'SafeCrackers','Y',5,8),(200,'BreakIn4U','B',8,8),(201,'Every Able Body','O',3,8),(202,'Healthy Workplaces','G',7,8),(203,'IntSec Loves You','Y',5,8),(204,'Love Us Or Else','G',7,8),(205,'Printz IS','Y',5,8),(206,'HandCrafters PLC','Y',4,4),(207,'Artistic Licence','G',6,4),(208,'Give Something Back','O',3,4),(209,'Selective Dining HPD','O',3,4),(210,'Funfoods To Go!','Y',5,4),(211,'DigIt','G',7,4),(212,'GREENHouses','G',6,4),(213,'Small World PLC','O',3,4),(214,'Who Knows Ware','Y',5,4),(215,'Out With The Old','O',3,4),(216,'PLC Moderately Express Mail Delivery','O',3,4),(217,'PLC SupaExpress','Y',5,4),(218,'Stock Sports','Y',4,4),(219,'Extra Power!','Y',5,6),(220,'ExecuRide','I',8,6),(221,'We Guard Power!','G',5,6),(222,'UraniFun PS','Y',4,6),(223,'Nuclear Integrity','G',6,6),(224,'SuperSump','R',2,6),(225,'TransIt','R',3,6),(226,'AgriGreen','Y',4,7),(227,'Kinetic Energy Associates','Y',4,7),(228,'Force Majeur','G',6,7),(229,'Mutie-Free RD','O',4,7),(230,'Purer Genomes Through Science!','Y',7,7),(231,'Entropy Exterminators RD','G',6,7),(232,'Spacetime Removal Services','B',7,7),(233,'A.I. Trauma Care','Y',5,5),(234,'BulbFixers','R',2,5),(235,'Pipe Patrollers','R',2,5),(236,'TransBot Industries','Y',6,5),(237,'Verminators','R',2,5),(238,'Occult Geomaticians','V',10,7),(239,'Fangry Citizen Combat Squad','R',3,3),(240,'Consumption Imbalance Rebalancer','O',3,2);
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
INSERT INTO `minion_skill` VALUES (1,23,1),(1,43,0),(1,54,0),(1,96,0),(1,145,0),(1,160,0),(1,162,1),(1,163,1),(1,166,0),(1,171,0),(1,175,0),(2,21,0),(2,22,1),(2,89,0),(2,107,0),(2,137,0),(2,138,0),(2,143,0),(2,145,0),(2,167,1),(2,177,0),(2,178,1),(3,27,1),(3,28,1),(3,29,1),(3,96,0),(3,117,0),(3,120,0),(3,123,1),(3,129,0),(3,144,0),(3,149,0),(3,156,0),(3,183,0),(3,225,0),(3,232,0),(3,237,0),(4,47,0),(4,52,0),(4,65,1),(4,66,1),(4,67,1),(4,191,0),(5,22,1),(5,44,0),(5,47,0),(5,77,0),(5,81,0),(5,88,0),(5,146,0),(5,164,0),(5,166,1),(5,176,0),(6,7,0),(6,8,0),(6,40,0),(6,46,0),(6,78,0),(6,83,1),(6,158,0),(6,176,1),(6,178,0),(6,179,1),(6,180,1),(6,184,0),(6,185,0),(6,186,1),(6,187,0),(6,188,1),(6,189,0),(6,203,0),(6,204,1),(7,41,0),(7,53,1),(7,140,1),(7,193,0),(8,22,0),(8,23,0),(8,34,0),(8,68,0),(8,69,0),(8,81,0),(8,87,0),(8,172,0),(9,47,0),(9,54,0),(9,57,1),(9,137,0),(9,172,0),(9,180,0),(9,192,0),(10,2,0),(10,37,0),(10,38,1),(10,39,1),(10,49,0),(10,50,0),(10,139,0),(10,194,0),(10,195,1),(10,202,0),(10,205,1),(11,4,0),(11,55,0),(11,61,0),(11,62,0),(11,63,1),(11,97,1),(11,108,0),(11,110,0),(11,111,1),(11,199,0),(11,200,1),(12,35,0),(12,36,1),(12,48,0),(12,49,0),(12,50,0),(12,56,1),(12,97,1),(12,110,0),(12,111,1),(12,121,0),(12,235,0),(13,30,0),(13,31,0),(13,32,0),(13,106,0),(13,119,0),(13,144,0),(13,197,1),(13,198,1),(14,57,0),(14,86,0),(14,199,0),(14,200,1),(15,84,1),(15,93,0),(15,173,1),(15,174,0),(16,11,0),(16,12,1),(16,17,1),(16,18,1),(16,19,0),(16,58,0),(16,59,1),(16,100,0),(16,109,0),(16,147,2),(16,157,0),(17,13,0),(17,14,1),(17,33,0),(17,146,0),(18,5,0),(18,6,1),(18,45,0),(18,60,0),(18,89,0),(18,109,0),(18,159,0),(18,239,0),(19,19,0),(19,90,0),(19,101,1),(19,119,1),(19,124,0),(19,148,0),(19,227,0),(19,228,1),(20,10,0),(20,120,0),(20,126,0),(20,182,0),(21,3,0),(21,4,1),(21,58,1),(21,157,0),(21,191,0),(21,196,1),(21,221,0),(22,17,1),(22,61,0),(22,62,1),(22,63,1),(22,68,0),(22,93,0),(22,129,0),(22,137,1),(22,161,0),(22,197,0),(22,198,1),(22,238,1),(23,18,0),(23,19,0),(23,20,2),(23,147,2),(23,162,0),(24,95,0),(24,98,0),(24,118,0),(24,120,0),(24,122,1),(24,124,0),(24,233,0),(24,236,0),(25,44,0),(25,90,0),(25,91,0),(25,119,0),(25,124,2),(25,182,0),(25,211,0),(25,225,0),(26,79,0),(26,121,1),(26,128,0),(26,224,0),(27,86,0),(27,93,0),(27,121,0),(27,127,0),(27,181,1),(27,190,0),(27,234,0),(28,87,0),(28,91,0),(28,92,0),(28,102,0),(28,219,0),(28,222,0),(28,223,1),(28,231,0),(29,74,0),(29,76,0),(29,79,0),(29,85,0),(29,125,1),(29,206,0),(29,207,1),(29,215,0),(29,218,0),(30,101,2),(30,103,1),(30,107,0),(30,127,1),(30,137,0),(30,148,1),(30,219,0),(30,231,0),(30,232,0),(30,238,0),(31,15,0),(31,59,0),(31,75,0),(31,88,0),(31,89,1),(31,90,0),(31,105,0),(31,137,0),(31,154,1),(31,155,0),(31,211,0),(31,216,0),(31,217,1),(31,220,1),(31,236,0),(32,98,1),(32,100,0),(32,122,0),(32,233,0),(32,236,0),(33,1,0),(33,2,0),(33,13,0),(33,14,0),(33,88,0),(33,118,0),(33,126,0),(33,127,0),(33,137,0),(33,150,0),(33,151,1),(34,24,0),(34,104,0),(34,170,0),(35,24,0),(35,30,0),(35,31,0),(35,32,0),(35,94,0),(35,106,0),(35,165,0),(35,168,0),(35,169,0),(35,170,1),(35,176,1),(36,81,0),(36,84,1),(36,141,0),(36,173,1),(36,174,1),(37,24,0),(37,103,0),(37,104,1),(37,167,0),(37,169,0),(37,170,1),(38,44,0),(38,75,0),(38,77,1),(38,88,0),(38,90,0),(38,103,0),(38,116,0),(38,125,0),(38,208,0),(38,211,0),(38,213,0),(38,214,0),(39,37,0),(39,38,1),(39,39,1),(39,48,0),(39,49,0),(39,50,1),(39,68,0),(39,83,1),(39,150,0),(39,151,0),(39,184,0),(39,185,1),(39,186,1),(39,187,0),(39,188,1),(40,26,0),(40,51,0),(40,87,0),(40,94,0),(40,95,0),(40,96,0),(40,99,0),(40,108,0),(40,115,0),(40,117,0),(40,128,0),(40,226,0),(40,230,0),(41,70,0),(41,71,0),(41,72,1),(41,73,1),(41,76,0),(41,142,0),(41,209,0),(41,210,1),(41,212,1),(41,239,0),(41,240,0),(42,80,1),(42,105,0),(42,112,0),(42,115,0),(42,179,1),(42,229,0),(42,230,0),(43,99,0),(43,103,0),(43,108,0),(43,113,0),(43,115,1),(43,183,0),(43,201,0),(43,202,0),(44,64,0),(44,102,0),(44,105,0),(44,161,0),(44,189,0),(44,229,0),(44,230,0),(45,33,0),(45,106,0),(45,108,0),(46,8,0),(46,25,0),(46,26,1),(46,45,0),(46,46,0),(46,82,1),(46,99,0),(46,158,0),(46,183,0),(47,25,0),(47,26,0),(47,42,0),(47,43,1),(47,188,0),(48,47,0),(48,130,0),(48,131,0),(48,132,0),(48,133,0),(48,134,0),(48,135,0),(48,136,0),(48,137,0),(48,149,0),(49,3,0),(49,9,0),(49,14,0),(49,16,0),(49,34,0),(49,52,0),(49,60,0),(49,85,0),(49,92,0),(49,139,0),(49,144,0),(49,146,0),(49,152,0),(49,153,1),(49,159,0),(49,202,0),(50,17,0),(50,18,0),(51,19,0),(53,37,0),(53,48,0),(53,49,0),(53,50,0),(54,58,0),(55,94,0),(56,107,0),(57,126,0),(68,130,0),(68,131,0),(68,132,0),(68,133,0),(68,134,0),(68,135,0),(68,136,0);
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
-- Dumping data for table `name_old`
--

LOCK TABLES `name_old` WRITE;
/*!40000 ALTER TABLE `name_old` DISABLE KEYS */;
INSERT INTO `name_old` VALUES (1,'Dignif','Y','ING',1),(2,'Catacl','Y','SMM',1),(3,'Get','R','EKT',1),(4,'Falsef','R','ONT',1),(5,'Cryoph','O','BIA',1),(6,'Acroph','O','BIA',1),(7,'Paran','O','IAA',1),(8,'Carbon','Y','LIC',1),(9,'Polyester','G','IRL',1),(10,'Rei','G','NED',1),(11,'Roast','B','EEF',1),(12,'Cue','B','ONE',1),(13,'Advert','I','ZED',1),(14,'Dr','I','VER',1),(15,'Affida','V','ITS',1),(16,'Recidi','V','IST',1),(17,'Allosa','U','RUS',1),(18,'G','U','ARD',1),(19,'Canes','U','GAR',1),(20,'Abd','U','CED',1),(21,'Bag','U','ETT',3),(22,'Dys','U','RIA',1),(23,'Bra','V','ADO',1),(24,'Cra','V','ENN',1),(25,'Dri','V','ELS',1),(26,'Acap','U','LCO',1),(27,'Bisc','U','ITS',1),(28,'Cher','U','BIM',1),(29,'Daiq','U','IRI',1);
/*!40000 ALTER TABLE `name_old` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` VALUES (1,'Sector lottery jackpots to 7.2 million credits!',NULL),(2,'Alpha Complex Space Agency landing declared hoax! Execution of agency head ##CIT-I-RD## scheduled for 6.',NULL),(3,'Borders to neighbouring sectors found lacking. ##CIT-I-IS##: \"It\'s like any old RED could get through, they will be fortified!\"',NULL),(4,'New craze \"planking\" declared waste of time and treasonous.',NULL),(5,'There was no shuddering of Alpha Complex. Alpha Complex is as stable as ever. Those who believe otherwise are encouraged to contact IntSec.',NULL),(6,'Survey on pronunciation of \"gif\" complete. 100% of respondents say soft-g. In unrelated news, terminations doubled yesterday.',NULL),(7,'##CIT-V-TR##\'s show trial and erasure due for 80:00 SCT todaycycle!',4),(8,'Painkiller RadicalMankey classified treasonous due to unintended side effects.',4),(9,'New CPU manager sends 800 staff to the re-employment office.',1),(10,'Alternative Power Firm Strike-Me-Not reports a 3000% increase in power generation this daycycle!',7),(11,'The new Petbot 214 released today, sells out in minutes!',8),(12,'CPU reports the air per person index has risen 4% in the last month!',9),(13,'Sold-out UltraSuperMegaBowl funball game between USS Patriots and MBB Ballers live todaycycle on Channel 5!',10),(14,'Power forecast: rolling brownouts down by 28%, blackouts down 41%.',6),(15,'A new reactor to be completed today! Citizens rejoice!',11),(16,'Serve your Complex! Battle the Commie Threat! Bonus Hot Fun! Enlist Today!',12),(17,'Friendly Ollie-U saves sector YRU yet again! Citizens Rejoice!',NULL),(18,'ChocolateLyke ration for INFRAREDs increased yet again! ##CIT-I-PL##: \"Once again, the war on production is won!\"',NULL),(19,'Frank-U destroys fifteen communist strongholds, says end in sight for war on terror.',NULL),(20,'Terrorist bomb unlawfully terminates 180 clones, IntSec urges terrorists to hand themselves over before it becomes erasable.',NULL),(21,'Water rations to be decreased 30% due to increased B3 usage.',13),(22,'New dirt indexer bots promise faster hygiene index response times!',14),(23,'Secret Society activity up 12%. Register your society affiliation today for bonus hot fun!',NULL),(24,'Hero Of Our Complex ##CIT-B-1## completes fourth successful tour of duty!',15),(25,'Bright Vision Reducation Centres announce the opening of a new location due to increasing needs!',16),(26,'Rolling blackout centred on RIO sector hit early this morning, productivity hit of 3.81%.',17),(27,'New proposal being investigated to outlaw sleep to raise efficiency.',NULL),(28,'##ZON## funball team under investigation for poor performance. ##CIT-I-IS##: \"No-one but a commie could perform so badly\".',NULL),(29,'SIB Splasheroony fun park boasts record attendence!',NULL),(30,'Friend Computer processing delay set to decrease in the next few weekcycles.',19),(31,'The Hatred Day Parade, live tonight at 8000SCT!',21),(32,'Vicky-B credited with her eleventh HOOC award!',22),(33,'Alternative Power Firm Strike-Me-Not reports a 3000% increase in power generation this daycycle!',23),(34,'Underplex reclaim up 4.8% yearcycle on yearcycle.',NULL),(35,'Bouncy Bubble Beverage increases artificial sweetener content to 81.4%',NULL),(36,'FizzWizz updates its recipe, now with extra Fizz!',NULL),(37,'Defence system update goes smoothly, minimal wrongful terminations.',NULL),(38,'Do we really need so much paperwork? Of course we do! Report at 11.',26),(39,'Alternative Power Firm Strike-Me-Not reports a 3000% increase in power generation this daycycle!',27),(40,'News of Danny-V-SNH\'s glorious return are, as usual, treason until proven otherwise.',28),(41,'A lonely citizen is a treasonous citizen!',29),(42,'Political orthodoxy down 2 points weekcycle on weekcycle.',NULL),(43,'c-bay breaks into ULTRAVIOLET market, announces record profits.',NULL),(44,'Cost of toilet paper rises 5000%, bidets installed in all public restroom blocks.',NULL),(45,'Hygiene standard updated to specify a two sock minimum.',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource`
--

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` VALUES (1,'Bubble Beverage Funball Stadium','LOC'),(2,'Compnode A','LOC'),(3,'Nuclear Waste','RES'),(4,'Left Boots','RES'),(5,'FunFoods MegaPark','LOC'),(6,'Abandoned Nuclear Plant','LOC'),(7,'New Nuclear Plant','LOC'),(8,'WasteRenewal Toilet Complex','LOC'),(9,'Hot Fun','RES'),(10,'Drinking Water','RES'),(11,'Plutonium','RES'),(12,'Soy','RES'),(13,'Copper Wire','RES'),(14,'##ZON## Splasherooni Fun Park','LOC'),(15,'Compnode B','LOC'),(16,'Compnode C','LOC'),(17,'Compnode D','LOC'),(18,'Compnode E','LOC'),(19,'Compnode F','LOC'),(20,'Compnode G','LOC'),(21,'Compnode H','LOC'),(22,'B3 Funball Stadium','LOC'),(23,'IR Cafeteria ##SUB-L1##','LOC'),(24,'R Cafeteria ##SUB-L2##','LOC'),(25,'O Cafeteria ##SUB-L3##','LOC'),(26,'IR EAP AC ##SUB-L4##','LOC'),(27,'R EAP AC ##SUB-L5##','LOC'),(28,'O EAP AC ##SUB-L6##','LOC'),(29,'Sewer Pipe ##SUB-L7##','LOC');
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
INSERT INTO `sg_skill` VALUES (1,11),(1,16),(1,17),(1,18),(1,20),(1,21),(1,22),(1,23),(1,31),(1,33),(1,49),(2,1),(2,2),(2,5),(2,7),(2,13),(2,17),(2,34),(2,35),(2,36),(2,37),(2,41),(2,45),(2,47),(3,2),(3,3),(3,5),(3,6),(3,7),(3,10),(3,18),(3,25),(3,27),(3,39),(3,44),(3,46),(3,47),(4,6),(4,15),(4,26),(4,29),(4,31),(4,36),(4,38),(4,39),(4,41),(4,42),(4,46),(5,3),(5,12),(5,19),(5,20),(5,24),(5,25),(5,26),(5,27),(5,29),(5,30),(5,32),(5,33),(5,42),(5,43),(6,8),(6,9),(6,14),(6,15),(6,24),(6,25),(6,27),(6,28),(6,35),(6,38),(6,40),(7,11),(7,16),(7,19),(7,23),(7,28),(7,30),(7,32),(7,34),(7,37),(7,40),(7,42),(7,43),(7,44),(7,45),(8,1),(8,4),(8,8),(8,9),(8,10),(8,11),(8,12),(8,14),(8,18),(8,21),(8,22),(8,44),(8,49),(9,48);
/*!40000 ALTER TABLE `sg_skill` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sgm`
--

LOCK TABLES `sgm` WRITE;
/*!40000 ALTER TABLE `sgm` DISABLE KEYS */;
INSERT INTO `sgm` VALUES (1,'The grunts are getting restless. Make sure at least two infantry units are given something to do.',1,NULL),(2,'The budget for the new Mark V Warbot is tight. We need an R&D team and half a ton of reactor fuel to attempt to turn it on.',1,NULL),(3,'Efficiency is down 15.7%. Find a way to increase it. It doesn\'t matter what it is, it\'ll all balance out in the end.',2,NULL),(4,'Housing in ##ZON## is oversubscribed by 21%. Either build more housing or reduce demand.',3,NULL),(5,'We\'ve overproduced on ##RES-1## by six months. Find a use for the excess ##RES-1##.',4,NULL),(6,'We have a machine. We don\'t know what it does. Turning it on needs unfettered access to a reactor. We want to turn it on.',7,NULL),(7,'Our contract to rebuild sector ZQD is behind schedule. We need another couple of construction teams but don\'t let anyone know we\'re using them to finish ZQD.',5,NULL),(8,'We\'ve got a huge excess of spent fuel rods. Find a place to put them, please?',6,NULL),(9,'We\'re having trouble storing all the nukes. Either use a few or find us a new storage area.',1,NULL),(10,'The jackbooted idiots are blaming us for no termination vouchers being dispatched, make sure the blame falls on them and not on us.',2,1),(11,'Those idiots in CPU haven\'t sent us any termination vouchers for the last daycycle. Find out why and make sure they take the blame.',8,1),(12,'We want this contract but haven\'t the time to prepare...make sure Power Services\' tubes stop working and get us that contract.',5,2),(13,'The money we could get from this contract, not to mention the extra housing space. Do whatever it takes to get us the contract.',3,2),(14,'It\'s not our fault there were so many problems with the Transtube timetables! Make sure we keep this contract, or we\'re all toast.',6,2),(15,'Power Services have never given our troops free rides. Make sure whoever gets the contract promises free rides to Armed Forces grunts.',1,2),(16,'We were ambushed while getting the weapon from R&D. Make sure we don\'t get embarrased over this.',1,3),(17,'We filed the RDP ##SUB## paperwork as complete before the exchange was ambushed. Make sure the exchange is completed before the end of the daycycle.',2,3),(18,'There\'s a small problem we\'ve located with RDP ##SUB##, once it gets fired it can\'t be turned off. Destroy the device and make sure no-one else finds out about this little flaw.',7,3),(19,'##CIT-V-TR##\'s erasure has to go off without a hitch. The Big C\'s twitchy enough as it is.',3,4),(20,'Due to constraints, we can\'t actually remove ##CIT-V-TR##\'s name from the databanks until after their erasure, or it\'ll crash the TTS. Make sure it doesn\'t crash.',2,4),(21,'The Big C seems to have a sore spot for ##CIT-V-TR##, and the TTS isn\'t tracking why. Make sure the trial goes off fine, but delay the erasure so we can use \'em as bait.',8,4),(22,'##CIT-V-TR##\'s erasure is due for 80:00, but we want it pushed back to 90:00 for...reasons. We also want their body afterwards, get it to us.',7,4),(23,'RDP ##SUB## is one of our best projects, and it actually works! Get it standardized sector-wide.',7,5),(24,'If RDP ##SUB## gets standardised we\'ll lose at least half our revenue. Make sure it doesn\'t make the cut.',4,5),(25,'RDP ##SUB## getting standardised means we can sell space in our reactors for growth! Get in standardised!',6,5),(26,'The nitwits at Power Services want RDP ##SUB## standardised for some reason. Make sure it doesn\'t happen.',5,5),(27,'If RDP ##SUB## is standardised, we won\'t be able to easily push drugs to the population. Stop it at all costs',3,5),(28,'If R&D\'s new gene is standardised, we can classify most of the sector as unregistered mutants and push our rates through the roof! Get it done.',8,5),(29,'Current projections of RDP ##SUB## show an efficiency increase of 180%! Get it standardised!',2,5),(33,'This attack must be from Beta Complex. We must march upon Beta Complex immediately!',1,7),(34,'This attack will lead to war if we don\'t act soon. Prevent a war with Beta Complex breaking out until we find the cause.',8,7),(35,'Our grid is almost overloaded! I can\'t believe I\'m saying this...but we need clones to use more power immediately!',6,7),(36,'There\'s a sudden drop of happiness in multiple subsectors. We need it cleared up pronto.',3,7),(37,'The Petbot 214 has a bit of a flaw, but make sure it doesn\'t get banned. It\'s set to have huge sales!',4,8),(38,'The Petbot 214 is a hit, but could use a few new features. Make sure the next production run encourages people to watch \"Late Night with Conan-O-BRN\"',3,8),(39,'The release of the Petbot 214 is ruining our metrics. Get it removed from sale however you can.',2,8),(40,'The new Petbot requires huge amounts of power, and needs to be rechared every two hours. The grid can\'t handle it, either get us more power or remove the petbots.',6,8),(41,'The concentration of petbots is at an exact level required for data gathering. Don\'t have them recalled, but don\'t let the next production run complete.',7,8),(42,'The truckbot with cold fun syrup crashed in ##SUB##, make sure we don\'t take the blame or have to pay any damages.',4,9),(43,'Tunnel ##SUB## was a clear run on the map! Make sure CPU wear the blame for the Cold Fun syrup truckbot crash.',5,9),(44,'The maps for the transtubes were supposed to be updated last monthcycle but weren\'t. Cover us while we update the maps and records to show the update happened as planned.',2,9),(45,'The data shows a terrorist attack is supposed to hit today. Identify the threat, and make sure it\'s stopped.',8,10),(46,'Finally, a chance to embarrass those idiots at IntSec. Find what this supposed terrorist attack is supposed to be, and when it happens make sure IntSec take the blame.',1,10),(47,'Make sure the UltraSuperMegaBowl goes off without a hitch - broadcasting is on a 10 minute delay to the complex, it must not be interrupted.',3,10),(48,'Our techs have nicknamed today the MegaBowel, for obvious reasons. If there\'s anything in the pipes there may be problems. If it happens, don\'t let the blame fall on us.',5,10),(49,'Project ##SUB## is going ahead today, make sure it\'s a success!',7,6),(50,'##SUB## is likely to cause us to lose the need of heaps of reactors! Don\'t let it succeed!',6,6),(51,'The reactor isn\'t actually ready to go yet, we need engineers sent down before it gets switched on. Whatever happens, make sure HPD don\'t get any footage of the unfinished reactor.',6,11),(52,'We\'ve been refused access to the opening ceremony of the new reactor, but you should have no problem. Get our cameras into the opening ceremony and find out what\'s going on.',3,11),(53,'We\'ve had this huge stock of radiation badges for over a yearcycle. Sell one to every citizen of the sector.',4,11),(54,'We just got word of a huge invasion force! Get at least four of our units into the fight!',1,12),(55,'We\'ve finally got a solid war coming. Get heaps of combat footage of our troops winning for the usual propaganda purposes.',3,12),(56,'We\'re going to war. Prevent any major damage to the sector, or make sure the other service groups get blamed for it.',5,12),(57,'We don\'t actually have enough laser barrels to go to war with. Either make more fast or find alterative ammo sources.',4,12),(58,'The flooding water may be fresh water! If it is, store it somewhere so we can bottle it and sell it for megacredits!',4,13),(59,'Mission quota is down. Send at least 5 troubleshooter teams on missions before the end of the daycycle.',9,NULL),(60,'That entire flooded area was almost condemned. Let it get destroyed so we can get a juicy demolition contract out of it.',5,13),(61,'This flooding is bad for morale. Implement a couple of initiatives to make the proles feel good about the flooding.',3,13),(62,'We have reliable information one of the High Programmers in the room is a traitor. Find them and have them terminated before end of daycycle.',8,NULL),(63,'There\'s been a sudden drop in the hygiene index, whatever the cause, make sure the blame is on tech services.',2,14),(64,'There\'s been a sudden loss of hygiene in the complex, our cleaners are still working fine. Make the blame land squarely on R&D.',5,14),(65,'We deployed a few dirt indexer bots last weekcycle and they work great! Make sure any blame for the loss of sector hygiene falls on CPU and not us.',7,14),(66,'The Computer\'s convinced that one of the other High Programmers is a traitor. If you don\'t prove that one is a traitor it\'ll blow a fuse.',2,NULL),(67,'We need more data on the effects of a nuclear meltdown, so we can test our new nuclear-proof underwear.',7,NULL),(68,'We need to close transtube ##ZON##-M4 for six months. Clear it with the other High Programmers and make sure they understand this will disrupt all transport in the sector.',6,NULL),(69,'##CIT-B-1## is one of our most decorated Vulture Troopers. Don\'t let treason be the public reason for his erasure.',1,15),(70,'##CIT-B-1## is an embarrassment to the complex, show him as an example that anyone can be a commie.',8,15),(71,'We need more exposure, ratings are falling. Get a high-profile citizen to go on at least two troubleshooter missions.',9,15),(72,'If ##CIT-B-1## is publicly denounced as treasonous it will be horrible for morale. Don\'t let that happen.',3,15),(73,'We\'re lagging behind on our High Profile Mutation Tracking. Find ##CIT-B-1##\'s mutation and have it registered before his erasure.',2,15),(74,'214-##SUB## is a commie weapon of mass destruction! Secure it and give it to us so we can study it!',7,16),(75,'Oh Computer, 214-##SUB## again? Make sure it\'s properly secured by us this time.',8,16),(76,'Everyone seems to be after 214-##SUB##. Make sure it\'s secured by us so we can replicate it and sell it to the other groups for massive profits!',4,16),(77,'Why are the other groups after 214-##SUB## anyway? Get it for us so we can sell it to the highest bidder!',6,16),(78,'Those Jackbooted idiots can\'t even keep one item under containment? Get 214-##SUB## for us, and we\'ll secure it properly this time.',1,16),(79,'MuchoMeat is one of our most profitable producers, don\'t let any serious damage come to it.',4,17),(80,'MuchoMeat is out-performing profit expectations. Bring it back within expectations.',2,17),(81,'MuchoMeat is full of expensive machinery, if things were to get broken there it would be extremely profitable for our techs. Make it profitable for our techs.',5,17),(82,'MuchoMeat uses far too much power. Halve their power consumption by the end of the daycycle.',6,17),(83,'##SUB## only has enough power for two firings, but the second will destroy the device. Stop people from using it more than once!',7,18),(84,'We\'re low on water. Use ##SUB## to pull water in from Jupiter\'s moon.',4,18),(85,'The enemy complex on the moon has always held a sword over our heads...until today! Use ##SUB## to destroy that complex!',1,18),(86,'There\'s an entrenched PURGE cell hiding deep underground, use ##SUB## to penetrate the fortress and eliminate the cell.',8,18),(87,'Something\'s going on with the computer\'s circuits...make sure FC doesn\'t terminate any of the HPs this daycycle so we can track down the problem.',5,19),(88,'The terrorist attack appears to be orchestrated by an Ultraviolet. Uncover this UV and have them terminated.',8,20),(89,'We have the Hatred Day Parade running tonight, make sure it\'s not interrupted.',3,21),(90,'The Parade usually brings out the commie rats. The arrest quota for today is 400% of normal, make sure we reach it.',8,21),(91,'PS aren\'t giving us the power we need for the parade lighting. Get it for us.',5,21),(92,'Our reactors are maxing out, reduce usage before one goes critical.',6,21),(93,'Vicky-B has an appearance later today, make sure she attends it before her disappearance.',1,22),(94,'The Big C has deterimined Vicky-B to be a traitor. Get her to us before her public appearance later this daycycle before she causes damage.',8,22),(95,'We still have tons of Vicky-B merchandise, make sure she is still a hero when this is all over.',4,22),(96,'Strike-Me-Not has suddenly become profitable, make sure it doesn\'t get shut down and stays under our control.',7,23),(97,'Strike-Me-Not is surpassing expectations. Make sure it gets shut down or gets transferred to us.',6,23),(98,'Strike-Me-Not is suddenly exceeding expectations, a sure sign of treason. Shut it down and have the staff interrogated.',8,23),(99,'When the exercise is over, make sure IntSec look incompetent while we look fantastic.',1,24),(100,'There\'ll be an invasion exercise today. Embarass the AF while making us look good.',8,24),(101,'We need to show the complex working together, get footage of at least 4 service groups working together to defend the complex.',3,24),(102,'We should take back ##CIT-V## by force! We must pre-emptively attack! Of course, clear it with the rest of the UVs first.',1,25),(103,'We should oversee the ##CIT-V## deal. Set up the deal, and make sure we get them back unharmed.',8,25),(104,'We need some good graces with FC, make sure a Troubleshooter Team oversees the transfer of ##CIT-V##.',9,25),(105,'We need to show R&D is still relevant in the fight against Communism, be sure our projects take a leading role in the upcoming exercise.',7,24),(106,'A staged commie attack means lots of repair work. Make sure all repair work is blamed on other SG\'s imcompetence, not on the exercise, or we\'ll have to pay the bill.',5,24),(107,'We should use this opportunity to move all our records to electronic sources. Convince FC to go full electronic!',2,26),(108,'CPU is going to try and make records go all electronic, stop it from happening, the power cost would be impossible.',6,26),(109,'Find a different medium than paper or electronic to put records on, and convince FC to use it to store all records. We\'ll make millions!',4,26),(110,'PLC is going to go silly over some new record storage material again, make sure it doesn\'t get implemented. Paper and Electronic are all we need.',3,26),(111,'We\'ve been trying to push for quantum storage for some time now, here\'s our chance. Convince FC to move all records to the new format.',7,26),(112,'R&D is going to push for quantum storage again, ever tried to convict someone on evidence they might be guilty? Yeah. Stop it from happening.',8,26),(113,'This attack must be from Beta Complex. We must march upon Beta Complex immediately!',1,27),(114,'This attack will lead to war if we don\'t act soon. Prevent a war with Beta Complex breaking out until we find the cause.',8,27),(115,'Danny-V-SNH was broken out of custody before his debriefing interrogation. Get them back and find the culprit.',8,28),(116,'Danny-V-SNH was a popular figure before they sent on their mission. Get them in the spotlight and keep them alive.',3,28),(117,'We still have a warehouse of Danny-V-SNH memorabillia. Bring them back into the public eye and flog off all this old stuff.',4,28),(118,'The sector is overpopulated. This is our chance to lower the Treason Termination Minimum by 15%. Clear it with the other HPs.',8,29),(119,'Cloning times are already at the maximum, make sure clone deaths are at a minimum this daycycle.',5,29),(120,'HPD&MC are going to use the overpopulation as an excuse to take our land. Make sure we don\'t lose any land to HPD todaycycle.',1,29),(121,'HPD&MC are going to use the overpopulation as an excuse to take our land. Make sure we don\'t lose any land to HPD todaycycle.',4,29),(122,'HPD&MC are going to use the overpopulation as an excuse to take our land. Make sure we don\'t lose any land to HPD todaycycle.',7,29),(123,'If we\'re going to fix the overpopulation, we need more land for housing. We need two of either AF bases, PLC warehouses, or R&D labs.',3,29);
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
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skills`
--

LOCK TABLES `skills` WRITE;
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
INSERT INTO `skills` VALUES (1,'Assessment','M'),(2,'Co-Ordination','M'),(3,'Hygiene','M'),(4,'Interrogation','M'),(5,'Paperwork','M'),(6,'Thought Control','M'),(7,'Thought Survey','M'),(8,'Covert Operations','Su'),(9,'Infiltration','Su'),(10,'Investigation','Su'),(11,'Security Systems','Su'),(12,'Surveillance','Su'),(13,'Cleanup','Su'),(14,'Sabotage','Su'),(15,'Black Marketeering','Su'),(16,'Assault','V'),(17,'Command','V'),(18,'Crowd Control','V'),(19,'Demolition','V'),(20,'Outdoor Ops','V'),(21,'Defence','V'),(22,'Wetwork','V'),(23,'Total War','V'),(24,'Bot Engineering','H'),(25,'Construction','H'),(26,'Chemical Eng','H'),(27,'Habitat Eng','H'),(28,'Nuclear Eng','H'),(29,'Production','H'),(30,'Weird Science','H'),(31,'Transport','H'),(32,'Bot Programming','So'),(33,'Communications','So'),(34,'Computer Security','So'),(35,'Data Retrieval','So'),(36,'Financial Systems','So'),(37,'Hacking','So'),(38,'Logistics','So'),(39,'Media Manipulation','So'),(40,'Biosciences','W'),(41,'Catering','W'),(42,'Cloning','W'),(43,'Medical','W'),(44,'Mutant Studies','W'),(45,'Outdoor Studies','W'),(46,'Pharmatherapy','W'),(47,'Sub. Messaging','W'),(48,'Troubleshooting','O'),(49,'Intimidation','M'),(50,'Must Not Fail','O'),(51,'Super Armoured','O'),(52,'Middle Managers','O'),(53,'Doubles Standing','O'),(54,'Rapid Response','O'),(55,'Vault Delvers','O'),(56,'Minion \'Enhancement\'','O'),(57,'Point of Contact','O'),(58,'Bigger Guns','O'),(59,'Propaganda','O'),(60,'Cyborging','O'),(61,'Disruption','O'),(62,'Procurement','O'),(63,'Mystic Weirdness','O'),(64,'Gadgeteering','O'),(65,'Old Stuff','O'),(66,'Running','O'),(67,'Salvage','O'),(68,'One Use, Multiples','O');
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
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ssm`
--

LOCK TABLES `ssm` WRITE;
/*!40000 ALTER TABLE `ssm` DISABLE KEYS */;
INSERT INTO `ssm` VALUES (1,1,NULL,'The muties have are getting too much peace, come down hard on mutants!'),(2,4,NULL,'Comrade! Pleasink to be spreadink Propaganda to Burgoise Complex!'),(3,5,NULL,'We\'re running out of washed caffiene. Can you deliver a truckload to ##LOC-1## for us?'),(4,6,NULL,'We have a surplus of cyborg parts, maim a good portion of the population so we can sell \'em off.'),(5,7,NULL,'Duuuude, we had a massive party last night and don\'t want to deal with the boys in blue... can you move IntSec out of the low clearance areas?'),(6,8,NULL,'We need more faith based initiatives...replace the morning anthem with a Hymn to our lord and savior, Friend Computer!'),(7,10,NULL,'There\'s too many bots around. Especially petbots, you can\'t sleep with those things around. Remove all petbots from the sector.'),(8,11,NULL,'Petbots are excellent for showing man\'s mastery over bots. Increase the number of petbots in the sector.'),(9,12,NULL,'Duuuuude, we had a massive rave last night, and we\'re out of uppers. Can you get us a transbotload of washed caffeine?'),(10,13,NULL,'We\'ve got a device we want to turn on but we don\'t know what it does. Can you get us unfettered access to a nuclear reactor to try?'),(11,15,NULL,'High value targets are currently under too much scrutiny, redirect IntSec to the lower-clearance areas.'),(12,16,NULL,'There\'s not enough pre-reckoning knowledge: get us onto the set of Celebrity Weekcycle and we\'ll do the rest.'),(13,19,NULL,'We need to tempt more clones with the wonders of the outdoors... can you get some trees into ##LOC-1##?'),(14,20,NULL,'La! The leylines intersect there, at ##LOC-1##! Build us a new temple there!'),(15,4,3,'Comrade! Ve are hearink rumours of superveapon gone missing! Recover and deliver veapon to us to aid great Lenin\'s cause.'),(16,8,3,'A heretical splinter group stole an experimental doomsday device and are hoarding it. Recover it for us so we can sanctify it before returning it.'),(17,9,NULL,'Yeah see, we got dis huge pile of defunct laser barrels. By huge, I\'s mean four million. Find us a buyer, and make sure IntSec\'s away.'),(18,2,NULL,'We\'re running low on armaments against the Commie threat! We need twenty tacnukes to beef up the arsenal by the end of the daycycle.'),(19,3,NULL,'We need more people to use our services. Kill off a high-profile citizen publicly to scare the masses.'),(20,17,NULL,'We\'ve got a secret escape route in ##LOC-1##, but there was a cave in. Repair it please.'),(21,21,NULL,'Comrade! We\'re going to hold a demonstration in the ##LOC-1##. Please make sure they don\'t run over us with tanks!'),(22,5,4,'The Big C\'s kinda...twitchy about the erasure for today, more so than usual. Get us into the crisis room so we can find out why.'),(23,3,4,'##CIT-V-TR## is a natural-born, no-one knows! Get us their spleen so we can use his genes.'),(24,20,4,'La! ##CIT-V-TR## is the sacrifice we need! Get them to us sedated! We\'ll be waiting in the ##LOC-SOC##.'),(25,17,4,'##CIT-V-TR## is a client of ours. Get them to us so we can get \'em out, we\'ll be at the ##LOC-RUN##.'),(26,12,4,'Whoa man, ##CIT-V-TR## paid well for a batch of RadicalMankey, but we can\'t find it, he must have eaten it. Get their stomach to use so we can pump the drugs back out.'),(27,7,1,'Whoa, the man\'s not keeping us down! Get us a pallet of B3 and a crate of WhiskeyLike to ##LOC-P## so we can throw a massive party!'),(28,12,1,'Whoa man! Like, we got space to breathe! Get us a load of Videoland to ##LOC-P## so we can rave, man!'),(29,3,1,'We haven\'t had any work all daycycle, it\'s costing us credits! Make a whole heap of clones die so we can get back to work.'),(30,2,1,'The commies have infiltrated IntSec! Get us five tacnukes so we can flush out the commie menace! We\'ll be waiting at the ##LOC-F##.'),(31,4,1,'Comrade! Ze IntSec veaklings are awaiting a blastink! Deliver us two hundred laser barrels so we can take ze fight to zem! We\'ll be waitink at ze ##LOC-F##'),(32,1,5,'Why are people tinkering with the human genome? Terminate ##CIT-G-SG##, he must be a filthy mutant.'),(33,3,5,'Why haven\'t we heard of ##SUB## before? Get us a copy of the data.'),(34,13,5,'##SUB## is actually working? Get us a copy of the data to look at'),(35,14,5,'The next step in human evolution is here! Get ##SUB## standardised.'),(36,9,5,'See here, dis ##SUB## project? Destroy the data, so\'s we don\'t lose our customer base later.'),(37,19,5,'##CIT-G-SG## is one of ours. Make sure his project succeeds!'),(40,2,7,'The Commies are finally attacking! Equip all clones with weapons to fight the impending Commie Horde!'),(41,4,7,'Comrade! Is not beink our superveapon! You be pinning blame on Beta Complex, yes?'),(42,5,7,'Yo dude, our electronics are going haywire! Whatever it is, make it stop.'),(43,13,7,'This is perfect time for some of our reanimation tests! Get us a safe way to the outdoors so we can try our latest experiment!'),(44,8,7,'The endtime prophecy has begun! Now we must part the RED sea! Destroy the Commie scum in Beta Complex!'),(45,20,7,'La! The old ones come to greet us! We must sacrifice a High Programmer to the elder gods to open the gate! Get us a suitable sacrifice!'),(46,19,7,'Outside is beautiful right now! Broadcast footage of it to the entire complex!'),(47,6,8,'The new petbot was influenced by us, get the next production run pushed through this daycycle.'),(48,4,8,'Comrade! Dis new petbot is symbol of capitalist excess! Remove from population!'),(49,5,8,'Our contract on the petbot is done, order the current stock to do a factory reset so they pay us again.'),(50,10,8,'These new bots are abominations! Reveal them for what they are and recall the whole product line!'),(51,13,8,'These new petbots so cute! Make them mandatory for all REDs and above!'),(52,7,9,'Cold Fun Syrup? Dude, get us a boxful! We can do some amazing pranks with it!'),(53,4,9,'Comrade! Ve must to be gettink hands on syrup of Cold Fun!'),(54,15,9,'Get us a good amount of that Cold Fun Syrup, we\'ll finally be able to make a bomb big enough to take down the Computer.'),(55,9,9,'Look see, gets us a box of Cold Fun Syrup and we\'ll cut youse in on de profits.'),(56,21,10,'Comrade! UltraSuperMegaBowl is capitalist excess! Cause an evacuation of the stadium!'),(57,15,10,'We have a bomb hidden under the UltraSuperMegaBowl, but security is too tight for us to activate it. Get us an alternate way in.'),(58,9,10,'Look see, we know there\'s something juicy under the UltraSuperMegaBowl. Get it for us and we\'ll cut you in on de profits.'),(59,14,NULL,'We\'re needing to find more of our kind, add more mutagenics to the water supply.'),(60,18,NULL,'We\'re needing building materials, specifically 5000 tons concrete. Deliver it to ##LOC-1## for us to pick up.'),(61,4,11,'Comrade! New reactor helping bourgoise cause! Must be destroyink!'),(62,21,11,'Comrade! New reactor helping bourgoise cause! Must be destroyink!'),(63,15,11,'The new reactor is a prime target. Destroy before the end of the daycycle.'),(64,1,11,'A new reactor means more mutants. Implement a new anti-mutant policy before the end of the daycycle!'),(65,14,11,'Get a crowd of people near the new reactor to celebrate its opening.'),(66,2,12,'We heard of the Commie threat! Send us some bigger guns so we can prepare for the final defence!'),(67,3,12,'We\'re gonna need some more cloning tanks. Appropriate some of tech services\' tanks for us.'),(68,18,12,'This is our chance. Get us a way to the front lines, and we\'ll end this war once and for all.'),(69,20,13,'La! The ritual is almost complete! Delay the computer\'s lackeys long enough so we may summon the old ones!'),(70,12,13,'We kinda lost a year\'s supply of hallucinogenics in the water supply. Can you get it back for us? '),(71,8,13,'We\'ve heard rumblings of heretical cultists building a temple in the sublevels. Destroy it and escort us there to cleanse it!'),(72,18,13,'Some idiots stole our building supplies and built a temple in the sublevels. Can you clear them out, but leave the place intact for us to strip?'),(73,10,14,'These new dirt indexer bots are terrible murderers! Destroy all of them!'),(74,6,14,'The dirt indexer bots are the next wave of bot freedom fighters. Get more out in the sector!'),(75,11,14,'One of the dirt indexer bots murdered a clone! Get footage of this and broadcast it to the complex!'),(76,15,15,'##CIT-B-1## wasn\'t a commie, they were one of ours. Get one of their clones to disappear into our hands before their erasure.'),(77,11,15,'##CIT-B-1## is due to be erased? They were one of our best clones! Get one of their clones to disappear before his erasure.'),(78,3,15,'We could do wonderful things with ##CIT-B-1##, get them delivered comatose to us at ##LOC-1##'),(79,2,15,'##CIT-B-1## can\'t be a traitor, they\'ve killed far too many commies. We\'ve a shipment of weaponry for him, pick it up at ##LOC-1## and deliver it to them.'),(80,5,15,'We need ##CIT-B-1##\'s tongue for identity purposes. Deliver it to us at ##CIT-B-1##.'),(81,8,15,'##CIT-B-1## is a commie? Their soul just needs cleansing, deliver them to us so we may do so. We\'ll be waiting at ##LOC-1##'),(82,4,15,'##CIT-B-1## is comrade? Vhy ve not know? Beink savink them and deliverink to us, yes? Ve vait at ##LOC-1##'),(83,21,15,'##CIT-B-1## is comrade? Must be very skilled. You be savink them and deliver to ##LOC-1## for us, yes?'),(84,4,16,'Comrade! My book finally returns! Re-secure ##SUB## for my coll...for future generations of Communists!'),(85,2,16,'This ##SUB## thing is a commie weapon! Broadcast its destruction to the complex.'),(86,11,16,'There\'s a wonderful item, ##SUB##, which will assist in the coming revolution. Capture it for us.'),(87,1,16,'There is a mutie killing device, ##SUB##, get it for us.'),(88,14,16,'We\'ve recently found ##SUB## is a major mutagenic, secure it for us.'),(89,7,16,'Whoa man, everyone seems to be after ##SUB##. Grab it for us and we\'ll pull the greatest stunt anyone has ever seen!'),(90,9,16,'Look see, there\'s dis really expensive ##SUB## running around, get it for us and we\'ll sell it to the highest bidder, capiche?'),(91,3,17,'Have the staff of MuchoMeat terminated, so their clones will buy our insurance this time.'),(92,4,17,'Comrade! MuchoMeat is symbol of capitalist excess! You are to be putting poison in MuchoMeat foodstuffs, yes?'),(93,11,17,'We require a crate of MuchoMeat\'s products to help the revolution. Deliver it to us at ##LOC-1##'),(94,6,17,'MuchoMeat doesn\'t use enough bots. Execute the lower-level staff and have them replaced with bots instead.'),(95,10,17,'MuchoMeat uses too many bots. Destroy all the bots and replace them with humans.'),(96,19,17,'MuchoMeat grows things from the outdoors, I think they\'re called Pugs, in vats. Get one of them for us, we\'ll be at ##LOC-1##.'),(97,13,18,'Get us the plans to ##SUB## so we can make our own device!'),(98,17,18,'Get us the plans to ##SUB## so we can make our own!'),(99,19,18,'Get us the plans to ##SUB## so we can make our own exits.'),(100,20,18,'La! Get us the plans to ##SUB## so we can open a true gate!'),(101,9,18,'Lookee here, get us deez here plans to ##SUB##, and we sells dem to the highest bidder, capiche?'),(102,8,19,'We\'ve noticed a drop in our lord\'s response times. Get us into one of the CompNodes to sanctify its circuites.'),(103,13,19,'We\'ve had advance warning of more compnodes, get one of our teams into a partially constructed CompNode.'),(104,15,20,'We\'re taking out ##LOC-1##, clear the IntSec goons out of there.'),(105,11,20,'We\'re bombing ##LOC-2##, remove the resistance for us.'),(106,4,20,'Comrade! Ve are bomink ##LOC-3##. Pleasink to be removink resistance.'),(107,8,20,'The heathens are coming for our temple near ##LOC-1##! Stop them and don\'t let our temple get discovered!'),(108,6,20,'We\'ve got a bot shop near ##LOC-2## that someone\'s trying to bomb. Stop them and keep our shop hidden.'),(109,2,20,'The commies are coming for our bunker at ##LOC-3##, stop them and keep our bunker hidden!'),(110,11,21,'Why do Commies get all the attention? Get us access to the Computer float so we can attract more recruits.'),(111,4,21,'Ve are hidink todaycycle, keep ze attention off us and get us some food to ##LOC-1##.'),(112,15,21,'We want to crash the party, get us access to the Vulture Craft Float for the parade.'),(113,7,21,'Dude, the parade is always a great time for pranks. Get us access to the speaker\'s stand!'),(114,2,21,'We\'re worried about the Commie threat. Get us access to the Vulture Craft Float for the parade so we can fight the commies when they strike!'),(115,8,21,'We wish to join our lord in his crusade against the Commies! Get us access to the Computer float!'),(116,15,22,'Vicky-B needs to disappear, deliver her to ##LOC-1## before the end of the daycycle.'),(117,11,22,'Vicky-B apparently needs to be gone? Get her to ##LOC-2## so we can get her to safety.'),(118,17,22,'We owe Vicky-B a favour, deliver her to ##LOC-3## and we\'ll get her out of there.'),(119,3,22,'We could profit from Vicky-B, get us access to her before she disappears so we can sell her a policy.'),(120,13,23,'Get us in to Strike-Me-Not, we want to check out the hardware!'),(121,18,23,'We need Strike-Me-Not\'s project for the new complex, deliver it to us.'),(122,2,23,'The Commies are attacking! Deliver us one thousand laser barrels to prepare for the attack!'),(123,14,23,'We have need of Strike-Me-Not, get our team in there, remove the security.'),(124,2,24,'When the exercise begins, make sure we\'re in the defending force.'),(125,4,24,'Comrade! Vhen ze exercise begins, making to be sure vhe are defending, yes?'),(126,1,24,'When the exercise begins, make sure we\'re a member of the defending force.'),(127,13,24,'When the exercise begins, make sure we\'re in the defending force.'),(128,8,25,'##CIT-V## must not come back alive. Terminate them before they re-enter the complex.'),(129,9,25,'##CIT-V## must not come back alive. Terminate them before they re-enter the complex.'),(130,16,25,'##CIT-V## must not come back alive. Terminate them before they re-enter the complex.'),(131,19,25,'##CIT-V## must not come back alive. Terminate them before they re-enter the complex.'),(132,12,25,'##CIT-V## must not come back alive. Terminate them before they re-enter the complex.'),(133,5,26,'Remove security from the Archives and get us in there, we have work to do.'),(134,9,26,'Remove security from the Archives and get us in there, we have work to do.'),(135,11,26,'Remove security from the Archives and get us in there, we have work to do.'),(136,13,26,'Remove security from the Archives and get us in there, we have work to do.'),(137,20,26,'Remove security from the Archives and get us in there, we have work to do.'),(138,13,27,'Get us in to Strike-Me-Not, we want to check out the hardware!'),(139,18,27,'We need Strike-Me-Not\'s project for the new complex, deliver it to us.'),(140,2,27,'The Commies are attacking! Deliver us one thousand laser barrels to prepare for the attack!'),(141,14,27,'We have need of Strike-Me-Not, get our team in there, remove the security.'),(142,1,28,'Danny-V-SNH has some nerve showing up again. Make sure they don\'t last the daycycle.'),(143,2,28,'Danny-V-SNH has some nerve showing up again. Make sure they don\'t last the daycycle.'),(144,3,28,'Danny-V-SNH has some nerve showing up again. Make sure they don\'t last the daycycle.'),(145,8,28,'Danny-V-SNH has some nerve showing up again. Make sure they don\'t last the daycycle.'),(146,11,28,'Danny-V-SNH has some nerve showing up again. Make sure they don\'t last the daycycle.'),(147,13,28,'Danny-V-SNH has some nerve showing up again. Make sure they don\'t last the daycycle.'),(148,14,28,'Danny-V-SNH has some nerve showing up again. Make sure they don\'t last the daycycle.'),(149,17,28,'Danny-V-SNH has some nerve showing up again. Make sure they don\'t last the daycycle.'),(150,4,29,'Comrade! Today is the day ve be throwink off chains! You startink a riot, yes?'),(151,21,29,'Comrade! Today is the day ve be throwink off chains! You startink a riot, yes?'),(152,11,29,'The sector is overpopulated and the people are unhappy. The time is now, start a riot so we may take over.'),(153,18,29,'The sector is overpopulated and the people are unhappy. The time is now, start a riot so we may take over.'),(154,7,29,'Woooo! Overpopulation annihilation! Start a riot so we can go crazy!');
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

-- Dump completed on 2016-04-26 11:03:42
