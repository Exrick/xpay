/*
Navicat MySQL Data Transfer

Source Server         : Ali_112.74.63.151
Source Server Version : 50556
Source Host           : 112.74.63.151:3306
Source Database       : xpay

Target Server Type    : MYSQL
Target Server Version : 50556
File Encoding         : 65001

Date: 2018-01-02 10:19:16
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

-- ----------------------------
-- Records of t_pay
-- ----------------------------
INSERT INTO `t_pay` VALUES ('0a9ce7fa-f3af-4a29-8b23-53a892d89941', '我来试试', '', '1.00', '594689303@qq.com', '2', 'Alipay', '2018-01-02 09:53:41', '2018-01-02 10:12:17', '', '');
INSERT INTO `t_pay` VALUES ('1ac9a7b7-5441-4714-b477-c9c8024150f1', '似懂非懂', 'sdfsd', '111.00', 'svend.jin@qq.com', '2', 'Alipay', '2018-01-02 09:43:38', '2018-01-02 10:09:07', 'svend.jin@qq.com', 'sdfsd');
INSERT INTO `t_pay` VALUES ('3f95dc9a-3f34-433e-a34b-38d6a7efdef3', 'dddd', '', '100.00', 'dddd@dddd.com', '2', 'Alipay', '2018-01-02 09:58:26', '2018-01-02 10:18:55', '', '');
INSERT INTO `t_pay` VALUES ('4b2aabd9-e0aa-46c4-bf23-451917fbd474', 'yj', '测试一下', '0.00', '279517443@qq.com', '3', 'Alipay', '2018-01-02 09:48:41', '2018-01-02 10:09:29', '', '');
INSERT INTO `t_pay` VALUES ('4f5603e4-965c-4724-9ec6-4786d0dc5952', '1', '', '2.00', '3a@qq.com', '2', 'Alipay', '2018-01-02 10:05:01', '2018-01-02 10:08:31', '', '');
INSERT INTO `t_pay` VALUES ('6974e10a-2b37-4661-b47c-bd603822bdc1', 'lisgroup', 'test', '0.00', '407505297@qq.com', '2', 'Alipay', '2018-01-02 09:40:25', '2018-01-02 10:08:58', '', '');
INSERT INTO `t_pay` VALUES ('6fdf4cf2-76d6-4428-b072-5b833c85007a', '一样', '', '0.00', '279517443@qq.com', '0', 'Alipay', '2018-01-02 09:51:14', null, '', '');
INSERT INTO `t_pay` VALUES ('8f29fd9e-f4c6-4524-ad5d-4e5896a5979e', '1', '1', '1.00', '1029538990@qq.com', '2', 'Alipay', '2018-01-02 10:04:09', '2018-01-02 10:10:48', '1029538990@qq.com', '1029538990@qq.com');
INSERT INTO `t_pay` VALUES ('b56194e7-73ee-4bbf-a243-53506942b219', '1', '', '1.00', 'asbb@gmail.com', '0', 'Alipay', '2018-01-01 20:58:38', null, '', '');
INSERT INTO `t_pay` VALUES ('dc1cde83-573c-4577-9b6f-bef9753cddeb', '熊三', '', '0.00', 'kl45678@qq.com', '0', 'Wechat', '2018-01-02 10:04:33', null, 'kl45678@qq.com', 'ppllpig@foxmail.com');
INSERT INTO `t_pay` VALUES ('fcc89502-93a4-41b8-bff7-0d2e123e3466', '何金涛', '', '0.00', '365353650@qq.com', '3', 'Alipay', '2018-01-02 09:58:49', '2018-01-02 10:08:18', '', '');
