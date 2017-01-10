/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50621
Source Host           : 127.0.0.1:3306
Source Database       : db_spring

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2016-12-11 22:40:27
*/
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_spring` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `db_spring`;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_count
-- ----------------------------
DROP TABLE IF EXISTS `t_count`;
CREATE TABLE `t_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `userName` varchar(20) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_count
-- ----------------------------
INSERT INTO `t_count` VALUES ('1', '1', '张三', '550');
INSERT INTO `t_count` VALUES ('2', '2', '李四', '550');
