CREATE TABLE `iots_regist_info` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `host` varchar(25) DEFAULT '' COMMENT 'ip和端口号，格式形如：192.192.192.192:8888',
  `mac_Address` varchar(12) DEFAULT '' COMMENT '服务的mac地址',
  `server_Type` varchar(20) DEFAULT '' COMMENT '服务的类型 PROXY--采集代理,COLLECT--物联网接口,WEB--web管理端,CONTROL--控制接口服务,DISTRIBUTE--分发接口服务',
  `server_Name` varchar(50) DEFAULT '' COMMENT '服务名称',
  `remark` varchar(225) DEFAULT '' COMMENT '说明',
  `create_Time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mac_Address` (`mac_Address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='服务注册信息表';