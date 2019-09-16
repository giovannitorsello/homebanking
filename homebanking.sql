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
  `descrizione` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `cliente_id` int(11) DEFAULT NULL,
  `data_conferma_cassiere` date DEFAULT NULL,
  `filiale_id` int(11) DEFAULT NULL,
  `cassiere_id` int(11) DEFAULT NULL,
  `note` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`,`servizio_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prodottoCliente`
--

DROP TABLE IF EXISTS `prodottoCliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prodottoCliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `saldo` float DEFAULT '0',
  `stato` varchar(20) DEFAULT 'disattivato',
  `prodotto_id` int(11) DEFAULT '-1',
  `cliente_id` int(11) DEFAULT NULL,
  `data_attivazione` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `tipologieOperazioneServizio` varchar(200) DEFAULT 'addebita,accredita',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `cliente_id` int(11) DEFAULT NULL,
  `data_attivazione` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `banca_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`filiale_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-09-13 22:17:07
