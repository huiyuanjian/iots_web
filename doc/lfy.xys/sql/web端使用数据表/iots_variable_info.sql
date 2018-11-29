-- 变量信息表
-- ----------------------------
--  Table structure for `iots_variable_info`
-- ----------------------------
DROP TABLE IF EXISTS `iots_variable_info`;
CREATE TABLE `iots_variable_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(225) DEFAULT '' COMMENT '变量名字',
  `pid` bigint(19) DEFAULT 0 COMMENT 'parentid ，所属设备的id',
  `cycle` bigint(10) DEFAULT '300' COMMENT '采集周期，默认为300，单位为秒（s）',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `variable_status` varchar(50) DEFAULT '' COMMENT '变量状态(包括是否需要采集等)',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 未启用 1 已启用）',
  `creator` bigint(19) DEFAULT 0 COMMENT '创建者ID',
  `createtime` datetime DEFAULT NOW() COMMENT '创建时间',
  `updator` bigint(19) DEFAULT 0 COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NOW() COMMENT '更新时间',
  `isdel` int(1) DEFAULT '0' COMMENT '是否已删除（0 表示未删除  1 表示已删除 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='变量信息表';