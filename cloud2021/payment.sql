/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80014
Source Host           : localhost:3306
Source Database       : eolinker_os

Target Server Type    : MYSQL
Target Server Version : 80014
File Encoding         : 65001

Date: 2020-08-27 14:43:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `serial` varchar(200) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payment
-- ----------------------------
INSERT INTO `payment` VALUES ('1', '1001');
