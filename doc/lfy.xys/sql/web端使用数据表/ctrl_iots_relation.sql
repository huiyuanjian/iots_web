-- 控制端 和 IOTServer  的关系表 
DROP TABLE IF EXISTS `ctrl_iots_relation`;
CREATE TABLE `ctrl_iots_relation` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id', 
  `ctrl_id` bigint(19) DEFAULT '0' COMMENT '控制端的id',
  `iotserver_id` bigint(19) DEFAULT '0' COMMENT 'IotServer的id', 
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='控制端 和 IOTServer  的关系表';
 