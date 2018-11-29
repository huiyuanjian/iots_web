package io.renren.modules.iots.utils.templates;

import com.alibaba.fastjson.JSONObject;
import io.renren.modules.iots.entity.MesBody;
import io.renren.modules.iots.entity.Message;
import io.renren.modules.iots.entity.ping.PingInfo;
import io.renren.modules.iots.entity.regist.RegistInfo;
import io.renren.modules.iots.entity.status.StatusInfo;
import io.renren.modules.iots.utils.config.WebConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息模版
 */
@Slf4j
public class MessageTemplates {

    /**
     * 获得MqttMessage
     */
    public MqttMessage getMqttMessage(Message message){
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setRetained(true);
        mqttMessage.setQos(1);
        mqttMessage.setPayload((JSONObject.toJSONString(message)).getBytes());
        return mqttMessage;
    }

    /**
     * 获得Message
     */
    public Message getMessage(int type, MesBody body){
        Message message = new Message();
        message.setMsg_id(WebConfig.SERVERTYPE + String.valueOf(System.currentTimeMillis())); // 消息id
        message.setSource_mac(WebConfig.MACADDRESS); // mac地址
        message.setMsg_type(type);
        message.setSource_type(WebConfig.SERVERTYPE); // 服务类型
        message.setCreate_time(System.currentTimeMillis()); // 时间戳
        message.setBody(body);
        return message;
    }

    /**
     * 获得MesBody
     */
    public MesBody getMesBody(int subType, Object... object){
        List<String> list = new ArrayList<>();
        //索引遍历
        for(int i=0;i<object.length;i++) {
            list.add(JSONObject.toJSONString(object[i]));
        }

        MesBody body = new MesBody();
        body.setSub_type(subType);
        body.setContext(list);
        return body;
    }

    /**
     * 注册信息
     */
    public RegistInfo getRegistInfo(){
        RegistInfo registInfo = new RegistInfo();
        registInfo.setHost("");
        registInfo.setMacAddress(WebConfig.MACADDRESS);
        registInfo.setRemark("我是web管理服务的注册信息");
        registInfo.setServerName("web管理服务");
        registInfo.setServerType(WebConfig.SERVERTYPE);
        return registInfo;
    }

    /**
     * 服务的状态信息
     */
    public StatusInfo getStatusInfo(){
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setHost("");
        statusInfo.setMacAddress(WebConfig.MACADDRESS);
        statusInfo.setServerType(WebConfig.SERVERTYPE);
        statusInfo.setServerName(WebConfig.SERVERNAME);
        statusInfo.setRemark(WebConfig.SERVERREMARK);
        statusInfo.setCreatetime(System.currentTimeMillis());
        // TODO 写一个工具类，专门提供相应的数据
        statusInfo.setNet_usage(1.0f);
        statusInfo.setMemory_used(2);
        statusInfo.setMemory_free(3);
        statusInfo.setMem_usage(4.0f);
        statusInfo.setIo_usage(5);
        statusInfo.setCpu_usage(6);
        statusInfo.setCpu_temperature(7);
        return statusInfo;
    }

    /**
     * 服务的心跳信息
     */
    public PingInfo getPingInfo(){
        PingInfo pingInfo = new PingInfo();
        pingInfo.setCreatetime(System.currentTimeMillis());
        pingInfo.setHost("");
        pingInfo.setMacAddress(WebConfig.MACADDRESS);
        pingInfo.setServerName(WebConfig.SERVERNAME);
        pingInfo.setServerType(WebConfig.SERVERTYPE);
        pingInfo.setRemark(WebConfig.SERVERREMARK);
        return pingInfo;
    }

}
