package io.renren.modules.iots.service.impl;

import com.alibaba.fastjson.JSON;
import io.renren.common.utils.R;
import io.renren.modules.iots.dao.IotsIoserverInfoDao;
import io.renren.modules.iots.dao.IotsIotserverDao;
import io.renren.modules.iots.entity.*;
import io.renren.modules.iots.entity.common.CommonConfig;
import io.renren.modules.iots.utils.mqtt.MqttUtils;
import io.renren.modules.iots.utils.sys.SysUtils;
import io.renren.modules.iots.utils.templates.MessageTemplates;
import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.iots.dao.IotsCtrlInfoDao;
import io.renren.modules.iots.service.IotsCtrlInfoService;

import javax.annotation.Resource;


@Service("iotsCtrlInfoService")
public class IotsCtrlInfoServiceImpl extends ServiceImpl<IotsCtrlInfoDao, IotsCtrlInfoEntity> implements IotsCtrlInfoService {

    @Resource
    private IotsCtrlInfoDao iotsCtrlInfoDao;

    @Resource
    private IotsIoserverInfoDao iotsIoserverInfoDao;

    @Resource
    private IotsIotserverDao iotsIotserverDao;

    MessageTemplates messageTemplates = new MessageTemplates();


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String id = (String) params.get("id");
        String name = (String) params.get("name");
        String ioserverName = (String) params.get("ioserverName");
        Page<IotsCtrlInfoEntity> page = this.selectPage(
                new Query<IotsCtrlInfoEntity>(params).getPage(),
                new EntityWrapper<IotsCtrlInfoEntity>()
                        .like(StringUtils.isNotBlank(name), "name", name)
                        .eq(StringUtils.isNotBlank(id), "id", id)
                        .like(StringUtils.isNotBlank(ioserverName), "ioserver_name", ioserverName)
        );

        return new PageUtils(page);
    }

    /*
     * @Author zcy
     * @Description //TODO 新增
     * @Date 14:23 2018/11/15
     * @Param
     * @return
     **/
    @Override
    public R newCtrlInfo(Map<String, String> params) {
        params.put("field1", SysUtils.getIdentifyingCode());//识别码
        int result = iotsCtrlInfoDao.newCtrlInfo(params);
        if (result==1) {
            // 下发配置
            return R.ok();
        }else{
            return R.error(1,"保存失败");
        }
    }

    /*
     * @Author zcy
     * @Description //TODO 修改控制接口管理
     * @Date 14:23 2018/11/15
     * @Param
     * @return
     **/
    public R updateInfo(Map<String, String> params){
        int result = iotsCtrlInfoDao.updateInfo(params);
        if (result==1) {
            return R.ok();
        }else{
            return R.error(1,"保存失败");
        }
    }

    /**
     * @Author zcy
     * @Description //TODO 配置接口下发mqtt
     * @Date 15:12 2018/11/15
     * @Param [params]
     * @return io.renren.common.utils.R
     **/
    public R configSendData(Map<String, String> params)  {
        //查询 物联网管理 ioserver
        try {
            String ctrlId= params.get("ctrlId");//控制id
            String THId= params.get("THId");//选择的 物联网id
            IotsCtrlInfoEntity ctrlEntity = iotsCtrlInfoDao.getCtrlInfoById(ctrlId);
            List<String> ioserverMACs = iotsIoserverInfoDao.queryMacById(THId);
            IotsIotserverEntity model = iotsIotserverDao.getCollectById(Long.parseLong(THId));
            if (ctrlEntity!=null&& ioserverMACs!=null&&ioserverMACs.size()>0&&model!=null) {
                MqttUtils mqttUtils = new MqttUtils();
                Message message = packIngEntity(model.getIp(),ctrlEntity, ioserverMACs);
                MqttMessage mqttMessage  = messageTemplates.getMqttMessage(message);
                String topic= "WEB/CONTROL/"+model.getIp()+"/CONFIG";
                boolean isok = mqttUtils.publish(topic, mqttMessage);
                if (isok)
                return R.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error(1,"下发失败！");
    }

    /**
     * 新增的时候下发配置
     * @param iotsCtrlInfo
     */
    @Override
    public void issuedByTheConfiguration(IotsCtrlInfoEntity iotsCtrlInfo) {
        this.publishCfg(iotsCtrlInfo, 0, null);
    }

    /**
     * 删除下发配置
     * @param ids
     */
    @Override
    public void issuedByTheConfigurationByDelete(String[] ids) {
        List<IotsCtrlInfoEntity> iotsCtrlInfoEntities = this.baseMapper.selectBatchIds(Arrays.asList(ids));
        if (iotsCtrlInfoEntities != null && iotsCtrlInfoEntities.size() > 0){
            iotsCtrlInfoEntities.stream().forEach(entity -> {
                this.publishCfg(entity,2, null);
            });
        }
    }

    /**
     * 修改下发配置
     * @param iotsCtrlInfo
     */
    @Override
    public void issuedByTheConfigurationByUpdate(IotsCtrlInfoEntity iotsCtrlInfo) {
        // 根据控制ID 查询所有的物联网接口和下边的ioserver
        List<Long> ioserverId = iotsCtrlInfo.getIoserverId();
        if (ioserverId != null && ioserverId.size() > 0){
            // 根据IDS 查询物联网接口
            List<IotsIotserverEntity> iotsIotserverEntities = iotsIotserverDao.selectBatchIds(ioserverId);
            List<Long> ids = new ArrayList<>();
            for(IotsIotserverEntity entity : iotsIotserverEntities){
                ids.add(entity.getId());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("pid", ids);
            List<IotsIoserverInfoEntity> iotsIoserverInfoEntities = iotsIoserverInfoDao.selectByPids(map);
            Map<String, List<String>> collect_proxy_map = new HashMap<>();
            for(IotsIotserverEntity mac : iotsIotserverEntities){
                String ip = mac.getIp();
                Long id = mac.getId();
                List<String> macs = new ArrayList<>();
                for(IotsIoserverInfoEntity iotsIoserverInfo : iotsIoserverInfoEntities){
                    Long pid = iotsIoserverInfo.getPid();
                    if (id.equals(pid)){
                        macs.add(iotsIoserverInfo.getIp());
                    }
                }
                collect_proxy_map.put(ip, macs);
            }
            this.publishCfg(iotsCtrlInfo,1,collect_proxy_map);
        } else {
            this.publishCfg(iotsCtrlInfo,1,null);
        }
    }

    /**
     * 下发配置封装
     * type 0 新增 ， 1 是修改 2, 是删除
     * @param iotsCtrlInfo
     */
    private void publishCfg(IotsCtrlInfoEntity iotsCtrlInfo, Integer type, Map<String, List<String>> collect_proxy_map){
        //下发配置
        //消息体
        MesBody mesBody= new MesBody();
        mesBody.setSub_type(1);
        CommonConfig cfg = new CommonConfig();
        if (type != null && type == 2){
            // 说明是删除的下发配置
        } else if(type != null && type == 0) {
            // 说明是新增的下发配置
            cfg.setServer_id(iotsCtrlInfo.getId().toString());
            cfg.setServer_name(iotsCtrlInfo.getName());
            cfg.setServer_type("CONTROL");
            cfg.setServer_remark(iotsCtrlInfo.getRemark());
            cfg.setIdentifying_code(iotsCtrlInfo.getField1());//身份识别码
        } else {
            // 说明是修改
            // 说明是修改的下发配置
            cfg.setServer_id(iotsCtrlInfo.getId().toString());
            cfg.setServer_name(iotsCtrlInfo.getName());
            cfg.setServer_type("CONTROL");
            cfg.setServer_remark(iotsCtrlInfo.getRemark());
            cfg.setIdentifying_code(iotsCtrlInfo.getField1());//身份识别码
            cfg.setCollect_proxy_map(collect_proxy_map);
        }
        List<String> list= new ArrayList<String>();
        list.add(JSON.toJSON(cfg).toString());
        mesBody.setContext(list);
        MqttUtils mqttUtils = new MqttUtils();
        String topic= "WEB/CONTROL/"+ iotsCtrlInfo.getIp() + "/CONFIG";
        mqttUtils.publish(topic, messageTemplates.getMqttMessage(messageTemplates.getMessage(8, mesBody)));
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
        map.put(thingMac,ioserverMACs);
        commonConfig.setCollect_proxy_map(map);
        List<String> list= new ArrayList<String>();
        list.add(JSON.toJSON(commonConfig).toString());
        mesBody.setContext(list);
       return messageTemplates.getMessage(8,mesBody);

    }

}
