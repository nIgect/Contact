CREATE DATABASE IF NOT EXISTS `tarasevichstasStudentLab` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `tarasevichstasStudentLab`;

CREATE TABLE IF NOT EXISTS `main_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `patronymic` varchar(20) DEFAULT NULL,
  `date_of_birth` date DEFAULT '1985-02-12',
  `gender` enum('мужской','женский') DEFAULT NULL,
  `nationality` varchar(20) DEFAULT NULL,
  `family_status` varchar(15) DEFAULT NULL,
  `web_site` varchar(150) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `work_place` varchar(50) DEFAULT NULL,
  `country` varchar(15) DEFAULT NULL,
  `city` varchar(15) DEFAULT NULL,
  `street` varchar(15) DEFAULT NULL,
  `house` varchar(10) DEFAULT NULL,
  `flat` int(11) unsigned DEFAULT NULL,
  `index_address` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=306 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `attachments` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(11) unsigned NOT NULL,
  `file_name` varchar(50) NOT NULL,
  `date_of_load` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comment` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_attachments_main_info_CascadetarasevichstasStudentLab` (`employee_id`),
  CONSTRAINT `FK_attachments_main_info_CascadetarasevichstasStudentLab` FOREIGN KEY (`employee_id`) REFERENCES `main_info` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5299329 DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS `phone` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(11) unsigned NOT NULL,
  `code_country` int(3) unsigned DEFAULT NULL,
  `code_operator` int(2) unsigned DEFAULT NULL,
  `number` int(11) unsigned NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `comment` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_phone_main_info_Cascade` (`employee_id`),
  CONSTRAINT `FK_phone_main_info_Cascade` FOREIGN KEY (`employee_id`) REFERENCES `main_info` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `photo` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(11) unsigned NOT NULL,
  `photo_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `FK_photo_main_info_Cascade` (`employee_id`),
  CONSTRAINT `FK_photo_main_info_Cascade` FOREIGN KEY (`employee_id`) REFERENCES `main_info` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

