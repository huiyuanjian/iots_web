package io.renren.modules.iots.utils.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * web配置信息实体类(用于持久化数据和服务间的通讯)
 */
@Data
@Slf4j
public class ConfigInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 服务id（数据库中的id）
    private String serverid;

    // 服务类型（字母全大写）
    private final String serverType = "WEB";

    // 服务的mac地址（用于topic）
    private String macaddress = null;

    /**
     * mqtt 注册 主题 broker2
     */
    private String mqtt_regist_topic = null;

    /**
     * mqtt 配置 主题 broker2
     */
    private String mqtt_config_topic = null;

    /**
     * mqtt collect应答 主题 broker2
     */
    private String mqtt_answer_collect_topic = null;

    /**
     * mqtt control应答 主题 broker2
     */
    private String mqtt_answer_control_topic = null;

    /**
     * mqtt distribute应答 主题 broker2
     */
    private String mqtt_answer_distribute_topic = null;

    /**
     * mqtt 状态 主题 broker2
     */
    private String mqtt_status_topic = null;

    /**
     * mqtt 心跳 主题 broker2
     */
    private String mqtt_ping_topic = null;

    /**
     * 存放日志上传后的访问路径
     */
    private Map<String, Map<String,String>> map_logs = new HashMap<>();

}
