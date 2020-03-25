/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 139.196.72.211:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 25/03/2020 10:44:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(512) NOT NULL COMMENT '标题',
  `content` text COMMENT '内容描述',
  `summary` text,
  `icon` varchar(128) DEFAULT NULL COMMENT '图标',
  `post_count` int(11) unsigned DEFAULT '0' COMMENT '该分类的内容数量',
  `order_num` int(11) DEFAULT NULL COMMENT '排序编码',
  `parent_id` bigint(32) unsigned DEFAULT NULL COMMENT '父级分类的ID',
  `meta_keywords` varchar(256) DEFAULT NULL COMMENT 'SEO关键字',
  `meta_description` varchar(256) DEFAULT NULL COMMENT 'SEO描述内容',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  `modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `content` longtext NOT NULL COMMENT '评论的内容',
  `parent_id` bigint(32) DEFAULT NULL COMMENT '回复的评论ID',
  `post_id` bigint(32) NOT NULL COMMENT '评论的内容ID',
  `user_id` bigint(32) NOT NULL COMMENT '评论的用户ID',
  `vote_up` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '“顶”的数量',
  `vote_down` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '“踩”的数量',
  `level` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '置顶等级',
  `status` tinyint(2) DEFAULT NULL COMMENT '评论的状态',
  `created` datetime NOT NULL COMMENT '评论的时间',
  `modified` datetime DEFAULT NULL COMMENT '评论的更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(128) NOT NULL COMMENT '标题',
  `content` longtext NOT NULL COMMENT '内容',
  `edit_mode` varchar(32) NOT NULL DEFAULT '0' COMMENT '编辑模式：html可视化，markdown ..',
  `category_id` bigint(32) DEFAULT NULL,
  `user_id` bigint(32) DEFAULT NULL COMMENT '用户ID',
  `vote_up` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '支持人数',
  `vote_down` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '反对人数',
  `view_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '访问量',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数量',
  `recommend` tinyint(1) DEFAULT NULL COMMENT '是否为精华',
  `level` tinyint(2) NOT NULL DEFAULT '0' COMMENT '置顶等级',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `modified` datetime DEFAULT NULL COMMENT '最后更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(128) NOT NULL COMMENT '昵称',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `email` varchar(64) DEFAULT NULL COMMENT '邮件',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机电话',
  `point` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '积分',
  `sign` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `gender` varchar(16) DEFAULT NULL COMMENT '性别',
  `wechat` varchar(32) DEFAULT NULL COMMENT '微信号',
  `vip_level` varchar(32) DEFAULT NULL COMMENT 'vip等级',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `avatar` varchar(256) NOT NULL COMMENT '头像',
  `post_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '内容数量',
  `comment_count` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '评论数量',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态',
  `lasted` datetime DEFAULT NULL COMMENT '最后的登陆时间',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `modified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'xiaoming', '1111', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 'https://tva1.sinaimg.cn/crop.0.0.118.118.180/5db11ff4gw1e77d3nqrv8j203b03cweg.jpg', 0, 0, 0, NULL, '2018-10-14 18:41:34', NULL);
COMMIT;

-- ----------------------------
-- Table structure for user_collection
-- ----------------------------
DROP TABLE IF EXISTS `user_collection`;
CREATE TABLE `user_collection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `post_id` bigint(20) NOT NULL,
  `post_user_id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收藏表';

-- ----------------------------
-- Table structure for user_message
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_user_id` bigint(20) DEFAULT NULL COMMENT '发送消息的用户ID',
  `to_user_id` bigint(20) DEFAULT NULL COMMENT '接收消息的用户ID',
  `post_id` bigint(20) DEFAULT NULL COMMENT '消息可能关联的帖子',
  `comment_id` bigint(20) DEFAULT NULL COMMENT '消息可能关联的评论',
  `content` text NOT NULL,
  `type` tinyint(2) DEFAULT NULL COMMENT '消息类型',
  `created` datetime NOT NULL,
  `modified` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
