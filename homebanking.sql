CREATE TABLE `banca` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) DEFAULT NULL,
  `indirizzo` varchar(100) DEFAULT NULL,
  `amministratore_id` int(11) DEFAULT NULL,
  `direttore_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `filiale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `indirizzo` varchar(45) DEFAULT NULL,
  `orario_apertura` varchar(5) NOT NULL DEFAULT '08:00',
  `orario_chiusura` varchar(5) NOT NULL DEFAULT '12:00',
  `banca_id` int(11) DEFAULT '-1',
  `direttore_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `gallery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descrizione` varchar(45) DEFAULT NULL,
  `image` blob,
  `data_inserimento` datetime DEFAULT NULL,
  `banca_id` int(11) DEFAULT NULL,
  `filiale_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  PRIMARY KEY (`id`,`servizio_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `servizioCliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `saldo` float DEFAULT '0',
  `stato` varchar(20) DEFAULT 'disattivato',
  `servizio_id` int(11) DEFAULT '-1',
  `cliente_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;