-- MySQL dump 10.16  Distrib 10.1.38-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: homebanking
-- ------------------------------------------------------
-- Server version	10.1.38-MariaDB-0+deb9u1

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
-- Table structure for table `banca`
--

DROP TABLE IF EXISTS `banca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `banca` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) DEFAULT NULL,
  `indirizzo` varchar(100) DEFAULT NULL,
  `amministratore_id` int(11) DEFAULT NULL,
  `direttore_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banca`
--

LOCK TABLES `banca` WRITE;
/*!40000 ALTER TABLE `banca` DISABLE KEYS */;
INSERT INTO `banca` VALUES (1,'Banca 1','Via Banca 1',1,0),(2,'Banca 2','Via Banca 2',1,0);
/*!40000 ALTER TABLE `banca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filiale`
--

DROP TABLE IF EXISTS `filiale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filiale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `indirizzo` varchar(45) DEFAULT NULL,
  `orario_apertura` varchar(5) NOT NULL DEFAULT '08:00',
  `orario_chiusura` varchar(5) NOT NULL DEFAULT '12:00',
  `banca_id` int(11) DEFAULT '-1',
  `direttore_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filiale`
--

LOCK TABLES `filiale` WRITE;
/*!40000 ALTER TABLE `filiale` DISABLE KEYS */;
INSERT INTO `filiale` VALUES (2,'Filiale 11','Filiale 11','08:30','12:30',1,1),(3,'Filiale 1','Filiale 1','08:30','12:30',1,1),(4,'aa','aa','08:00','12:00',2,1),(5,'bb','bb','08:00','12:00',2,1);
/*!40000 ALTER TABLE `filiale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gallery`
--

DROP TABLE IF EXISTS `gallery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gallery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descrizione` varchar(45) DEFAULT NULL,
  `image` blob,
  `data_inserimento` datetime DEFAULT NULL,
  `banca_id` int(11) DEFAULT NULL,
  `filiale_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gallery`
--

LOCK TABLES `gallery` WRITE;
/*!40000 ALTER TABLE `gallery` DISABLE KEYS */;
INSERT INTO `gallery` VALUES (1,'/home/torsello/Scrivania/banca.jpeg','ÿØÿà\0JFIF\0\0\0\0\0\0ÿÛ\0„\0		\n\n	\r\r\r \"\" $(4,$&1\'-=-157:::#+?D?8C49:7\n\n\n\r\r\Z\Z7%%77777777777777777777777777777777777777777777777777ÿÀ\0\0\0¡\"\0ÿÄ\0\0\0\0\0\0\0\0\0\0\0\0\0\0ÿÄ\0K\0\n\n\0\0\0\0\0\0!1\"AQRqÑ23Sa“±²ÁÒ#6Brs‚ƒ‘’”¡áğ$&45CUbc„¢E£ÿÄ\0\0\0\0\0\0\0\0\0\0\0\0\0\0ÿÄ\0\0\0\0\0\0\0\0\0\0\0\0AÿÚ\0\0\0?\0î(ˆ€ˆˆˆ€ˆˆ¾^ö±ºN R¡+sLq†yG7`é*hEY¦Å±œ]RÆH×–·kTÍ\'MV8Üm<I,«Ô^/UAc0ÆMËuíÚ¼Ü#æıåeD]KËuô•à¤…»şÅgD:Ò \"\"\" \"ø’VDİ)\Z;ê³1Ç¤c a™üáØ\n–‰É%dMÒ‘Á£”•	[˜ckŒT,3ÉÊ¡áØ¡äßUïı.WIı¨õ4t­¨(Ã@³[Íf¯½fı5Y·Õ|Ÿ¦Jçÿ\0f-¶`£h\Z£o1j,Ú#­4“a»›[[¼ÉŠ`âkéØ]¼¥KTc”ğf\Zlívë<.“NúÒ@qğwÖFó©ã#€ØZµæ§:ZNn‘$a³‡hÆÆcÓàÕâ* ÛÓ9Òğ\'6¾íÁ6¶­}z¯Í„aª d1W5ÎÓ}N¨Ãu’x<Š‹.\'WL5ñúÁMQâ”ÕC‚ıñµÚˆUœ.i±*móWBê2îÀn—q§P·AY¦§~Òí±ìÔà’ØbÜŠ¯I‰UÓ\rNß\r öCóàSXµ5^ í	8ØíD-Ë©‰K¢¨\"\"\" \"\"‰ed-Ò‘Á£”•W˜š÷°èŒïç_‚<*[¤eu”ï\0é\rWâ<_z…š—ş9‘µìhà››lÙŞYº±,5U½t¦Kÿ\0U±\r,lm¤smÄÆêŠúsãwfÇ–;©|Zâ|YêXVÓK\Z,ÒĞ9÷M¼áô­KAÜO‹=Ih;‰ñg©MÙR¾zéç{è |µÂ¬TG!t±ï°Ô!e®Ëx\\Ãİ)ÙŠ¶¸T1â¡æ6Æƒmn@Î¥h´Äø³Ô–ƒ¸Ÿz•T%n..ìA¸“`‰³ä¦–KŸŒh\0C‘hÖåjªúL¬ÒË\r%•u ¼¸X–êâµÁåäV›AÜO‹=Ih;‰ñg©5\Z¹zŸ¡ ˜¤ñTx0Î×\'³‹Hq-ùÛ\'¤ŞpúVµ î\'Å¤´Äø³Ô Í#\"ÜØ;”kZZ{ÛH‡Ûcšt\\İ î\'Å¤öä|[ºe¦¬­£Çw‹šíN\n^‹¦ª:\ZZs]¨¨ˆÜæ±Œ}É°à¥%K†Z¦9æk	f¶Ûh+SJ•DE¶DD@DD«[NÙ@‘îµ‚à\ZxìVÒ=õX]s;XÃ¬poo½xúŠãqK|³gŞ¤qJX£1FÆtõ×3ÎUõT´rŠZ‰aÒ°%¶«ÙsÅjMì•Å<˜¨ÉcÜË€û\\kV|¡˜ñŒv¢yİOŠAdmuÎ«Ü.ÿ\0•r¼>²]ËeuÉ0º<ÅI^á¥ÂF_Sø$‹•h¿ïš½ï¦ß“CñU¼ã™ñŒ¾Êiau<™Å¥¯k®¸³¶)lÚXKuX[¼¹&7QQˆc2¶®y$lgEÎÔÑ«bY¨ı‘qêª¨ ltmİdlzN³nm~Ét&TVêT4jÛ z× ‰­`·u,¶×M•ig™î’S¦Ò÷“g%²TWáK5\rN,6¿E×9²N<ÙÍÆŒ–8´ccnrÛÎÕTôÏe=D± ë\\ª”ÔLl`b×EÊ—Ça¨šwSÆ\"x`lmuÎ«ÜİÊÅ¾jô/¾›¥É¡ø®o’á|yª‚6JöÅ3ÜÙcÍ8‹ølUï“]£q`vjØ¥Dæe©{Ù<mˆs5)Ærl65•,ü–b=²F]îãv³µL-Éˆ\"\" ˆˆˆ€ˆˆ\"±~Áİ>…Èóßìoéoœºæ/Ø;óÄ¹{ıı-ó–=_Œ\\ßW¬¤ËfZıNó\n£`½“~¿eQúÇEğæ”¢3v·ô9r\Z¯ß•Ğ^Ì½­İ\\†§÷íGÃô ÔŞ…Ñ2à¶S¥ù<â¨1joBèÜ¥7LR‚‡»_Êo•FÒ2ğ‚¤s·jùMò­*ĞT¶Se³Vºï1Êá™{ô;Òª™X~´a¿ï1Ê×™{ô;Ò¢,GÜåÅŸ)S\n(ûœ ø³å*atAV/Ø;óÄ¹{ıı-ó—[Æ\\?;%Ï;ÍçŠíò®~µâ¯ƒvMéWÜ¦ëæZıNóJ a&ÖéWlŸ(9ƒ_¾w˜åQjÌ½­İ\\†§÷íGÃô×³	\rärä5z±Ú›êáúX¢µ7¡t,¼o”éºdò•Ì™-˜Ş…ÒrËÁÊT€wÿ\0±J(ÙÛµ|¦ùV…	´Hg`w+ÿ\0S|ªš@#Úª¬yQ×ÍoÆ»Ìr¶æ^ÁıôªVPÙ†ÿ\0Çÿ\07+¦b:l~!Q£îrƒâÏ”©u”½ÎĞF)S¤Ax½@DDá OŠÖ¶6î!íÂá×ï©h®“wÒÜÃ¬N«‚ª¸îY_O,p5…Îa\0;V»t+”|ø>§âšlçÁõ?†œ‰Â9ÜÇj ØBHö4ÒßÕºQÄaÃ_\n÷·Ğ¯p£îTWåÜû`‚;îb•—Û£Æ\núij/ İ¼¡rüï†Ua¸5M@hfè´ƒï¹¿€…ÖtãçÁõÄ§–Û ¥}¶iE{ ã‘HdĞ-oy\rhi:‚ìäç!„9‘µ§V«€°î€öº=[-YtØ}üQ4ÅoÀëk©ee;Z\\æĞMµı›C8pu}…vİ8ùôÿ\0QbÜ(û•ˆ)¾Æ$º¾º¤´º&Â\"Ò\ZøDƒäz·WÓÉPcN¾]K4m‚ Db•€íÑŠ×_zQóàúŠ\rœ™M-D3ÛLÌ^-}„èV[¥ªm<Í>\"ĞĞEõ+r6F5Í Ü^À­Ê•‘xˆ´Qxˆ=DDçkÚËi. ßGˆ3±çÂB°\"˜+N§ÄÆÈiÏËwRÄæbãe-9ù×zªÓd²œÅÕEÇ()ÏÏ»ÕXİ.:6atçü‡zªåd°äNaªQ¨Ç¿”Áö“ê/õÿ\0(ƒí\'ÔW{DÑ‰Ì5HßXÿ\0òˆ>Ò}D8ùÿ\0¨ƒí\'ÔW}È–‰Ì5KcÇn\0ÿ\0!Ş¢È×ãgnN>}Şª¸Xr%‡\"s\rU\Z1ƒ¶Šœ|ó½U‘°â§m=8ùÇu+=’ÉÌ5]m.%Ì€|£Ô¤0Újˆd.œÅbÛ\0ËİIX/l¬†ˆˆªˆ€ˆˆˆ€ˆˆˆƒÂ½(ˆQ…zˆÂˆ€ˆˆˆ€ˆˆ?ÿÙ','2019-09-09 00:00:00',1,1),(2,'/home/torsello/Scrivania/banca.jpeg','ÿØÿà\0JFIF\0\0\0\0\0\0ÿÛ\0„\0		\n\n	\r\r\r \"\" $(4,$&1\'-=-157:::#+?D?8C49:7\n\n\n\r\r\Z\Z7%%77777777777777777777777777777777777777777777777777ÿÀ\0\0\0¡\"\0ÿÄ\0\0\0\0\0\0\0\0\0\0\0\0\0\0ÿÄ\0K\0\n\n\0\0\0\0\0\0!1\"AQRqÑ23Sa“±²ÁÒ#6Brs‚ƒ‘’”¡áğ$&45CUbc„¢E£ÿÄ\0\0\0\0\0\0\0\0\0\0\0\0\0\0ÿÄ\0\0\0\0\0\0\0\0\0\0\0\0AÿÚ\0\0\0?\0î(ˆ€ˆˆˆ€ˆˆ¾^ö±ºN R¡+sLq†yG7`é*hEY¦Å±œ]RÆH×–·kTÍ\'MV8Üm<I,«Ô^/UAc0ÆMËuíÚ¼Ü#æıåeD]KËuô•à¤…»şÅgD:Ò \"\"\" \"ø’VDİ)\Z;ê³1Ç¤c a™üáØ\n–‰É%dMÒ‘Á£”•	[˜ckŒT,3ÉÊ¡áØ¡äßUïı.WIı¨õ4t­¨(Ã@³[Íf¯½fı5Y·Õ|Ÿ¦Jçÿ\0f-¶`£h\Z£o1j,Ú#­4“a»›[[¼ÉŠ`âkéØ]¼¥KTc”ğf\Zlívë<.“NúÒ@qğwÖFó©ã#€ØZµæ§:ZNn‘$a³‡hÆÆcÓàÕâ* ÛÓ9Òğ\'6¾íÁ6¶­}z¯Í„aª d1W5ÎÓ}N¨Ãu’x<Š‹.\'WL5ñúÁMQâ”ÕC‚ıñµÚˆUœ.i±*móWBê2îÀn—q§P·AY¦§~Òí±ìÔà’ØbÜŠ¯I‰UÓ\rNß\r öCóàSXµ5^ í	8ØíD-Ë©‰K¢¨\"\"\" \"\"‰ed-Ò‘Á£”•W˜š÷°èŒïç_‚<*[¤eu”ï\0é\rWâ<_z…š—ş9‘µìhà››lÙŞYº±,5U½t¦Kÿ\0U±\r,lm¤smÄÆêŠúsãwfÇ–;©|Zâ|YêXVÓK\Z,ÒĞ9÷M¼áô­KAÜO‹=Ih;‰ñg©MÙR¾zéç{è |µÂ¬TG!t±ï°Ô!e®Ëx\\Ãİ)ÙŠ¶¸T1â¡æ6Æƒmn@Î¥h´Äø³Ô–ƒ¸Ÿz•T%n..ìA¸“`‰³ä¦–KŸŒh\0C‘hÖåjªúL¬ÒË\r%•u ¼¸X–êâµÁåäV›AÜO‹=Ih;‰ñg©5\Z¹zŸ¡ ˜¤ñTx0Î×\'³‹Hq-ùÛ\'¤ŞpúVµ î\'Å¤´Äø³Ô Í#\"ÜØ;”kZZ{ÛH‡Ûcšt\\İ î\'Å¤öä|[ºe¦¬­£Çw‹šíN\n^‹¦ª:\ZZs]¨¨ˆÜæ±Œ}É°à¥%K†Z¦9æk	f¶Ûh+SJ•DE¶DD@DD«[NÙ@‘îµ‚à\ZxìVÒ=õX]s;XÃ¬poo½xúŠãqK|³gŞ¤qJX£1FÆtõ×3ÎUõT´rŠZ‰aÒ°%¶«ÙsÅjMì•Å<˜¨ÉcÜË€û\\kV|¡˜ñŒv¢yİOŠAdmuÎ«Ü.ÿ\0•r¼>²]ËeuÉ0º<ÅI^á¥ÂF_Sø$‹•h¿ïš½ï¦ß“CñU¼ã™ñŒ¾Êiau<™Å¥¯k®¸³¶)lÚXKuX[¼¹&7QQˆc2¶®y$lgEÎÔÑ«bY¨ı‘qêª¨ ltmİdlzN³nm~Ét&TVêT4jÛ z× ‰­`·u,¶×M•ig™î’S¦Ò÷“g%²TWáK5\rN,6¿E×9²N<ÙÍÆŒ–8´ccnrÛÎÕTôÏe=D± ë\\ª”ÔLl`b×EÊ—Ça¨šwSÆ\"x`lmuÎ«ÜİÊÅ¾jô/¾›¥É¡ø®o’á|yª‚6JöÅ3ÜÙcÍ8‹ølUï“]£q`vjØ¥Dæe©{Ù<mˆs5)Ærl65•,ü–b=²F]îãv³µL-Éˆ\"\" ˆˆˆ€ˆˆ\"±~Áİ>…Èóßìoéoœºæ/Ø;óÄ¹{ıı-ó–=_Œ\\ßW¬¤ËfZıNó\n£`½“~¿eQúÇEğæ”¢3v·ô9r\Z¯ß•Ğ^Ì½­İ\\†§÷íGÃô ÔŞ…Ñ2à¶S¥ù<â¨1joBèÜ¥7LR‚‡»_Êo•FÒ2ğ‚¤s·jùMò­*ĞT¶Se³Vºï1Êá™{ô;Òª™X~´a¿ï1Ê×™{ô;Ò¢,GÜåÅŸ)S\n(ûœ ø³å*atAV/Ø;óÄ¹{ıı-ó—[Æ\\?;%Ï;ÍçŠíò®~µâ¯ƒvMéWÜ¦ëæZıNóJ a&ÖéWlŸ(9ƒ_¾w˜åQjÌ½­İ\\†§÷íGÃô×³	\rärä5z±Ú›êáúX¢µ7¡t,¼o”éºdò•Ì™-˜Ş…ÒrËÁÊT€wÿ\0±J(ÙÛµ|¦ùV…	´Hg`w+ÿ\0S|ªš@#Úª¬yQ×ÍoÆ»Ìr¶æ^ÁıôªVPÙ†ÿ\0Çÿ\07+¦b:l~!Q£îrƒâÏ”©u”½ÎĞF)S¤Ax½@DDá OŠÖ¶6î!íÂá×ï©h®“wÒÜÃ¬N«‚ª¸îY_O,p5…Îa\0;V»t+”|ø>§âšlçÁõ?†œ‰Â9ÜÇj ØBHö4ÒßÕºQÄaÃ_\n÷·Ğ¯p£îTWåÜû`‚;îb•—Û£Æ\núij/ İ¼¡rüï†Ua¸5M@hfè´ƒï¹¿€…ÖtãçÁõÄ§–Û ¥}¶iE{ ã‘HdĞ-oy\rhi:‚ìäç!„9‘µ§V«€°î€öº=[-YtØ}üQ4ÅoÀëk©ee;Z\\æĞMµı›C8pu}…vİ8ùôÿ\0QbÜ(û•ˆ)¾Æ$º¾º¤´º&Â\"Ò\ZøDƒäz·WÓÉPcN¾]K4m‚ Db•€íÑŠ×_zQóàúŠ\rœ™M-D3ÛLÌ^-}„èV[¥ªm<Í>\"ĞĞEõ+r6F5Í Ü^À­Ê•‘xˆ´Qxˆ=DDçkÚËi. ßGˆ3±çÂB°\"˜+N§ÄÆÈiÏËwRÄæbãe-9ù×zªÓd²œÅÕEÇ()ÏÏ»ÕXİ.:6atçü‡zªåd°äNaªQ¨Ç¿”Áö“ê/õÿ\0(ƒí\'ÔW{DÑ‰Ì5HßXÿ\0òˆ>Ò}D8ùÿ\0¨ƒí\'ÔW}È–‰Ì5KcÇn\0ÿ\0!Ş¢È×ãgnN>}Şª¸Xr%‡\"s\rU\Z1ƒ¶Šœ|ó½U‘°â§m=8ùÇu+=’ÉÌ5]m.%Ì€|£Ô¤0Újˆd.œÅbÛ\0ËİIX/l¬†ˆˆªˆ€ˆˆˆ€ˆˆˆƒÂ½(ˆQ…zˆÂˆ€ˆˆˆ€ˆˆ?ÿÙ','2019-09-09 00:00:00',1,1);
/*!40000 ALTER TABLE `gallery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operazione`
--

DROP TABLE IF EXISTS `operazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operazione` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` datetime DEFAULT NULL,
  `hash` varchar(100) DEFAULT NULL,
  `importo` float DEFAULT NULL,
  `tipologia` varchar(45) DEFAULT NULL,
  `servizio_id` int(11) NOT NULL DEFAULT '-1',
  `stato` varchar(20) DEFAULT 'non confermata',
  PRIMARY KEY (`id`,`servizio_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operazione`
--

LOCK TABLES `operazione` WRITE;
/*!40000 ALTER TABLE `operazione` DISABLE KEYS */;
/*!40000 ALTER TABLE `operazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotto`
--

DROP TABLE IF EXISTS `prodotto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prodotto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `denominazione` varchar(45) DEFAULT NULL,
  `data_attivazione` date DEFAULT NULL,
  `data_scadenza` date DEFAULT NULL,
  `descrizione` varchar(500) DEFAULT NULL,
  `url_condizioni_generali` varchar(45) DEFAULT NULL,
  `interessi_passivi` float DEFAULT NULL,
  `interessi_attivi` float DEFAULT NULL,
  `banca_id` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`,`banca_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotto`
--

LOCK TABLES `prodotto` WRITE;
/*!40000 ALTER TABLE `prodotto` DISABLE KEYS */;
INSERT INTO `prodotto` VALUES (3,'aaaa','2019-09-05','2019-09-20','ffff','lll',2,3,1),(4,'bbbb','2019-09-05','2019-09-20','ffff','lll',2,3,1),(5,'uuuu','2019-09-05','2019-09-20','ffff','lll',2,3,1);
/*!40000 ALTER TABLE `prodotto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servizio`
--

DROP TABLE IF EXISTS `servizio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servizio` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `denominazione` varchar(45) NOT NULL DEFAULT 'servizio generico',
  `data_attivazione` date NOT NULL DEFAULT '2019-01-01',
  `data_scadenza` date DEFAULT '2019-01-01',
  `descrizione` varchar(500) DEFAULT 'servizio finanziario generico',
  `numero_massimo_operazioni` int(11) DEFAULT '200',
  `prodotto_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servizio`
--

LOCK TABLES `servizio` WRITE;
/*!40000 ALTER TABLE `servizio` DISABLE KEYS */;
INSERT INTO `servizio` VALUES (4,'Den. servizio','2019-09-11','2021-09-18','Descrizione estesa',20,3),(5,'Secondo servizio bbb','2019-09-03','2019-09-20','Descrizione estesa',2000,4),(6,'Servizio di uuuu','2019-09-03','2019-09-04','Carta di Credito',200,5);
/*!40000 ALTER TABLE `servizio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servizioCliente`
--

DROP TABLE IF EXISTS `servizioCliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servizioCliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `saldo` float DEFAULT '0',
  `stato` varchar(20) DEFAULT 'disattivato',
  `servizio_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servizioCliente`
--

LOCK TABLES `servizioCliente` WRITE;
/*!40000 ALTER TABLE `servizioCliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `servizioCliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_registrazione` datetime DEFAULT NULL,
  `nome` varchar(45) DEFAULT NULL,
  `cognome` varchar(45) DEFAULT NULL,
  `indirizzo` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `ruolo` varchar(20) DEFAULT 'Not registered',
  `codice_fiscale` varchar(16) DEFAULT NULL,
  `partitaiva` varchar(20) DEFAULT NULL,
  `pec` varchar(45) DEFAULT NULL,
  `codice_univoco` varchar(45) DEFAULT NULL,
  `data_nascita` date DEFAULT NULL,
  `filiale_id` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`,`filiale_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES (1,'2019-09-07 00:00:00','Admin','','','first.admin@domain.com','admin','61646d696e','Amministratore','FRTAMM','000000000000','first.admin@domain.com','UUUUU','2019-09-07',0),(5,'2019-09-09 00:00:00','Carlo','Rossi','Via Mazzini n.4, 73040, Galatina, LE','carlo.rossi@domain.it','carlo.rossi@domain.it','134234267556','Cliente','TRSBTTPOIDSI','','','','2019-09-09',2),(6,'2019-09-09 00:00:00','Giovanni','Torsello','Via Mazzini n.4, 73040, Galatina, LE','giovanni.torsello@gmail.com','giovanni.torsello@gmail.com','789162849256','Cliente','TRSBTTPOIDSI','','','','2019-09-09',3);
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-09 20:38:22
