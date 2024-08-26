--
-- Delete the tables
--
-- Currently, due to FOREIGN KEY dependencies, the deletion order is strict,
-- A better approach is to drop all FOREIGN KEYs before the tables.
--

DROP TABLE IF EXISTS collection_quote;
DROP TABLE IF EXISTS topic_quote;
DROP TABLE IF EXISTS topic;
DROP TABLE IF EXISTS collection;
DROP TABLE IF EXISTS quote;
DROP TABLE IF EXISTS author;


--
-- Table structure for table `author`
--

CREATE TABLE `author` (
  `id` int NOT NULL AUTO_INCREMENT,
  `birthday` varchar(255) DEFAULT NULL,
  `count` int DEFAULT NULL,
  `featured` int NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `profile` text,
  PRIMARY KEY (`id`)
);


--
-- Table structure for table `collection`
--

CREATE TABLE `collection` (
  `id` int NOT NULL AUTO_INCREMENT,
  `count` int DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `profile` text,
  PRIMARY KEY (`id`)
);


--
-- Table structure for table `topic`
--

CREATE TABLE `topic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `count` int DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `profile` text,
  PRIMARY KEY (`id`)
);


--
-- Table structure for table `quote`
--

CREATE TABLE `quote` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text,
  `author_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK55f8tgrl40w4xdhsfvgy2ve4m` (`author_id`),
  CONSTRAINT `FK55f8tgrl40w4xdhsfvgy2ve4m` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
);


--
-- Table structure for table `collection_quote`
--

CREATE TABLE `collection_quote` (
  `collection_id` int NOT NULL,
  `quote_id` int NOT NULL,
  PRIMARY KEY (`collection_id`,`quote_id`),
  KEY `FK8r0v18ubsoqpba320bjc8qhog` (`quote_id`),
  CONSTRAINT `FK8r0v18ubsoqpba320bjc8qhog` FOREIGN KEY (`quote_id`) REFERENCES `quote` (`id`),
  CONSTRAINT `FKc6e4tcstnob5n25s1c8b0p762` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`)
);


--
-- Table structure for table `topic_quote`
--

CREATE TABLE `topic_quote` (
  `topic_id` int NOT NULL,
  `quote_id` int NOT NULL,
  PRIMARY KEY (`topic_id`,`quote_id`),
  KEY `FKa4okiytbw15i6w38lk939t4dm` (`quote_id`),
  CONSTRAINT `FKa4okiytbw15i6w38lk939t4dm` FOREIGN KEY (`quote_id`) REFERENCES `quote` (`id`),
  CONSTRAINT `FKb21oeu8iiineq5t84hl0icqot` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
);