-- phpMyAdmin SQL Dump
-- version 4.0.10.15
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 31, 2016 at 09:29 AM
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
-- Table structure for table `scout`
--

CREATE TABLE IF NOT EXISTS `scout` (
  `troopId` varchar(20) NOT NULL,
  `firstName` varchar(20) NOT NULL,
  `middleInit` varchar(1) DEFAULT NULL,
  `lastName` varchar(20) NOT NULL,
  `DOB` varchar(10) NOT NULL COMMENT 'YYYY-MM-DD',
  `phoneNumber` varchar(14) NOT NULL COMMENT '(555)-555-5555',
  `email` varchar(30) NOT NULL,
  PRIMARY KEY (`troopId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `scout`
--

INSERT INTO `scout` (`troopId`, `firstName`, `middleInit`, `lastName`, `DOB`, `phoneNumber`, `email`) VALUES
('937480', 'Caleb', 'A', 'Butcher', '1995-04-28', '(585)-752-0305', 'butcher.caleb@gmail.com'),
('11212', 'Tester', 'A', 'Dummy', '1999-02-25', '(585)-555-5789', 'tester@gmail.com'),
('87458', 'Mike', 'D', 'Carter', '1994-02-25', '(585)-555-5782', 'mcart5@brockport.edu'),
('8758', 'Nick', 'L', 'Dunham', '2000-05-30', '(585)-555-7836', 'nickDH@gmail.com'),
('34563', 'Bryan', 'Z', 'Duran', '2002-12-25', '(585)-555-4847', 'BryanZD@hotmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `trees`
--

CREATE TABLE IF NOT EXISTS `trees` (
  `barcode` varchar(20) NOT NULL,
  `treeType` varchar(20) NOT NULL,
  `notes` text,
  `status` varchar(10) NOT NULL,
  PRIMARY KEY (`barcode`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `trees`
--

INSERT INTO `trees` (`barcode`, `treeType`, `notes`, `status`) VALUES
('DGF821678', 'Douglas Fir', 'minor imperfections. $3 discount', 'sold'),
('FRF87585', 'Fraiser Fir', 'perfect condition', 'not-sold'),
('BSP82689', 'Blue Spruce', 'perfect condition', 'not-sold'),
('CCF834638', 'Concolor Fir', 'minor imperfections. $3 discount', 'not-sold'),
('DGF887598', 'Douglas Fir', 'perfect condition', 'not-sold');

-- --------------------------------------------------------

--
-- Table structure for table `tree_type`
--

CREATE TABLE IF NOT EXISTS `tree_type` (
  `barcodePrefix` varchar(10) NOT NULL,
  `description` text NOT NULL,
  `cost` double NOT NULL,
  PRIMARY KEY (`barcodePrefix`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tree_type`
--

INSERT INTO `tree_type` (`barcodePrefix`, `description`, `cost`) VALUES
('DGF', 'Dark green or blue green. Needles are soft to the touch', 20),
('FRF', 'Dark blue-green color, strong scent and good needle retention.', 22.5),
('BSP', 'Bluish-white color. Very distinct smell. Sharp needles', 18.75),
('CCF', 'Very vibrant blue-green color. Needles are short, thick and curved.', 19.75);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
