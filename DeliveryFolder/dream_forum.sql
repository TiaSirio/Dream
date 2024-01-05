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
-- Database: `dream_forum`
--
CREATE DATABASE IF NOT EXISTS `dream_forum` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `dream_forum`;

-- --------------------------------------------------------

--
-- Struttura della tabella `Attachment`
--

DROP TABLE IF EXISTS `Attachment`;
CREATE TABLE IF NOT EXISTS `Attachment` (
  `attachmentId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `postId` int(11) NOT NULL,
  PRIMARY KEY (`attachmentId`),
  KEY `postId` (`postId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `Discussion`
--

DROP TABLE IF EXISTS `Discussion`;
CREATE TABLE IF NOT EXISTS `Discussion` (
  `discussionId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `text` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `topicId` int(11) NOT NULL,
  PRIMARY KEY (`discussionId`),
  KEY `topicId` (`topicId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `Post`
--

DROP TABLE IF EXISTS `Post`;
CREATE TABLE IF NOT EXISTS `Post` (
  `postId` int(11) NOT NULL AUTO_INCREMENT,
  `text` text NOT NULL,
  `status` int(11) NOT NULL DEFAULT 0,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `creatorId` int(11) NOT NULL,
  `discussionId` int(11) NOT NULL,
  PRIMARY KEY (`postId`),
  KEY `creatorId` (`creatorId`),
  KEY `discussionId` (`discussionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `Topic`
--

DROP TABLE IF EXISTS `Topic`;
CREATE TABLE IF NOT EXISTS `Topic` (
  `topicId` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`topicId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Struttura della tabella `User`
--

DROP TABLE IF EXISTS `User`;
CREATE TABLE IF NOT EXISTS `User` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `dateOfBirth` date NOT NULL,
  `areaOfResidence` varchar(255) NOT NULL,
  `policyMakerID` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`userId`),
  UNIQUE KEY `mail` (`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `Attachment`
--
ALTER TABLE `Attachment`
  ADD CONSTRAINT `Attachment_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `Post` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `Discussion`
--
ALTER TABLE `Discussion`
  ADD CONSTRAINT `Discussion_ibfk_1` FOREIGN KEY (`topicId`) REFERENCES `Topic` (`topicId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Limiti per la tabella `Post`
--
ALTER TABLE `Post`
  ADD CONSTRAINT `Post_ibfk_1` FOREIGN KEY (`creatorId`) REFERENCES `User` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Post_ibfk_2` FOREIGN KEY (`discussionId`) REFERENCES `Discussion` (`discussionId`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
