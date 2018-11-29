package io.renren.modules.iots.utils.mqtt;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.iots.entity.*;
import io.renren.modules.iots.entity.log.ServerLog;
import io.renren.modules.iots.entity.regist.RegistInfo;
import io.renren.modules.iots.entity.status.StatusInfo;
import io.renren.modules.iots.service.IotsIoserverInfoService;
import io.renren.modules.iots.service.IotsRegistInfoServerI;
import io.renren.modules.iots.service.LoggerService;
import io.renren.modules.iots.utils.config.WebConfig;
import io.renren.modules.iots.utils.sys.SystemCache;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * topic 业务处理类
 * 也就是说，这里的订阅消息全部来自采集代理
 */
@Slf4j
@Component
public class SubscribHandler {

    /**
     * 定义一个阻塞长度为128长度的队列
     * 注册 主题 broker2
     *
     * add        增加一个元索                如果队列已满，则抛出一个IIIegaISlabEepeplian异常
     * remove     移除并返回队列头部的元素      如果队列为空，则抛出一个NoSuchElementException异常
     * element    返回队列头部的元素           如果队列为空，则抛出一个NoSuchElementException异常
     * offer      添加一个元素并返回true       如果队列已满，则返回false
     * poll       移除并返问队列头部的元素      如果队列为空，则返回null
     * peek       返回队列头部的元素           如果队列为空，则返回null
     * put        添加一个元素                如果队列满，则阻塞
     * take       移除并返回队列头部的元素      如果队列为空，则阻塞
     */
    private static BlockingQueue<MqttMessage> queue_regist = new ArrayBlockingQueue<MqttMessage>(128);

    /**
     * 定义一个阻塞长度为128长度的队列
     * 配置 主题 broker2
     */
    private static BlockingQueue<MqttMessage> queue_config = new ArrayBlockingQueue<MqttMessage>(128);

    /**
     * 定义一个阻塞长度为1024长度的队列
     * 心跳 主题 broker2
     */
    private static BlockingQueue<MqttMessage> queue_ping = new ArrayBlockingQueue<MqttMessage>(1024);

    /**
     * 定义一个阻塞长度为1024长度的队列
     * 服务状态 主题 broker2
     */
    private static BlockingQueue<MqttMessage> queue_status = new ArrayBlockingQueue<MqttMessage>(1024);

    /**
     * 定义一个阻塞长度为128长度的队列
     * 服务日志 主题 broker2
     */
    private static BlockingQueue<MqttMessage> queue_log = new ArrayBlockingQueue<MqttMessage>(128);

    /**
     * 定义一个阻塞长度为16长度的队列
     * control 应答 主题 broker2
     */
    private static BlockingQueue<MqttMessage> queue_answer_control = new ArrayBlockingQueue<MqttMessage>(16);

    /**
     * 定义一个阻塞长度为32长度的队列
     * collect 应答 主题 broker2
     */
    private static BlockingQueue<MqttMessage> queue_answer_collect = new ArrayBlockingQueue<MqttMessage>(32);

    /**
     * 定义一个阻塞长度为16长度的队列
     * distribute 应答 主题 broker2
     */
    private static BlockingQueue<MqttMessage> queue_answer_distribute = new ArrayBlockingQueue<MqttMessage>(16);

    /**
     * 消息处理
     * topic 主题
     * qos 消息质量
     * msg 接收到的消息
     * topic 订阅主题
     * 详细说明参考 《14_mqtt通讯协议》
     */
    public static void handler(String topic,MqttMessage mqttMessage) throws InterruptedException {
        String array_top[] = topic.split("/");
        int len = array_top.length;
        String top = array_top[len > 0 ? (len - 1) : 0];
        switch (top){
            case "CONFIG" :  queue_config.put(mqttMessage); break;
            case "ANSWER" :  {
                top = topic.split("/")[0];
                switch (top) {
                    case "COLLECT" :  queue_answer_collect.put(mqttMessage); break;
                    case "CONTROL" :  queue_answer_control.put(mqttMessage); break;
                    case "DISTRIBUTE" :  queue_answer_distribute.put(mqttMessage); break;
                    default: break;
                }
            }; break;
            default: {
                top = topic.split("/")[0];
                switch (top) {
                    case "PING" :  queue_ping.put(mqttMessage); break;
                    case "STATUS" :  queue_status.put(mqttMessage); break;
                    case "REGIST" :  queue_regist.put(mqttMessage); break;
                    case "LOG" : queue_log.put(mqttMessage); break;
                    default: break;
                }
            };break;
        }
    }

    /**
     * 注册信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerRegist(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                IotsRegistInfoServerI iotsRegistInfoServerI = (IotsRegistInfoServerI) SpringContextUtils.getBean("iotsRegistInfoServerImpl");
                while (true){
                    try {
                        MqttMessage mqttMessage = queue_regist.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null && message.getBody() != null && message.getBody().getContext() != null){
                            // 注册消息入库,用于测试链接
                            RegistInfo registInfo = JSONObject.parseObject(message.getBody().getContext().get(0),RegistInfo.class);
                            if (registInfo != null) {
                                IotsRegistInfoEntity iotsRegistInfoEntity = new IotsRegistInfoEntity();
                                BeanUtils.copyProperties(registInfo,iotsRegistInfoEntity);
                                iotsRegistInfoServerI.addOne(iotsRegistInfoEntity);
                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("处理注册信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"注册线程").start();
    }

    /**
     * 处理配置信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerConfig(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    IotsIoserverInfoService iotsIoserverInfoService = (IotsIoserverInfoService) SpringContextUtils.getBean("iotsIoserverInfoService");
                    try {
                        MqttMessage mqttMessage = queue_config.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null && message.getBody() != null && message.getBody().getContext() != null){
                            // 回应消息
                            publish2(message);
                            //将设备和变量的信息入库（不能物理删除，需要比较更新）
                            // 获取到消息体
                            List<String> context = message.getBody().getContext();
                            // 保存设备和变量PushCallback
                            iotsIoserverInfoService.sync(context);
                            // 返回的消息
                            boolean change = MessageManage.change(message);
                            log.info("返回的结果是: - " + change);
                        }
                    } catch (InterruptedException e) {
                        log.error("处理配置信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"配置线程").start();
    }

    /**
     * 处理collect应答信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerAnswerCollect(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = queue_answer_collect.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null && message.getBody() != null){
                            // TODO 应答
                            MessageManage.answer(message.getCallback_id());
                            // TODO 重发做到一个线程中，单独维护
//                            // 控制消息的子消息类型 0 是成功  1 是失败
//                            int sub = message.getBody().getSub_type();
//                            if(sub == 0){
//                                MessageManage.answer(message.getCallback_id());
//                            } else if(sub == 1){
//                                MessageManage.republish(message.getCallback_id());
//                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("处理collect应答信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"collect应答线程").start();
    }

    /**
     * 处理control应答信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerAnswerControl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = queue_answer_control.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null && message.getBody() != null && message.getBody().getSub_type() != null){
                            // 控制消息的子消息类型 0 是成功  1 是失败
                            int sub = message.getBody().getSub_type();
                            if(sub == 0){
                                MessageManage.answer(message.getCallback_id());
                            } else if(sub == 1){
                                MessageManage.republish(message.getCallback_id());
                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("处理control应答信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"control应答线程").start();
    }

    /**
     * 处理distribute应答信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerAnswerDistribute(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = queue_answer_distribute.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null && message.getBody() != null){
                            // 控制消息的子消息类型 0 是成功  1 是失败
                            int sub = message.getBody().getSub_type();
                            if(sub == 0){
                                MessageManage.answer(message.getCallback_id());
                            } else if(sub == 1){
                                MessageManage.republish(message.getCallback_id());
                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("处理distribute应答信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"distribute应答线程").start();
    }

    /**
     * 心跳信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerPing(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = queue_ping.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null && message.getBody() != null && message.getBody().getContext() != null){
                            /**
                             * TODO 将心跳信息存到内存中
                             * TODO 相应的业务逻辑应写在 io.renren.modules.iots.utils.sys.SystemCache 类中，
                             * TODO 但是因为服务状态兼具了心跳的功能，所以这里先不实现，以后如果需要的话，再另外做一下即可
                             */
                        }
                    } catch (InterruptedException e) {
                        log.error("处理心跳信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"心跳线程").start();
    }

    /**
     * 处理服务状态信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerStatus(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        MqttMessage mqttMessage = queue_config.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null && message.getBody() != null && message.getBody().getContext() != null){
                            // 回应消息
                            publish2(message);

                            // 将信息存入管理器中
                            StatusInfo statusInfo = JSONObject.parseObject(message.getBody().getContext().get(0),StatusInfo.class);
                            SystemCache.put(statusInfo);
                        }
                    } catch (InterruptedException e) {
                        log.error("处理服务状态信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"服务状态线程").start();
    }

    /**
     * 日志信息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void handlerLog(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoggerService loggerService = (LoggerService) SpringContextUtils.getBean("loggerService");
                while (true){
                    try {
                        MqttMessage mqttMessage = queue_log.take();
                        Message message = JSONObject.parseObject(new String(mqttMessage.getPayload()),Message.class);
                        if(message != null && message.getBody() != null && message.getBody().getContext() != null){
                            // 拿到日志传输信息
                            ServerLog serverLog = JSONObject.parseObject(message.getBody().getContext().get(0),ServerLog.class);
                            // 转成数据库用的实体类集合// TODO 此处转list时侯转成了null，是有问题的
                            List<LogEntity> list = getLogEntitys(serverLog);
                            // 将日志信息入库
                            loggerService.saveList(list);
                        }
                    } catch (InterruptedException e) {
                        log.error("处理日志信息出现异常！异常信息是：{}",e.getMessage());
                    }
                }
            }
        },"日志线程").start();
    }

    /**
     * 向broker2回应
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void publish2(Message message){
        // 回应topic
        String answer_topic = WebConfig.SERVERTYPE + "/" + message.getSource_type().toUpperCase() + "/" + message.getSource_mac() + "/ANSWER";
        // mqtt工具类 broker2
        MqttUtils mqttUtils = new MqttUtils();
        mqttUtils.publish(answer_topic,getAnswer(message.getMsg_id(),"SUCCESS"));
        // 移除已经回应的消息
        MessageManage.PUBLISH_MSG_MAP2.remove(message.getCallback_id());
    }

    /**
     * 向broker2回应
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static void publish2(Message message,String context){
        // 回应topic
        String answer_topic = WebConfig.SERVERTYPE + "/" + message.getSource_type().toUpperCase() + "/" + message.getSource_mac() + "/ANSWER";
        // mqtt工具类 broker2
        MqttUtils mqttUtils = new MqttUtils();
        mqttUtils.publish(answer_topic,getAnswer(message.getMsg_id(),context));
        // 移除已经回应的消息
        MessageManage.PUBLISH_MSG_MAP2.remove(message.getCallback_id());
    }

    /**
     * 转换成数据库的实体类集合
     */
    private static List<LogEntity> getLogEntitys(ServerLog serverLog){
        // 获得日志上传的集合
        Map<String,Map<String,String>> map = serverLog.getMap();
        if (map != null && !map.isEmpty()) {
            // 得到日志的数量
            int len = getSize(map);
            List<LogEntity> entityList = new ArrayList<>(len);
            // 转换成日志实体信息
            for (Map<String,String> m :map.values()) {
                for (String fileName : m.keySet()){
                    String[] array = fileName.split("_");
                    String index_str = array[3].substring(0,array[3].length() - 4);
                    LogEntity logEntity = new LogEntity();
                    logEntity.setServer_mac(serverLog.getServer_mac());
                    logEntity.setIsdel(false);
                    logEntity.setServer_type(serverLog.getServer_type());
                    logEntity.setIndex(Long.valueOf(index_str));
                    logEntity.setFile_name(fileName);
                    logEntity.setDay_string(array[2]);
                    logEntity.setCreate_time(new Date());
                    logEntity.setUpload_url(m.get(fileName));
                    entityList.add(logEntity);
                }
            }
            return entityList;
        }
        return null;
    }

    /**
     * 得到日志map的长度
     */
    private static int getSize(Map<String,Map<String,String>> map){
        int index = 0;
        if (map == null || map.isEmpty()) {
            return 0;
        }
        for (Map<String,String> m : map.values()) {
            if (m != null) {
                index += m.size();
            }
        }
        return index;
    }

    /**
     * 构造应答消息
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static MqttMessage getAnswer(String msg_id,String context){

        // 构造消息内容
        MesBody body = new MesBody();
        List<String> list = new ArrayList <String>();
        list.add(context);
        body.setContext(list);

        // 构造消息
        Message answer_msg = new Message();
        answer_msg.setMsg_id(WebConfig.MACADDRESS + System.currentTimeMillis());// 此消息的id
        answer_msg.setCallback_id(msg_id);// 应答目标的id
        answer_msg.setLicense("");// TODO 如果有授权码，则再写相应的逻辑
        answer_msg.setSource_mac(WebConfig.MACADDRESS);// 发布者mac地址
        answer_msg.setSource_type(WebConfig.SERVERTYPE);// 发布者类型
        answer_msg.setMsg_type(6);// 服务类型
        answer_msg.setCreate_time(System.currentTimeMillis()); // 创建时间
        answer_msg.setBody(body); // 消息体

        // 构造mqtt消息
        MqttMessage answer = new MqttMessage();
        answer.setQos(1);
        answer.setRetained(true);
        answer.setPayload(JSONObject.toJSONString(answer_msg).getBytes());

        return answer;
    }

    /**
     * 转类型
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    private static String getType(int typs){
        String str_type = null;
        switch (typs){
            case 1 : str_type = "REGIST"; break;
            case 2 : str_type = "PING"; break;
            case 3 : str_type = "DATA"; break;
            case 4 : str_type = "CONFIG"; break;
            case 5 : str_type = "STATUS"; break;
            case 6 : str_type = "ANSWER"; break;
            case 7 : str_type = "CTRL"; break;
            default: break;
        }
        return str_type;
    }


    /**
     * 启动队列任务
     * @author 周西栋
     * @date
     * @param
     * @return
     */
    static {
        handlerRegist();
        handlerConfig();
        handlerAnswerCollect();
        handlerAnswerControl();
        handlerAnswerDistribute();

        handlerPing();
        handlerStatus();
        handlerLog();
    }
}
