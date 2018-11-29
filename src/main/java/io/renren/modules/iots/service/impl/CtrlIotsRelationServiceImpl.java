package io.renren.modules.iots.service.impl;

import com.alibaba.fastjson.JSON;
import io.renren.common.utils.R;
import io.renren.modules.iots.dao.IotsCtrlInfoDao;
import io.renren.modules.iots.dao.IotsIoserverInfoDao;
import io.renren.modules.iots.dao.IotsIotserverDao;
import io.renren.modules.iots.entity.*;
import io.renren.modules.iots.entity.common.CommonConfig;
import io.renren.modules.iots.utils.mqtt.MqttUtils;
import io.renren.modules.iots.utils.templates.MessageTemplates;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.iots.dao.CtrlIotsRelationDao;
import io.renren.modules.iots.service.CtrlIotsRelationService;

import javax.annotation.Resource;


@Service("ctrlIotsRelationService")
public class CtrlIotsRelationServiceImpl extends ServiceImpl<CtrlIotsRelationDao, CtrlIotsRelationEntity> implements CtrlIotsRelationService {

    @Resource
    private IotsCtrlInfoDao iotsCtrlInfoDao;

    @Resource
    private IotsIoserverInfoDao iotsIoserverInfoDao;

    @Resource
    private IotsIotserverDao iotsIotserverDao;

    MessageTemplates messageTemplates = new MessageTemplates();
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CtrlIotsRelationEntity> page = this.selectPage(
                new Query<CtrlIotsRelationEntity>(params).getPage(),
                new EntityWrapper<CtrlIotsRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<Long> selectByPid(Long id) {
        return this.baseMapper.selectByPid(id);
    }

    @Override
    public void deleteByCtrlId(Long id) {
        this.baseMapper.deleteByCtrlId(id);
    }

    @Override
    public void issuedByTheConfiguration(CtrlIotsRelationEntity ctrlIotsRelation) {
        String ctrlId= null;//控制id
        String THId= null;//选择的 物联网id
        if (ctrlIotsRelation.getCtrlId() != null) {
            ctrlId = ctrlIotsRelation.getCtrlId().toString();
        }
        if (ctrlIotsRelation.getIotserverId() != null) {
            THId = ctrlIotsRelation.getIotserverId().toString();
        }
        try{
            IotsCtrlInfoEntity ctrlEntity = iotsCtrlInfoDao.getCtrlInfoById(ctrlId);
            List<String> ioserverMACs = iotsIoserverInfoDao.queryMacById(THId);
            IotsIotserverEntity model = null;
            if (THId != null && !"".equals(THId)){
                model = iotsIotserverDao.getCollectById(Long.parseLong(THId));
            }
            if (ctrlEntity!=null&& ioserverMACs!=null&&ioserverMACs.size()>0&&model!=null) {
                MqttUtils mqttUtils = new MqttUtils();
                Message message = null;
                if (model != null){
                    message = packIngEntity(model.getIp(),ctrlEntity, ioserverMACs);
                } else {
                    message = packIngEntity(null,ctrlEntity, ioserverMACs);
                }
                MqttMessage mqttMessage  = messageTemplates.getMqttMessage(message);
                String topic= "WEB/CONTROL/"+ctrlEntity.getIp()+"/CONFIG";
                boolean isok = mqttUtils.publish(topic, mqttMessage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @Author zcy
     * @Description //TODO 封装下发类
     * @Date 15:19 2018/11/15
     * @Param []
     * @return io.renren.modules.iots.entity.Message
     **/
    private Message packIngEntity(String thingMac, IotsCtrlInfoEntity ctrlEntity, List<String> ioserverMACs) throws Exception {
        //消息体
        MesBody mesBody= new MesBody();
        mesBody.setSub_type(1);
        CommonConfig commonConfig= new CommonConfig();
        commonConfig.setServer_id(ctrlEntity.getId()+"");
        commonConfig.setServer_name(ctrlEntity.getName());
        commonConfig.setServer_type("CONTROL");
        commonConfig.setServer_remark(ctrlEntity.getRemark());
        commonConfig.setIdentifying_code(ctrlEntity.getField1());//身份识别码
        Map<String,List<String>> map= new HashMap<>();
        if (thingMac != null && !"".equals(thingMac)){
            map.put(thingMac,ioserverMACs);
        }
        commonConfig.setCollect_proxy_map(map);
        List<String> list= new ArrayList<String>();
        list.add(JSON.toJSON(commonConfig).toString());
        mesBody.setContext(list);
        return messageTemplates.getMessage(8,mesBody);

    }

}
