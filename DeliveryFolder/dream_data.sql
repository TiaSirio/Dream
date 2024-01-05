-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Feb 06, 2022 alle 23:32
-- Versione del server: 10.5.13-MariaDB-0ubuntu0.21.10.1
-- Versione PHP: 8.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dream_data`
--
CREATE DATABASE IF NOT EXISTS `dream_data` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `dream_data`;

-- --------------------------------------------------------

--
-- Struttura della tabella `Administrator`
--

DROP TABLE IF EXISTS `Administrator`;
CREATE TABLE IF NOT EXISTS `Administrator` (
  `administratorId` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  PRIMARY KEY (`administratorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `Area`
--

DROP TABLE IF EXISTS `Area`;
CREATE TABLE IF NOT EXISTS `Area` (
  `areaId` varchar(4) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`areaId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `Data`
--

DROP TABLE IF EXISTS `Data`;
CREATE TABLE IF NOT EXISTS `Data` (
  `dataId` int(11) NOT NULL AUTO_INCREMENT,
  `value` text NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `district` varchar(255) NOT NULL,
  `dataSourceId` int(11) NOT NULL,
  PRIMARY KEY (`dataId`),
  KEY `dataSourceId` (`dataSourceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `DataSet`
--

DROP TABLE IF EXISTS `DataSet`;
CREATE TABLE IF NOT EXISTS `DataSet` (
  `dataSetId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`dataSetId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `DataSource`
--

DROP TABLE IF EXISTS `DataSource`;
CREATE TABLE IF NOT EXISTS `DataSource` (
  `dataSourceId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `source` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `dataTypeId` int(11) NOT NULL,
  PRIMARY KEY (`dataSourceId`),
  KEY `dataTypeId` (`dataTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `DataType`
--

DROP TABLE IF EXISTS `DataType`;
CREATE TABLE IF NOT EXISTS `DataType` (
  `dataTypeId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`dataTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `Ranking`
--

DROP TABLE IF EXISTS `Ranking`;
CREATE TABLE IF NOT EXISTS `Ranking` (
  `rankId` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `districtId` varchar(4) NOT NULL,
  `jsonData` varchar(255) NOT NULL,
  `parameters` varchar(255) NOT NULL,
  `policyMakerID` varchar(255) NOT NULL,
  PRIMARY KEY (`rankId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `Data`
--
ALTER TABLE `Data`
  ADD CONSTRAINT `Data_ibfk_1` FOREIGN KEY (`dataSourceId`) REFERENCES `DataSource` (`dataSourceId`);

--
-- Limiti per la tabella `DataSource`
--
ALTER TABLE `DataSource`
  ADD CONSTRAINT `DataSource_ibfk_1` FOREIGN KEY (`dataTypeId`) REFERENCES `DataType` (`dataTypeId`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
