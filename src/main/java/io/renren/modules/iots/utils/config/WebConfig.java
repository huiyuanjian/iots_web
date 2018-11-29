/**   
 * Copyright © 2018 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.htby.bksw.iots_collect.utils.config 
 * @author: zhouxidong   
 * @date: 2018年5月27日 下午2:33:31 
 */
package io.renren.modules.iots.utils.config;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/** 
 * @ClassName: WebConfig
 * @Description: 关于web服务的配置
 * @author: zhouxidong
 * @date: 2018年5月27日 下午2:33:31  
 */
@Slf4j
public class WebConfig {
	
	// 防止新建对象
    private WebConfig(){};

	/**
	 * 服务id
	 */
	public static String SERVERID = null;

	/**
	 * 服务名称
	 */
	public static String SERVERNAME = null;

	/**
	 * 服务类型
	 */
	public static String SERVERTYPE = "WEB";

	/**
	 * 服务说明
	 */
	public static String SERVERREMARK = null;

	/**
	 * mac地址
	 */
	public static String MACADDRESS = null;

	/**
	 * mqtt 注册 主题 broker2
	 */
	public static String MQTT_REGIST_TOPIC = null;

	/**
	 * mqtt 配置 主题 broker2
	 */
	public static String MQTT_CONFIG_TOPIC = null;

	/**
	 * mqtt collect应答 主题 broker2
	 */
	public static String MQTT_ANSWER_COLLECT_TOPIC = null;

	/**
	 * mqtt control应答 主题 broker2
	 */
	public static String MQTT_ANSWER_CONTROL_TOPIC = null;

	/**
	 * mqtt distribute应答 主题 broker2
	 */
	public static String MQTT_ANSWER_DISTRIBUTE_TOPIC = null;

	/**
	 * mqtt 心跳 主题 broker2
	 */
	public static String MQTT_PING_TOPIC = null;

	/**
	 * mqtt 服务状态 主题 broker2
	 */
	public static String MQTT_STATUS_TOPIC = null;

	/**
	 * 存放上传后的日志访问路径
	 */
	public static Map<String, Map<String,String>> MAP_LOGS = new HashMap<>();

	/**
	 * 系统是否已经启动
	 */
	public static boolean SERVER_STATUS = false;

}
