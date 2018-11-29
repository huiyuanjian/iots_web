package io.renren.modules.iots.utils.mqtt;

import io.renren.modules.iots.utils.config.WebConfig;

/**
 * 主题订阅
 * @author 周西栋
 * @date
 * @param
 * @return
 */
public class TopicSubscrib {

    public void run(){
        MqttUtils mqttUtils = new MqttUtils();
        mqttUtils.subscribTopic(WebConfig.MQTT_CONFIG_TOPIC);
        mqttUtils.subscribTopic("COLLECT/WEB/" + WebConfig.MACADDRESS + "/ANSWER");
        mqttUtils.subscribTopic(WebConfig.MQTT_ANSWER_CONTROL_TOPIC);
        mqttUtils.subscribTopic(WebConfig.MQTT_ANSWER_DISTRIBUTE_TOPIC);
        mqttUtils.subscribTopic("PING/#");

        mqttUtils.subscribTopic("STATUS/#");
        mqttUtils.subscribTopic("LOG/#");
        mqttUtils.subscribTopic("REGIST/#");
    }
}
