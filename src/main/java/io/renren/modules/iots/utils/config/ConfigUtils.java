package io.renren.modules.iots.utils.config;

import io.renren.modules.iots.entity.Message;
import io.renren.modules.iots.utils.fileIO.FileUtils;
import io.renren.modules.iots.utils.mqtt.MessageManage;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ConfigUtils {


    /**
     * 移除超时的元素
     */
    private Map<String, Message> outTime(Map<String, Message> messageMap){
        Map<String, Message> map = messageMap;
        if (!messageMap.isEmpty()){
            for (Message msg : messageMap.values()){
                // 获取当前时间戳
                Long current_time = System.currentTimeMillis();
                // 获得消息的时间戳
                Long create_time = msg.getCreate_time();
                if(current_time - create_time > 600000){ // 10 分钟
                    map.remove(msg.getMsg_id());
                }
            }
        }
        return map;
    }

    /**
     * 启用定时任务
     */
    public void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MessageManage.PUBLISH_MSG_MAP2 = outTime(MessageManage.PUBLISH_MSG_MAP2);
            }
        },1000);
    }

    /**
     * 更新config
     */
    public void setConfig(ConfigInfo configInfo){

        WebConfig.SERVERID = configInfo.getServerid();// 服务id
        WebConfig.SERVERTYPE = configInfo.getServerType();// 服务类型
        WebConfig.MQTT_ANSWER_COLLECT_TOPIC = configInfo.getMqtt_answer_collect_topic();// collect应答topic
        WebConfig.MQTT_ANSWER_CONTROL_TOPIC = configInfo.getMqtt_answer_control_topic();// control应答topic
        WebConfig.MQTT_ANSWER_DISTRIBUTE_TOPIC = configInfo.getMqtt_answer_distribute_topic();// distribute应答topic

        WebConfig.MQTT_CONFIG_TOPIC = configInfo.getMqtt_config_topic();// 配置topic
        WebConfig.MQTT_REGIST_TOPIC = configInfo.getMqtt_regist_topic();// 注册topic
        WebConfig.MQTT_PING_TOPIC = configInfo.getMqtt_ping_topic();// 心跳
        WebConfig.MQTT_STATUS_TOPIC = configInfo.getMqtt_status_topic();// 服务状态
        WebConfig.MAP_LOGS = configInfo.getMap_logs();// 日志

        // 持久化配置信息到文件中
        FileUtils fileUtils = new FileUtils();
        fileUtils.saveLoginInfo(configInfo);
    }
}
