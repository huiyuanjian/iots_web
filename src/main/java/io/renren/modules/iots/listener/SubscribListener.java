package io.renren.modules.iots.listener;

import io.renren.modules.iots.utils.config.ConfigUtils;
import io.renren.modules.iots.utils.config.WebConfig;
import io.renren.modules.iots.utils.mqtt.TopicSubscrib;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(value = 4)
public class SubscribListener implements ApplicationRunner {

    @Override
    public void run( ApplicationArguments applicationArguments ) throws Exception {

        // 订阅
        TopicSubscrib topicSubscrib = new TopicSubscrib();
        topicSubscrib.run();

        // 启用定时任务
        ConfigUtils utils = new ConfigUtils();
        utils.startTimer();

        // 服务启动成功
        WebConfig.SERVER_STATUS = true;
    }
}
