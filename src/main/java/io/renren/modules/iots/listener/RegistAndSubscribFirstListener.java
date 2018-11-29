package io.renren.modules.iots.listener;

import io.renren.modules.iots.entity.common.CommonConfig;
import io.renren.modules.iots.entity.ping.PingInfo;
import io.renren.modules.iots.entity.status.StatusInfo;
import io.renren.modules.iots.utils.config.WebConfig;
import io.renren.modules.iots.utils.mqtt.MqttUtils;
import io.renren.modules.iots.utils.templates.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(value = 2)
public class RegistAndSubscribFirstListener implements ApplicationRunner {

    @Override
    public void run( ApplicationArguments applicationArguments ) throws Exception {
        log.info("第二步：向 broker2 注册自身信息，并创建需要订阅的主题");

        MessageTemplates messageTemplates = new MessageTemplates();
        MqttMessage mqttMessage = new MqttMessage();

        // 发布
        MqttUtils mqttUtils = new MqttUtils();

        // 注册消息
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(1,messageTemplates.getMesBody(0,messageTemplates.getRegistInfo())));
        mqttUtils.publish(WebConfig.MQTT_REGIST_TOPIC,mqttMessage);

        // 配置
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(4,messageTemplates.getMesBody(0, new CommonConfig())));
        mqttUtils.publish(WebConfig.MQTT_CONFIG_TOPIC,mqttMessage);

        // control应答
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(6,null));
        mqttUtils.publish(WebConfig.MQTT_ANSWER_CONTROL_TOPIC,mqttMessage);

        // collect应答
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(6,null));
        mqttUtils.publish(WebConfig.MQTT_ANSWER_COLLECT_TOPIC,mqttMessage);

        // distribute应答
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(6,null));
        mqttUtils.publish(WebConfig.MQTT_ANSWER_DISTRIBUTE_TOPIC,mqttMessage);

        // 服务状态
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(5,messageTemplates.getMesBody(0,new StatusInfo())));
        mqttUtils.publish(WebConfig.MQTT_STATUS_TOPIC,mqttMessage);

        // 心跳
        mqttMessage = messageTemplates.getMqttMessage(messageTemplates.getMessage(2,messageTemplates.getMesBody(0,new PingInfo())));
        mqttUtils.publish(WebConfig.MQTT_PING_TOPIC,mqttMessage);

        // 向下减一计数
        ListenManager.ENABLE_SUBSCRIB.countDown();

        log.info("第二步：完成");
        // 执行完方法后，计数器减一
        ListenManager.COUNT.countDown();
    }
}
