-- IOServer 代理 信息表
-- ----------------------------
--  Table structure for `iots_ioserver_info`
-- ----------------------------
DROP TABLE IF EXISTS `iots_ioserver_info`;
CREATE TABLE `iots_ioserver_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(200) NOT NULL DEFAULT '' COMMENT 'IOServer的名字',
  `pid` bigint(19) DEFAULT 0 COMMENT 'parentid ， 所属的分组的 id ',
  `cycle` bigint(10) DEFAULT '300' COMMENT '采集周期，默认为300，单位为秒（s）',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `ioserver_type` bigint(19) DEFAULT 0 COMMENT 'IOServer厂商类型id',
  `ip` varchar(225)  DEFAULT '' COMMENT 'ip地址 ',
  `port` int(10)  DEFAULT 0 COMMENT 'port',
  `state` int(10)  DEFAULT 0 COMMENT 'ioserver的状态',
  `creator` bigint(19) DEFAULT 0 COMMENT '创建者id',
 `createtime` datetime DEFAULT NOW() COMMENT '创建时间',
  `updator` bigint(19) DEFAULT 0 COMMENT '更新者id',
 `updatetime` datetime DEFAULT NOW() COMMENT '更新时间',
  `enable` int(1) DEFAULT 0 COMMENT '是否启用（0 表示未启用  1 表示已启用 ）',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(100) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(225) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='IOServer信息表';


-- 给 name创建唯一索引，保证 name不重复
alter table iots_ioserver_info add unique index(name);