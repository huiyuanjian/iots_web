-- Date: 06/11/2018 16:29:11 PM 导出

/*
 Navicat MySQL Data Transfer

 Source Server         : 123.56.138.120
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : 123.56.138.120
 Source Database       : iots

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : utf-8

 Date: 06/11/2018 16:29:11 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `iots_business_tree_relation`
-- ----------------------------
DROP TABLE IF EXISTS `iots_business_tree_relation`;
CREATE TABLE `iots_business_tree_relation` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `business_id` bigint(19) DEFAULT '0' COMMENT '业务id',
  `parent_id` bigint(19) DEFAULT '0' COMMENT '父节点id',
  `business_type` bigint(19) DEFAULT '0' COMMENT '业务类型',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatet_ime` datetime DEFAULT NULL COMMENT '更新时间',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='树形关系表';

-- ----------------------------
--  Table structure for `iots_collection_agent`
-- ----------------------------
DROP TABLE IF EXISTS `iots_collection_agent`;
CREATE TABLE `iots_collection_agent` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(225) NOT NULL DEFAULT '' COMMENT '采集代理服务名字',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `ip_self` varchar(225) DEFAULT '' COMMENT '自身ip（完整，带端口号）',
  `ip_service` varchar(225) DEFAULT '' COMMENT '物联网接口服务器ip地址（完整，带端口号）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 表示未启用  1 表示已启用 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='采集代理服务表';

-- ----------------------------
--  Table structure for `iots_collectserver_info`
-- ----------------------------
DROP TABLE IF EXISTS `iots_collectserver_info`;
CREATE TABLE `iots_collectserver_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `name` varchar(225) DEFAULT '' COMMENT '采集代理服务器的名称',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `server_type` int(2) DEFAULT '0' COMMENT '采集服务器的类型（0 代表默认 1 。。 2 。。 3 。。 ）',
  `ip_address` varchar(225) DEFAULT '' COMMENT '采集服务器的ip地址',
  `enable` int(11) DEFAULT '0' COMMENT '是否启用（0 表示未启用  1 表示启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`),
  KEY `ip_address` (`ip_address`) COMMENT '采集代理服务ip地址'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='采集代理服务信息表';

-- ----------------------------
--  Table structure for `iots_ctrl_manage`
-- ----------------------------
DROP TABLE IF EXISTS `iots_ctrl_manage`;
CREATE TABLE `iots_ctrl_manage` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `manage` varchar(225) DEFAULT '0' COMMENT '协议信息',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `result` varchar(225) DEFAULT '' COMMENT '结果',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatet_ime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='控制信息表';

-- ----------------------------
--  Table structure for `iots_equipment_group`
-- ----------------------------
DROP TABLE IF EXISTS `iots_equipment_group`;
CREATE TABLE `iots_equipment_group` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(225) DEFAULT '' COMMENT '设备分组名字',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `parentid` bigint(19) DEFAULT NULL COMMENT '父节点id',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1 已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='设备分组表';

-- ----------------------------
--  Table structure for `iots_equipment_info`
-- ----------------------------
DROP TABLE IF EXISTS `iots_equipment_info`;
CREATE TABLE `iots_equipment_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(225) DEFAULT '' COMMENT '设备的名字',
  `pid` bigint(19) DEFAULT NULL COMMENT 'parentid, 所属的IOServer的 id',
  `cycle` bigint(10) DEFAULT '300' COMMENT '采集周期，默认为300，单位为秒（s）',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `equipment_type` int(4) DEFAULT '0' COMMENT '设备类型 （0表示默认）',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1 已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='设备信息表';

-- ----------------------------
--  Table structure for `iots_group`
-- ----------------------------
DROP TABLE IF EXISTS `iots_group`;
CREATE TABLE `iots_group` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(225) NOT NULL DEFAULT '' COMMENT '分组名字',
  `topic` varchar(200) DEFAULT NULL COMMENT '分组对应的kafka主题',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `pid` bigint(19) DEFAULT '0' COMMENT '父节点id（当值为0时，表示没有父节点，即最高级别）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 表示未启用  1 表示已启用 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='IOServer 分组表';

-- ----------------------------
--  Table structure for `iots_interface_server_things`
-- ----------------------------
DROP TABLE IF EXISTS `iots_interface_server_things`;
CREATE TABLE `iots_interface_server_things` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `name` varchar(225) DEFAULT '' COMMENT '物联网接口服务器的名字',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `ip_address` varchar(225) DEFAULT '' COMMENT '物联网接口服务器的ip地址',
  `server_type` int(1) DEFAULT '0' COMMENT '物联网接口服务器的类型（0 采集服务 1控制服务）',
  `runable` int(1) DEFAULT '0' COMMENT '是否正常运行（0 是  1 否）',
  `excep_msg` varchar(225) DEFAULT '' COMMENT '异常信息',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='物联网接口服务器表';

-- ----------------------------
--  Table structure for `iots_interface_server_upper`
-- ----------------------------
DROP TABLE IF EXISTS `iots_interface_server_upper`;
CREATE TABLE `iots_interface_server_upper` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `name` varchar(225) DEFAULT '' COMMENT '业务接口服务和控制接口服务的名字',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `ip_address` varchar(225) DEFAULT '' COMMENT '业务接口服务和控制接口服务的ip地址',
  `server_type` int(1) DEFAULT '0' COMMENT '业务接口服务和控制接口服务的类型（0 业务接口服务 1 控制接口服务）',
  `runable` int(1) DEFAULT '0' COMMENT '是否正常运行（0 是  1 否）',
  `excep_msg` varchar(225) DEFAULT '' COMMENT '异常信息',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='业务接口服务和控制接口服务表';

-- ----------------------------
--  Table structure for `iots_ioserver_info`
-- ----------------------------
DROP TABLE IF EXISTS `iots_ioserver_info`;
CREATE TABLE `iots_ioserver_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT 'IOServer的名字',
  `pid` bigint(19) DEFAULT NULL COMMENT 'parentid ， 所属的分组的 id ',
  `cycle` bigint(10) DEFAULT '300' COMMENT '采集周期，默认为300，单位为秒（s）',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `ioserver_type` bigint(19) NOT NULL COMMENT 'IOServer厂商类型id',
  `ip_address` varchar(225) NOT NULL DEFAULT '' COMMENT 'ip地址（包括端口号）',
  `ioserver_status` varchar(50) NOT NULL DEFAULT '' COMMENT 'ioserver的状态',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者id',
  `createtime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者id',
  `updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 表示未启用  1 表示已启用 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(100) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(225) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='IOServer信息表';

-- ----------------------------
--  Table structure for `iots_relation_tree`
-- ----------------------------
DROP TABLE IF EXISTS `iots_relation_tree`;
CREATE TABLE `iots_relation_tree` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `business_id` bigint(19) DEFAULT '0' COMMENT '业务id',
  `parent_id` bigint(19) DEFAULT '0' COMMENT '父节点id',
  `business_type` bigint(19) DEFAULT '0' COMMENT '业务类型',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatet_ime` datetime DEFAULT NULL COMMENT '更新时间',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='树形关系表';

-- ----------------------------
--  Table structure for `iots_system_monitor`
-- ----------------------------
DROP TABLE IF EXISTS `iots_system_monitor`;
CREATE TABLE `iots_system_monitor` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `system_id` bigint(3) DEFAULT '0' COMMENT '所属系统',
  `system_ip` varchar(225) DEFAULT '0' COMMENT '所属系统的ip',
  `totalMemory` varchar(225) DEFAULT '' COMMENT '内存总量',
  `used_Memory` varchar(225) DEFAULT '0' COMMENT '内存使用量',
  `cpu_Length` int(3) DEFAULT '0' COMMENT 'cpu个数',
  `cpu_id` int(3) DEFAULT NULL COMMENT 'cpu的id',
  `cpu_Used` varchar(225) DEFAULT NULL COMMENT 'cpu使用率',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='系统监控信息存储表';

-- ----------------------------
--  Table structure for `iots_topic`
-- ----------------------------
DROP TABLE IF EXISTS `iots_topic`;
CREATE TABLE `iots_topic` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `topic` varchar(225) DEFAULT '' COMMENT '队列主题',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `topic_type` int(1) DEFAULT '0' COMMENT '主题类型（0 默认不用 1 采集传输 2 监控传输 3 命令传输 4分发传输）',
  `runable` int(1) DEFAULT '0' COMMENT '是否正常运行（0 是  1 否）',
  `excep_msg` varchar(225) DEFAULT '' COMMENT '异常信息',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='消失队列主题表';

-- ----------------------------
--  Table structure for `iots_topic_ioserver_relation`
-- ----------------------------
DROP TABLE IF EXISTS `iots_topic_ioserver_relation`;
CREATE TABLE `iots_topic_ioserver_relation` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `topic_id` bigint(19) NOT NULL COMMENT '队列主题的id',
  `ioserver_id` bigint(19) NOT NULL COMMENT 'IOServer的id',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='消失队列主题与IOServer关系表';

-- ----------------------------
--  Table structure for `iots_topology_info`
-- ----------------------------
DROP TABLE IF EXISTS `iots_topology_info`;
CREATE TABLE `iots_topology_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '' COMMENT '节点名字',
  `remark` varchar(255) DEFAULT '' COMMENT '说明',
  `topology_type` int(1) DEFAULT '0' COMMENT '拓扑类型（0 采集信息流 1 指令信息流）',
  `upper_id` bigint(19) NOT NULL COMMENT '上一个节点的id',
  `lower_id` bigint(19) NOT NULL COMMENT '下一个节点的id',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='拓扑信息表';

-- ----------------------------
--  Table structure for `iots_variable_authority_relation`
-- ----------------------------
DROP TABLE IF EXISTS `iots_variable_authority_relation`;
CREATE TABLE `iots_variable_authority_relation` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `variable_id` bigint(19) NOT NULL COMMENT '变量的id',
  `authority_type` int(1) DEFAULT '0' COMMENT '权限类型（0 业务接口可用 1 控制接口可用）',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='变量权限关系表';

-- ----------------------------
--  Table structure for `iots_variable_info`
-- ----------------------------
DROP TABLE IF EXISTS `iots_variable_info`;
CREATE TABLE `iots_variable_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(225) DEFAULT '' COMMENT '变量名字',
  `pid` bigint(19) DEFAULT NULL COMMENT 'parentid ，所属设备的id',
  `cycle` bigint(10) DEFAULT '300' COMMENT '采集周期，默认为300，单位为秒（s）',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `variable_status` varchar(50) DEFAULT '' COMMENT '变量状态(包括是否需要采集等)',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1 已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='变量信息表';

-- ----------------------------
--  Table structure for `iots_wrap_info`
-- ----------------------------
DROP TABLE IF EXISTS `iots_wrap_info`;
CREATE TABLE `iots_wrap_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(225) DEFAULT '' COMMENT '名字',
  `pid` bigint(19) DEFAULT NULL COMMENT 'parentid ，所属设备的id',
  `cycle` bigint(10) DEFAULT '300' COMMENT '采集周期，默认为300，单位为秒（s）',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `wrap_status` varchar(50) DEFAULT '' COMMENT '数据包的状态',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1 已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据包信息表';

-- ----------------------------
--  Table structure for `iots_wrap_variable_relation`
-- ----------------------------
DROP TABLE IF EXISTS `iots_wrap_variable_relation`;
CREATE TABLE `iots_wrap_variable_relation` (
  `id` bigint(19) NOT NULL COMMENT 'id',
  `wrap_id` bigint(19) DEFAULT NULL COMMENT '数据包的id',
  `variable_id` bigint(19) DEFAULT NULL COMMENT '变量id',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1 已启用）',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`),
  KEY `wrap_id` (`wrap_id`) COMMENT '采集包的id',
  KEY `variable_id` (`variable_id`) COMMENT '变量的id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='采集包与变量的关系表';

SET FOREIGN_KEY_CHECKS = 1;

-- IOTS_system_monitor.sql
-- 系统列表
-- 1：监控系统
-- 2: web端
-- 3：iots端
-- 4: 代理端
-- lfy.xys

DROP TABLE IF EXISTS `IOTS_system_monitor`;
CREATE TABLE `IOTS_system_monitor` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `system_id` bigint(3) DEFAULT '0' COMMENT '所属系统',
  `system_ip` varchar(225) DEFAULT '0' COMMENT '所属系统的ip',
  `totalMemory` varchar(225) DEFAULT '' COMMENT '内存总量',
  `used_Memory` varchar(225) DEFAULT '0' COMMENT '内存使用量',
  `cpu_Length` int(3) DEFAULT '0' COMMENT 'cpu个数',
  `cpu_id` int(3) DEFAULT NULL COMMENT 'cpu的id',
  `cpu_Used` varchar(225) DEFAULT NULL COMMENT 'cpu使用率',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
)  COMMENT='系统监控信息存储表';


-- IOTS_Ctrl_manage.sql
-- 控制信息表，存储 协议的消息，和返回的结果

DROP TABLE IF EXISTS `IOTS_Ctrl_manage`;
CREATE TABLE `IOTS_Ctrl_manage` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `manage` varchar(225) DEFAULT '0' COMMENT '协议信息',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `result` varchar(225) DEFAULT '' COMMENT '结果',
  `creator` bigint(19) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updator` bigint(19) DEFAULT NULL COMMENT '更新者ID',
  `updatet_ime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)  COMMENT='控制信息表';

