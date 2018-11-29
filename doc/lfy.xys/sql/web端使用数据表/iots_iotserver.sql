-- IOTServer， IOT Server端 ，即分组表
-- ----------------------------
--  Table structure for `iots_iotserver`
-- ----------------------------
DROP TABLE IF EXISTS `iots_iotserver`;
CREATE TABLE `iots_iotserver` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(225)   DEFAULT '' COMMENT '分组名字',
  `topic` varchar(200) DEFAULT '' COMMENT '分组对应的kafka主题',
  `ip` varchar(200) DEFAULT '' COMMENT 'ip地址',
  `port` int(8) DEFAULT 0 COMMENT 'port端口',
  `state` int(3) DEFAULT 0 COMMENT '状态',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `pid` bigint(19) DEFAULT '0' COMMENT '父节点id（当值为0时，表示没有父节点，即最高级别）',
  `creator` bigint(19) DEFAULT 0 COMMENT '创建者ID',
  `createtime` datetime DEFAULT NOW() COMMENT '创建时间',
  `updator` bigint(19) DEFAULT 0 COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NOW() COMMENT '更新时间',
  `enable` int(1) DEFAULT '0' COMMENT '是否启用（0 表示未启用  1 表示已启用 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='IOT Server端 ，即分组表';

-- 给 name创建唯一索引，保证 name不重复
alter table iots_iotserver add unique index(name);