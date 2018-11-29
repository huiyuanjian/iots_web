package io.renren.modules.model.service.impl;

import java.util.*;

import io.renren.datasources.DataSourceNames;
import io.renren.datasources.annotation.DataSource;
import io.renren.modules.iots.entity.*;
import io.renren.modules.iots.entity.common.CommonConfig;
import io.renren.modules.iots.entity.monitor.ChildAndParent;
import io.renren.modules.iots.entity.proxy.DeviceInfo;
import io.renren.modules.iots.entity.proxy.PackageInfo;
import io.renren.modules.iots.entity.proxy.VarInfo;
import io.renren.modules.iots.service.IotsIoserverInfoService;
import io.renren.modules.iots.utils.config.WebConfig;
import io.renren.modules.iots.utils.mqtt.MessageManage;
import io.renren.modules.iots.utils.mqtt.MqttUtils;
import io.renren.modules.iots.utils.templates.MessageTemplates;
import io.renren.modules.model.entity.ModelEquipmentInfoEntity;
import io.renren.modules.model.entity.ModelVariableInfoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.model.dao.ModelBindRelationDao;
import io.renren.modules.model.entity.ModelBindRelationEntity;
import io.renren.modules.model.service.ModelBindRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("modelBindRelationService")
public class ModelBindRelationServiceImpl extends ServiceImpl<ModelBindRelationDao, ModelBindRelationEntity> implements ModelBindRelationService {

    private static Logger logger = LoggerFactory.getLogger(ModelBindRelationServiceImpl.class);

    @Autowired
    private ModelBindRelationService modelBindRelationService;

    @Autowired
    private IotsIoserverInfoService iotsIoserverInfoService;

    @Value("${acquisition.domain.name}")
    private String DOMAIN;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ModelBindRelationEntity> page = this.selectPage(
                new Query<ModelBindRelationEntity>(params).getPage(),
                new EntityWrapper<ModelBindRelationEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据 model分组的ids，查询 所有有 绑定 的关系
     */
    @Override
    public List<ModelBindRelationEntity> queryBindRelation(Long[] modelIds){
    	return baseMapper.queryBindRelation(modelIds);
    }

    /**
     * 查询 第二个数据源的数据
     *
     * @author yeyaowen
     * @date 2018年11月5日
     *
     * @return
     */
    @DataSource(name = DataSourceNames.SECOND)
    @Override
    public List<ModelBindRelationEntity> queryAllList() {
        return this.baseMapper.selectList(null);
    }

    @Override
    public List<ModelBindRelationEntity> selectByDyncId(Long dyncId) {
        return this.baseMapper.selectByDyncId(dyncId);
    }

    /**
     * 绑定变量
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bind(Map<String, String> param) {
        if (param != null && param.size() > 0){
            String dyncId = param.get("dyncId");
            String ids = param.get("ids");
            Long newDyncId = Long.valueOf(dyncId.trim());
            List<ModelBindRelationEntity> idList = new ArrayList<>();
            ModelBindRelationEntity modelBindRelation = null;
            for(String id : ids.split(",")){
                Long newId = Long.valueOf(id.trim());
                modelBindRelation = new ModelBindRelationEntity();
                modelBindRelation.setWebId(newId);
                modelBindRelation.setModelId(newDyncId);
                modelBindRelation.setIsflag(0);
                idList.add(modelBindRelation);
            }
            //先根据ID查询出所有的绑定的关系, 并全部删除
            List<ModelBindRelationEntity> modelBindRelationEntities = this.baseMapper.selectByDyncId(newDyncId);
            if (modelBindRelationEntities != null && modelBindRelationEntities.size() > 0){
                modelBindRelationEntities.stream().forEach(entity -> {
                    entity.setIsflag(1);
                    this.baseMapper.updateById(entity);
                });
            }
            //把新的变量关系保存到数据库
            this.insertBatch(idList);

            //TODO 2018-11-15 目前变量绑定 一个变量绑定一个变量
            String varId = ids.split(",")[0];
            // 根据绑定的变量查询ioserver 信息;
            IotsIoserverInfoEntity ioserver = this.baseMapper.selectIoserverByVarid(varId);
            // 获取ioserver 的mac地址
            String mac = this.baseMapper.selectMacByIoserverInfoId(ioserver.getPid());
            // 根据ioserver 查询所有的设备和变量
            Map<String, String> map = new HashMap<>();
            map.put("ids", ioserver.getId().toString());
            List<IotsIoserverInfoEntity> iotsIoserverInfoEntities = iotsIoserverInfoService.groupAndEquipmentAndVariable(map);
            IotsIoserverInfoEntity iotsIoserverInfoEntity = iotsIoserverInfoEntities.get(0);
            List<IotsEquipmentInfoEntity> list1 = iotsIoserverInfoEntity.getList();
            // 获取所有的设备

            // 封装config
            CommonConfig cfg = new CommonConfig();
            cfg.setServer_id(ioserver.getId().toString());
            cfg.setServer_type("COLLECT");
            cfg.setServer_name(ioserver.getName());
            cfg.setServer_remark(ioserver.getRemark());
            cfg.setServer_mac(ioserver.getIp());
            cfg.setDomain(DOMAIN);
            // 封装设备 变量 数据包
            // 设备
            List<DeviceInfo> device_list = new ArrayList<>();
            // 根据
            //变量
            List<VarInfo> var_list = new ArrayList<>();
            for(IotsEquipmentInfoEntity iotsEquipmentInfoEntity : list1){
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setId(iotsEquipmentInfoEntity.getDyncId().intValue());
                deviceInfo.setName(iotsEquipmentInfoEntity.getName());
                deviceInfo.setTime_out(60);
                device_list.add(deviceInfo);
                List<IotsVariableInfoEntity> list = iotsEquipmentInfoEntity.getList();
                for(IotsVariableInfoEntity variableInfoEntity : list){
                    VarInfo varInfo = new VarInfo();
                    //IotsVariableInfoEntity variableInfoEntity = this.baseMapper.selectVarByDyncId(dyncId);
                    varInfo.setId(variableInfoEntity.getId().intValue());
                    varInfo.setVar_id(variableInfoEntity.getDyncId().intValue());
                    varInfo.setName(variableInfoEntity.getName());
                    varInfo.setDevice_id(iotsEquipmentInfoEntity.getId().intValue());
                    varInfo.setDevice_name(iotsEquipmentInfoEntity.getName());
                    varInfo.setInfo(variableInfoEntity.getRemark());
                    varInfo.setPack_type(0);
                    varInfo.setTime_out(60);
                    varInfo.setTime_span(60);
                    var_list.add(varInfo);
                };
            }
            //数据包
            List<PackageInfo> pack_list = new ArrayList<>();
            // 查询所有绑定的设备
            List<ModelEquipmentInfoEntity> entitys = this.baseMapper.selectAllEqui(iotsIoserverInfoEntity.getId());
                for(ModelEquipmentInfoEntity modelEquipmentInfoEntity : entitys){
                    PackageInfo packageInfo = new PackageInfo();
                    // 封装设备信息
                    packageInfo.setId(modelEquipmentInfoEntity.getDyncId().intValue());
                    packageInfo.setName(modelEquipmentInfoEntity.getName());
                    packageInfo.setGroup_id(this.getNameLine(this.get(new ArrayList<>(), modelEquipmentInfoEntity.getDyncId())));
                    packageInfo.setPack_type(0);
                    packageInfo.setTime_out(60);
                    packageInfo.setTime_span(5);
                    // 查询该设备下的所有已经绑定 的变量的ID
                    List<Long> idsList = this.baseMapper.selectAllBindVarIds(modelEquipmentInfoEntity.getDyncId(), null);
                    List<Integer> isList = new ArrayList<>();
                    for(Long id : idsList){
                        isList.add(id.intValue());
                    }
                    packageInfo.setVar_list(isList);
                    pack_list.add(packageInfo);
                }
            cfg.setDevice_list(device_list);
            cfg.setPack_list(pack_list);
            cfg.setVar_list(var_list);

            ModelVariableInfoEntity modelVariableInfoEntity = this.baseMapper.selectModelVarByDyncId(dyncId);
            List<Integer> list = new ArrayList<>();
            for(String id : ids.split(",")){
                list.add(Integer.parseInt(id));
            }
            //packageInfo.set
            // 发布控制命令
            String topic = WebConfig.SERVERTYPE + "/COLLECT/" + mac + "/CONFIG";
            MqttUtils mqttUtils = new MqttUtils();
            MessageTemplates messageTemplates = new MessageTemplates();
            Message message = messageTemplates.getMessage(4, messageTemplates.getMesBody(3, cfg));
            mqttUtils.publish(topic,messageTemplates.getMqttMessage(message));
            // 放进管理容器中
            MessageManage.add(message.getMsg_id(),message);
        }
    }

    private List<String> get(List<String> name, Long id){
        List<ChildAndParent> childAndParents = this.baseMapper.selectPNamesByChildId(id);
        if (childAndParents != null && childAndParents.size() > 0){
            // 有数据继续查询父节点
            name.add(childAndParents.get(0).getName());
            this.get(name,childAndParents.get(0).getId());
        }
        return name;
    }

    public String getNameLine(List<String> names){
        Collections.reverse(names);
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < names .size(); i++){
            if (i < names.size() - 1){
                sb.append(names.get(i) + "/");
            } else {
                sb.append(names.get(i));
            }

        }
        return sb.toString();
    }

    /**
     * 解除绑定变量
     * @param param
     */
    @Override
    @Transactional
    public void noBind(Map<String, String> param) {
        if (param != null && param.size() > 0){
            String dyncId = param.get("dyncId");
            String ids = param.get("ids");
            Long newDyncId = Long.valueOf(dyncId.trim());
            for(String id : ids.split(",")){
                Long newId = Long.valueOf(id.trim());
                this.baseMapper.deleteByDyncId(newDyncId,newId);
            }
            //TODO 2018-11-15 目前变量绑定 一个变量绑定一个变量
            String varId = ids.split(",")[0];
            // 根据绑定的变量查询ioserver 信息;
            IotsIoserverInfoEntity ioserver = this.baseMapper.selectIoserverByVarid(varId);

            String mac = this.baseMapper.selectMacByIoserverInfoId(ioserver.getPid());
            // 根据ioserver 查询所有的设备和变量
            Map<String, String> map = new HashMap<>();
            map.put("ids", ioserver.getId().toString());
            List<IotsIoserverInfoEntity> iotsIoserverInfoEntities = iotsIoserverInfoService.groupAndEquipmentAndVariable(map);
            IotsIoserverInfoEntity iotsIoserverInfoEntity = iotsIoserverInfoEntities.get(0);
            List<IotsEquipmentInfoEntity> list1 = iotsIoserverInfoEntity.getList();
            // 获取所有的设备

            // 封装config
            CommonConfig cfg = new CommonConfig();
            cfg.setServer_id(ioserver.getId().toString());
            cfg.setServer_type("COLLECT");
            cfg.setServer_name(ioserver.getName());
            cfg.setServer_remark(ioserver.getRemark());
            cfg.setDomain(DOMAIN);
            cfg.setServer_mac(ioserver.getIp());
            // 封装设备 变量 数据包
            // 设备
            List<DeviceInfo> device_list = new ArrayList<>();
            // 根据
            //变量
            List<VarInfo> var_list = new ArrayList<>();
            for(IotsEquipmentInfoEntity iotsEquipmentInfoEntity : list1){
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setId(iotsEquipmentInfoEntity.getDyncId().intValue());
                deviceInfo.setName(iotsEquipmentInfoEntity.getName());
                deviceInfo.setTime_out(60);
                device_list.add(deviceInfo);
                List<IotsVariableInfoEntity> list = iotsEquipmentInfoEntity.getList();
                for(IotsVariableInfoEntity iotsVariableInfoEntity : list){
                    VarInfo varInfo = new VarInfo();
                    varInfo.setId(iotsVariableInfoEntity.getId().intValue());
                    varInfo.setVar_id(iotsVariableInfoEntity.getDyncId().intValue());
                    varInfo.setName(iotsVariableInfoEntity.getName());
                    varInfo.setDevice_id(iotsEquipmentInfoEntity.getId().intValue());
                    varInfo.setDevice_name(iotsEquipmentInfoEntity.getName());
                    varInfo.setInfo(iotsVariableInfoEntity.getRemark());
                    varInfo.setPack_type(0);
                    varInfo.setTime_out(60);
                    varInfo.setTime_span(60);
                    var_list.add(varInfo);
                };
            }
            //数据包
            List<PackageInfo> pack_list = new ArrayList<>();
            // 查询所有绑定的设备
            List<ModelEquipmentInfoEntity> entitys = this.baseMapper.selectAllEqui(iotsIoserverInfoEntity.getId());
            for(ModelEquipmentInfoEntity modelEquipmentInfoEntity : entitys){
                PackageInfo packageInfo = new PackageInfo();
                // 封装设备信息
                packageInfo.setId(modelEquipmentInfoEntity.getDyncId().intValue());
                packageInfo.setName(modelEquipmentInfoEntity.getName());
                packageInfo.setGroup_id(this.getNameLine(this.get(new ArrayList<>(), modelEquipmentInfoEntity.getDyncId())));
                packageInfo.setPack_type(0);
                packageInfo.setTime_out(60);
                packageInfo.setTime_span(5);
                // 查询该设备下的所有已经绑定 的变量的ID
                List<Long> idsList = this.baseMapper.selectAllBindVarIds(modelEquipmentInfoEntity.getDyncId(),Long.valueOf(dyncId));
                List<Integer> isList = new ArrayList<>();
                for(Long id : idsList){
                    isList.add(id.intValue());
                }
                packageInfo.setVar_list(isList);
                pack_list.add(packageInfo);
            }
            cfg.setDevice_list(device_list);
            cfg.setPack_list(pack_list);
            cfg.setVar_list(var_list);
            //packageInfo.set
            // 发布控制命令
            String topic = WebConfig.SERVERTYPE + "/COLLECT/" + mac + "/CONFIG";
            MqttUtils mqttUtils = new MqttUtils();
            MessageTemplates messageTemplates = new MessageTemplates();
            Message message = messageTemplates.getMessage(4, messageTemplates.getMesBody(3, cfg));
            mqttUtils.publish(topic,messageTemplates.getMqttMessage(message));
            // 放进管理容器中
            MessageManage.add(message.getMsg_id(),message);
        }
    }
}
