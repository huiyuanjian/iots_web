package io.renren.modules.iots.listener;

import io.renren.modules.iots.utils.config.WebConfig;
import io.renren.modules.iots.utils.sys.SysUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Order(value = 1)
public class ReadSystemPropertiesListener  implements ApplicationRunner {

    @Override
    public void run( ApplicationArguments applicationArguments ) throws Exception {
        log.info("第一步：读取系统信息");

        // 获取mac地址
        WebConfig.MACADDRESS = SysUtils.getMACAddress();

        // 初始化mqtt broker2 需要订阅的主题
        WebConfig.MQTT_REGIST_TOPIC = "REGIST/WEB/" + WebConfig.MACADDRESS; // 注册// TODO REGIST/#
        WebConfig.MQTT_CONFIG_TOPIC = "COLLECT/WEB/" + WebConfig.MACADDRESS + "/CONFIG"; // 配置
        WebConfig.MQTT_ANSWER_CONTROL_TOPIC = "CONTROL/WEB/" + WebConfig.MACADDRESS + "/ANSWER"; // control应答
        WebConfig.MQTT_ANSWER_COLLECT_TOPIC = "COLLECT/WEB/" + WebConfig.MACADDRESS + "/ANSWER"; // collect应答
        WebConfig.MQTT_ANSWER_DISTRIBUTE_TOPIC = "DISTRIBUTE/WEB/" + WebConfig.MACADDRESS + "/ANSWER"; // distribute应答

        WebConfig.MQTT_PING_TOPIC = "PING/WEB/" + WebConfig.MACADDRESS;// 心跳// TODO PING/#
        WebConfig.MQTT_STATUS_TOPIC = "STATUS/WEB/" + WebConfig.MACADDRESS; // 服务状态// TODO STATUS/#

        log.info("我所用的mac地址是：{}",WebConfig.MACADDRESS);
        log.info("第一步：完成");
        // 执行完方法后，计数器减一
        ListenManager.COUNT.countDown();
    }
}
