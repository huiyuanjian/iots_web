-- 控制端 信息
-- ----------------------------
--  Table structure for `iots_ctrl_info`
-- ----------------------------
DROP TABLE IF EXISTS `iots_ctrl_info`;
CREATE TABLE `iots_ctrl_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` bigint(19) DEFAULT 0 COMMENT 'parentId , 所属 IOTServer的id',
  `name` varchar(225)  DEFAULT '' COMMENT '控制端名字',
  `ip` varchar(200) DEFAULT '' COMMENT '控制端ip',
  `port` int(10) DEFAULT 0 COMMENT '控制端 port',
  `creator` bigint(19) DEFAULT 0 COMMENT '创建者ID',
  `createtime` datetime DEFAULT NOW() COMMENT '创建时间',
  `updator` bigint(19) DEFAULT 0 COMMENT '更新者ID',
  `updatetime` datetime DEFAULT NOW() COMMENT '更新时间',
  `field1` varchar(50) DEFAULT '' COMMENT '预留字段1',
  `field2` varchar(255) DEFAULT '' COMMENT '预留字段2',
  `field3` varchar(255) DEFAULT '' COMMENT '预留字段3',
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='IOT Server端 ，即分组表';
 
