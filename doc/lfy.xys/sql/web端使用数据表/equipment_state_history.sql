-- equipment_state_history.sql
-- 设备状态历史记录表

DROP TABLE IF EXISTS `equipment_state_history`;
CREATE TABLE `equipment_state_history` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `eqmId` bigint(19) DEFAULT 0 COMMENT '设备id',
  `state` int(1) DEFAULT 0 COMMENT '状态（0 在线 ，1 不在线）',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `createtime` datetime DEFAULT NOW() COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='设备状态历史记录表';