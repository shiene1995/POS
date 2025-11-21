-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 21, 2025 at 12:26 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_pos`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_account`
--

CREATE TABLE `tb_account` (
  `id` int(11) NOT NULL,
  `uname` varchar(100) NOT NULL,
  `username` varchar(20) NOT NULL,
  `pass_hash` varchar(100) NOT NULL,
  `pass_salt` varchar(100) NOT NULL,
  `acc_type` varchar(20) NOT NULL,
  `date_created` date NOT NULL,
  `status` varchar(5) NOT NULL,
  `img_link` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_account`
--

INSERT INTO `tb_account` (`id`, `uname`, `username`, `pass_hash`, `pass_salt`, `acc_type`, `date_created`, `status`, `img_link`) VALUES
(1, 'Kenneth Salvador', 'admin', 'S9WgXRcpietxKvnXID9Wpli171UDGfffPlscFR+KA5I=', 'k12I/BIM34PNNnej7w9w0g==', 'master', '2025-11-13', 'true', '1.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `tb_category`
--

CREATE TABLE `tb_category` (
  `category` varchar(50) NOT NULL,
  `icon` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_category`
--

INSERT INTO `tb_category` (`category`, `icon`) VALUES
('Coffee', 'coffee'),
('Cake', 'cake'),
('Drinks', 'drinks'),
('Sides', 'sides');

-- --------------------------------------------------------

--
-- Table structure for table `tb_product`
--

CREATE TABLE `tb_product` (
  `id` int(11) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `short_name` varchar(50) NOT NULL,
  `qty` int(11) NOT NULL,
  `price` float(10,2) NOT NULL,
  `category` varchar(50) NOT NULL,
  `icon` varchar(50) NOT NULL,
  `type` varchar(20) NOT NULL,
  `status` varchar(5) NOT NULL,
  `display` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_product`
--

INSERT INTO `tb_product` (`id`, `item_name`, `short_name`, `qty`, `price`, `category`, `icon`, `type`, `status`, `display`) VALUES
(1, 'Chocolate Cherry Cake', 'CCCherry', 20, 550.00, 'Cake', 'cake', 'product', 'true', 'true'),
(2, 'Vanilla Cake', 'VaniCake', 20, 605.00, 'Cake', 'cake', 'product', 'true', 'true'),
(3, 'Strawberry Cake', 'StrbryCake', 20, 830.00, 'Coffee', 'coffee', 'product', 'true', 'true'),
(4, 'Spanish Latte Cake', 'SpnLattCake', 30, 715.00, 'Coffee', 'coffee', 'product', 'true', 'true'),
(5, 'Coke', 'Coke', 20, 30.00, 'Drinks', 'drinks', 'variant', 'true', 'true'),
(6, 'Coke Regular', 'Coke R', 20, 30.00, 'Drinks', 'drinks', 'product', 'true', 'false'),
(7, 'Coke Medium', 'Coke M', 20, 40.00, 'Drinks', 'drinks', 'product', 'true', 'false'),
(8, 'Coke Large', 'Coke L', 20, 50.00, 'Drinks', 'drinks', 'product', 'true', 'false');

-- --------------------------------------------------------

--
-- Table structure for table `tb_productvariant`
--

CREATE TABLE `tb_productvariant` (
  `id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `display_name` varchar(50) NOT NULL,
  `price` float(10,2) NOT NULL,
  `default_variant` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_productvariant`
--

INSERT INTO `tb_productvariant` (`id`, `product_id`, `display_name`, `price`, `default_variant`) VALUES
(5, 6, 'Regular', 30.00, 'true'),
(5, 7, 'Medium', 40.00, 'false'),
(5, 8, 'Large', 50.00, 'false');

-- --------------------------------------------------------

--
-- Table structure for table `tb_session`
--

CREATE TABLE `tb_session` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `session_start` varchar(20) NOT NULL,
  `session_closed` varchar(20) NOT NULL,
  `starting_cash` float(10,2) NOT NULL,
  `net_sales` float(10,2) NOT NULL,
  `refunds` float(10,2) NOT NULL,
  `service_charge` float(10,2) NOT NULL,
  `discount` float(10,2) NOT NULL,
  `cash` float(10,2) NOT NULL,
  `gcash` float(10,2) NOT NULL,
  `debit` float(10,2) NOT NULL,
  `credit` float(10,2) NOT NULL,
  `cash_declared` float(10,2) NOT NULL,
  `cash_drawer` float(10,2) NOT NULL,
  `cash_difference` float(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_session`
--

INSERT INTO `tb_session` (`id`, `user_id`, `session_start`, `session_closed`, `starting_cash`, `net_sales`, `refunds`, `service_charge`, `discount`, `cash`, `gcash`, `debit`, `credit`, `cash_declared`, `cash_drawer`, `cash_difference`) VALUES
(14, 1, '2025-11-21 17:24:51', '', 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_account`
--
ALTER TABLE `tb_account`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_product`
--
ALTER TABLE `tb_product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tb_session`
--
ALTER TABLE `tb_session`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_account`
--
ALTER TABLE `tb_account`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tb_product`
--
ALTER TABLE `tb_product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `tb_session`
--
ALTER TABLE `tb_session`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
