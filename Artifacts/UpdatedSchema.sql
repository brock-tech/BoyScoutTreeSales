-- phpMyAdmin SQL Dump
-- version 4.0.10.15
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 11, 2016 at 07:09 PM
-- Server version: 5.1.73-log
-- PHP Version: 5.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `spr16_csc429_cbutc1`
--

-- --------------------------------------------------------

--
-- Table structure for table `Scout`
--

CREATE TABLE IF NOT EXISTS `Scout` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LastName` text NOT NULL,
  `FirstName` text NOT NULL,
  `MiddleName` text NOT NULL,
  `DateOfBirth` text NOT NULL,
  `PhoneNumber` text NOT NULL,
  `Email` text NOT NULL,
  `TroopID` text NOT NULL,
  `Status` text NOT NULL COMMENT 'will have values ‘Active’ and ‘Inactive’',
  `DateStatusUpdated` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `Scout`
--

INSERT INTO `Scout` (`ID`, `LastName`, `FirstName`, `MiddleName`, `DateOfBirth`, `PhoneNumber`, `Email`, `TroopID`, `Status`, `DateStatusUpdated`) VALUES
(1, 'Butcher', 'Caleb', 'Andrew', '1995/04/28', '585-752-0305', 'butcher.caleb@gmail.com', '8746885139', 'Inactive', '2016/04/10'),
(2, 'Doe', 'Joe', 'Test', '1999/08/14', '585-555-8574', 'joeTest@gmail.com', '7841310852', 'Active', '2011/08/25');

-- --------------------------------------------------------

--
-- Table structure for table `Tree`
--

CREATE TABLE IF NOT EXISTS `Tree` (
  `BarCode` int(11) NOT NULL,
  `TreeType` int(11) DEFAULT NULL,
  `SalePrice` float NOT NULL,
  `CName` varchar(40) NOT NULL,
  `CPhoneNum` varchar(20) NOT NULL,
  `CEmail` varchar(30) NOT NULL,
  `Notes` text,
  `Status` text,
  `DateStatusUpdated` text,
  `TimeStatusUpdated` text,
  PRIMARY KEY (`BarCode`),
  KEY `TreeType` (`TreeType`),
  KEY `TreeType_2` (`TreeType`),
  KEY `TreeType_3` (`TreeType`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Tree`
--

INSERT INTO `Tree` (`BarCode`, `TreeType`, `SalePrice`, `CName`, `CPhoneNum`, `CEmail`, `Notes`, `Status`, `DateStatusUpdated`, `TimeStatusUpdated`) VALUES
(258, 1, 20, 'TEST', 'test', 'tTest', '', '', '04/11/2016', '19:02:01');

-- --------------------------------------------------------

--
-- Table structure for table `Tree_Type`
--

CREATE TABLE IF NOT EXISTS `Tree_Type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TypeDescription` text NOT NULL,
  `Cost` int(20) NOT NULL,
  `BarcodePrefix` int(2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `Tree_Type`
--

INSERT INTO `Tree_Type` (`ID`, `TypeDescription`, `Cost`, `BarcodePrefix`) VALUES
(1, 'Fraiser Fir - Regular', 45, 20),
(2, 'Fraiser Fir - Premium', 45, 21),
(3, 'Douglas Fir - Regular', 45, 30),
(4, 'Douglas Fir - Premium', 50, 31),
(5, 'Blue Spruce - Regular', 45, 40),
(6, 'Blue Spruce - Premium', 50, 41),
(7, 'Concolor - Regular', 45, 50),
(8, 'Concolor - Premium', 50, 51),
(9, 'Balsam Fir - Regular', 45, 60),
(10, 'Balsam Fir - Premium', 50, 61);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Tree`
--
ALTER TABLE `Tree`
  ADD CONSTRAINT `Tree_ibfk_1` FOREIGN KEY (`TreeType`) REFERENCES `Tree_Type` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
