/*
Navicat MySQL Data Transfer

Source Server         : Ali_112.74.63.151
Source Server Version : 50556
Source Host           : 112.74.63.151:3306
Source Database       : xpay

Target Server Type    : MYSQL
Target Server Version : 50556
File Encoding         : 65001

Date: 2018-01-09 16:22:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_pay
-- ----------------------------
DROP TABLE IF EXISTS `t_pay`;
CREATE TABLE `t_pay` (
  `id` varchar(255) CHARACTER SET utf8 NOT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `info` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `money` decimal(10,2) DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `state` int(2) DEFAULT NULL COMMENT '显示状态 0待审核 1确认显示 2驳回 3通过不展示',
  `pay_type` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `test_email` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '假管理邮箱',
  `username` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
