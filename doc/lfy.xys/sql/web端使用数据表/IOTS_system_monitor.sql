-- IOTS_system_monitor.sql
-- 系统列表
-- lfy.xys

DROP TABLE IF EXISTS `IOTS_system_monitor`;
CREATE TABLE `IOTS_system_monitor` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `system_id` bigint(3) DEFAULT '0' COMMENT '所属系统',
  `system_ip` varchar(225) DEFAULT '0' COMMENT '所属系统的ip',
  `totalMemory` varchar(225) DEFAULT '' COMMENT '内存总量',
  `used_Memory` varchar(225) DEFAULT '0' COMMENT '内存使用量',
  `cpu_Length` int(3) DEFAULT '0' COMMENT 'cpu个数',
  `cpu_id` int(3) DEFAULT 0 COMMENT 'cpu的id',
  `cpu_Used` varchar(225) DEFAULT '' COMMENT 'cpu使用率',
  `create_time` datetime DEFAULT NOW() COMMENT '创建时间',
  PRIMARY KEY (`id`)
)  COMMENT='系统监控信息存储表';


