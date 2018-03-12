/*
Navicat MySQL Data Transfer

Source Server         : 本地mysql
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : bookstore

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-03-12 16:59:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for orderitem
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem` (
  `order_id` varchar(100) NOT NULL,
  `product_id` varchar(100) NOT NULL,
  `buynum` int(11) DEFAULT NULL,
  PRIMARY KEY (`order_id`,`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `orderitem_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderitem
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` varchar(100) NOT NULL,
  `money` double DEFAULT NULL,
  `receiverAddress` varchar(255) DEFAULT NULL,
  `receiverName` varchar(20) DEFAULT NULL,
  `receiverPhone` varchar(20) DEFAULT NULL,
  `paystate` int(11) DEFAULT NULL,
  `ordertime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` varchar(100) NOT NULL,
  `name` varchar(40) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `pnum` int(11) DEFAULT NULL,
  `category` varchar(40) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `img_url` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES ('3b5aa996-2663-4702-b0b3-24c736b7cc45', '8', '8', '999', '计算机', '哈哈哈', '7\\4\\104.jpg');
INSERT INTO `products` VALUES ('48fe08b7-bfed-4e70-938e-6bd9391b4127', '测试编码', '9', '995', '计算机', '测试编码', 'e\\c\\TS4.jpg');
INSERT INTO `products` VALUES ('6a60833f-c4d4-4700-9c11-f4401bbd3a9f', '5', '5', '997', '计算机', '7', 'c\\b\\java2.jpg');
INSERT INTO `products` VALUES ('7b3e56c8-00f8-462b-9f90-d62a3d3bcede', '3', '3', '999', '计算机', '3', 'e\\0\\fish.jpg');
INSERT INTO `products` VALUES ('9f8fbc43-b0e5-4191-8ca9-253c190c208c', '7', '7', '6', '原版', '7', 'd\\6\\think.jpg');
INSERT INTO `products` VALUES ('b66d3e7e-e791-4e50-9210-5e0ca11beeb6', '9', '9', '9', '艺术', '9', '9\\2\\TS12.jpg');
INSERT INTO `products` VALUES ('cb4f9efb-1462-4245-8290-ea9b0f4b9972', '1', '1', '888', '文学', '1', 'f\\1\\euro.jpg');
INSERT INTO `products` VALUES ('d0526713-067c-497c-ae92-62ab418365da', '测试商品', '111', '111', '生活', '任振华', '7\\3\\kongb.jpg');
INSERT INTO `products` VALUES ('d54ae5aa-d05e-46f3-bba3-9a36a7bdeab2', '6', '6', '999', '励志', '6', 'e\\c\\TS2.jpg');
INSERT INTO `products` VALUES ('df725548-1e2c-44ce-932e-39ab257d2158', '2', '2', '999', '生活', '2', '8\\1\\subwayinshanghai.jpg');
INSERT INTO `products` VALUES ('ea048b4c-8074-476f-b04a-69fe2e452d96', '4', '4', '999', '外语', '4', '7\\4\\104.jpg');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `PASSWORD` varchar(20) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `introduce` varchar(100) DEFAULT NULL,
  `activeCode` varchar(50) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `role` varchar(10) DEFAULT '普通用户',
  `registTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('20', '任振华', '201625212', '男', 'scen@vip.qq.com', '110', '任振华', 'ce2b5147-c2b4-4569-b6ff-1986d08b197d', '1', 'admin', '2018-03-12 16:50:27');
