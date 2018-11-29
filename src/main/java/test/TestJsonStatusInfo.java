package test;

import com.alibaba.fastjson.JSONObject;
import io.renren.modules.iots.entity.MesBody;
import io.renren.modules.iots.entity.Message;
import io.renren.modules.iots.entity.status.StatusInfo;
import io.renren.modules.iots.utils.mqtt.MqttUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 打印状态信息
 */
public class TestJsonStatusInfo {

    public static void main(String args[]){
        sendStatus("COLLECT","JU23HS8DK4HJ");
        sendStatus("PROXY","JU23H18DK4HJ");
        sendStatus("CONTROL","JU2DHS8DK4HJ");
        sendStatus("DISTRIBUTE","1U23HS8DK4HJ");
        sendStatus("WEB","JU23HS8D94HJ");
        sendStatus("COLLECT","JU230S8DK4HJ");
        sendStatus("PROXY","JU23HS9DK4HJ");
        sendStatus("CONTROL","JU232S8DK4HJ");
        sendStatus("DISTRIBUTE","JU20HS8DK4HJ");

    }


    public static void sendStatus(String servertype,String mac){
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setServerType(servertype);
        statusInfo.setServerName("我是物联网接口");
        statusInfo.setRemark("我是说明");
        statusInfo.setMemory_used(1);
        statusInfo.setMemory_free(2);
        statusInfo.setMem_usage(1.0f);
        statusInfo.setMacAddress(mac);
        statusInfo.setIo_usage(2.0f);
        statusInfo.setHost("192.134.213.123:8888");
        statusInfo.setCreatetime(System.currentTimeMillis());
        statusInfo.setCpu_usage(3.1f);
        statusInfo.setCpu_temperature(4.2f);

        List<String> list = new ArrayList <>();
        list.add(JSONObject.toJSONString(statusInfo));

        MesBody body = new MesBody();
        body.setSub_type(1);
        body.setContext(list);

        Message message = new Message();
        message.setSource_type(servertype);
        message.setSource_mac(mac);
        message.setMsg_type(5);
        message.setCreate_time(System.currentTimeMillis());
        message.setMsg_id(servertype + message.getCreate_time());
        message.setLicense("");
        message.setCallback_id("1234567890");
        message.setBody(body);

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(1);
        mqttMessage.setRetained(true);
        mqttMessage.setPayload((JSONObject.toJSONString(message)).getBytes());

        String topic = "STATUS/"+servertype+"/"+mac;
        MqttUtils mqttUtils = new MqttUtils();
        mqttUtils.publish(topic,mqttMessage);
    }


}
