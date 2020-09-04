/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : springcloud

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 05/03/2019 16:43:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for properties
-- ----------------------------
DROP TABLE IF EXISTS `properties`;
CREATE TABLE `properties`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `KEY` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `VALUE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `APPLICATION` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `PROFILE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `LABLE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of properties
-- ----------------------------
INSERT INTO `properties` VALUES (3, 'com.laiyy.gitee.config', 'I am the mysql configuration file from dev environment.', 'config-simple', 'dev', 'master');
INSERT INTO `properties` VALUES (4, 'com.laiyy.gitee.config', 'I am the mysql configuration file from test environment.', 'config-simple', 'test', 'master');
INSERT INTO `properties` VALUES (5, 'com.laiyy.gitee.config', 'I am the mysql configuration file from prod environment.', 'config-simple', 'prod', 'master');

SET FOREIGN_KEY_CHECKS = 1;
