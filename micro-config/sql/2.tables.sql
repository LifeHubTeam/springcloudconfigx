/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : life_config

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-08-19 15:58:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_app
-- ----------------------------
DROP TABLE IF EXISTS `t_app`;
CREATE TABLE `t_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'application name',
  `app_profile` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'active profile',
  `app_label` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'label version',
  `app_state` int(4) NOT NULL DEFAULT '0' COMMENT '0 delete 1 effect',
  `app_version` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'version desc',
  `app_desc` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'describle',
  `profile_type` int(1) NOT NULL DEFAULT '0' COMMENT '环境类型，0：dev，1：test，2：beta，3：exp，4：prod',
  `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `create_time` timestamp(3) NOT NULL DEFAULT '0000-00-00 00:00:00.000',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_app_properties
-- ----------------------------
DROP TABLE IF EXISTS `t_app_properties`;
CREATE TABLE `t_app_properties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) NOT NULL,
  `p_key` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '配置项Key',
  `p_value` varchar(2048) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '配置项值',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1: deleted, 0: normal',
  `describ` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `modify_man_id` int(11) NOT NULL COMMENT '修改人id',
  `modify_man` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '修改人',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '0 delete 1 effect',
  `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `create_time` timestamp(3) NOT NULL DEFAULT '0000-00-00 00:00:00.000' COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_app_properties_history
-- ----------------------------
DROP TABLE IF EXISTS `t_app_properties_history`;
CREATE TABLE `t_app_properties_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `old_id` int(11) NOT NULL COMMENT '旧配置Id',
  `app_id` int(11) NOT NULL COMMENT 'app关联Id',
  `p_key` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT '配置项Key',
  `p_value` varchar(2048) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '配置项值',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '0 delete 1 effect',
  `is_deleted` int(4) NOT NULL DEFAULT '0' COMMENT '1: deleted, 0: normal',
  `describ` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `modify_man_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `modify_man` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `create_time` timestamp(3) NOT NULL DEFAULT '0000-00-00 00:00:00.000' COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for t_config_user
-- ----------------------------
DROP TABLE IF EXISTS `t_config_user`;
CREATE TABLE `t_config_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) NOT NULL COMMENT '账号',
  `mobile` varchar(20) NOT NULL COMMENT '手机号码',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `state` int(1) NOT NULL COMMENT '状态 0:冻结 1:正常',
  `role_type` int(1) NOT NULL DEFAULT '0' COMMENT '角色类型，0:普通用户，1:超级管理员',
  `head_image` varchar(500) DEFAULT NULL COMMENT '头像',
  `create_time` timestamp(3) NOT NULL DEFAULT '0000-00-00 00:00:00.000' COMMENT '创建时间',
  `update_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ind_account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_properties
-- ----------------------------
DROP TABLE IF EXISTS `t_properties`;
CREATE TABLE `t_properties` (
  `p_application` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `p_profile` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `p_label` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `p_version` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `p_key` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `p_value` varchar(5000) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
