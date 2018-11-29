package io.renren.modules.iots.utils.config;

import io.renren.modules.iots.listener.ListenManager;
import io.renren.modules.iots.utils.fileIO.FileUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 读取本地配置信息的线程
 */
@Slf4j
public class ReadLocalConfigInfoThread implements Runnable{

    @Override
    public void run() {
        log.info("读取本地配置信息的线程已经启动！");
        FileUtils fileUtils = new FileUtils();
        // 配置消息
        ConfigInfo configInfo = fileUtils.readLoginInfo();
        // 配置工具类
        ConfigUtils configUtils = new ConfigUtils();
        if(configInfo != null){
            // 将读到的配置信息注入到系统中
            configUtils.setConfig(configInfo);
            log.info("本地配置信息读取成功！信息内容是：{}",configInfo.toString());
        } else {
            log.info("没有本地配置信息，读取信息失败，信息为空！");
        }
        log.info("读取本地配置的线程执行完毕！");
        // 执行完方法后，计数器减一
        ListenManager.COUNT.countDown();
    }
}
