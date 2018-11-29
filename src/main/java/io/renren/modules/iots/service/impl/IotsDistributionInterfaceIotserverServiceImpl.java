package io.renren.modules.iots.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.modules.iots.entity.IotsDistributionInterfaceEntity;
import io.renren.modules.iots.entity.IotsIotserverEntity;
import io.renren.modules.iots.entity.Message;
import io.renren.modules.iots.entity.common.CommonConfig;
import io.renren.modules.iots.service.IotsDistributionInterfaceService;
import io.renren.modules.iots.service.IotsIotserverService;
import io.renren.modules.iots.utils.config.WebConfig;
import io.renren.modules.iots.utils.mqtt.MessageManage;
import io.renren.modules.iots.utils.mqtt.MqttUtils;
import io.renren.modules.iots.utils.templates.MessageTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.modules.iots.dao.IotsDistributionInterfaceIotserverDao;
import io.renren.modules.iots.entity.IotsDistributionInterfaceIotserverEntity;
import io.renren.modules.iots.service.IotsDistributionInterfaceIotserverService;

/**
 * 分发接口-物联网接口-关系
 */
@Service
public class IotsDistributionInterfaceIotserverServiceImpl extends ServiceImpl<IotsDistributionInterfaceIotserverDao, IotsDistributionInterfaceIotserverEntity> implements IotsDistributionInterfaceIotserverService {

	@Autowired
	private IotsIotserverService iotsIotserverService;

	@Autowired
	private IotsDistributionInterfaceService iotsDistributionInterfaceService;

	@Transactional
	public void insertBatch2(List<IotsDistributionInterfaceIotserverEntity> entityList) {
		Map<String, Object> columnMap = new HashMap<>();
		columnMap.put("distribution_id", entityList.get(0).getDistributionId());
		this.deleteByMap(columnMap);
		if (entityList != null && entityList.size() > 0){
			this.insertBatch(entityList);
		}
	}

	@Override
	public List<Long> selectByDistributionId(Long id) {
		return this.baseMapper.selectByDistributionId(id);
	}

	@Override
	public void deleteByDistributionId(Long id) {
		//TODO 删除该消息，为了信息安全删除为逻辑删除，但是此处却是物理删除，有时间需要整改
		this.baseMapper.deleteByDistributionId(id);
	}

	@Override
	public void issuedByTheConfiguration(List<IotsDistributionInterfaceIotserverEntity> entityList) {

		List<String> topic = new ArrayList<>();

		for(IotsDistributionInterfaceIotserverEntity entity : entityList){
			Long iotserverId = entity.getIotserverId();
			// 根据ID 查询物联网接口
			IotsIotserverEntity iotsIotserverEntity = iotsIotserverService.selectById(iotserverId);
			topic.add(iotsIotserverEntity.getTopic());
		}
		// 查询分发接口
		Long distributionId = entityList.get(0).getDistributionId();
		IotsDistributionInterfaceEntity iotsDistributionInterfaceEntity = iotsDistributionInterfaceService.selectById(distributionId);
		// 发布配置信息
		sendConfig(iotsDistributionInterfaceEntity);
	}

	@Override
	public void setStatus(Long distributionId) {
		this.baseMapper.setStatus(distributionId);
	}

	/**
	 * 下发配置信息（默认是非删除时的配置信息）
	 */
	@Override
	public void sendConfig(IotsDistributionInterfaceEntity iotsDistributionInterfaceEntity){
		sendConfig(iotsDistributionInterfaceEntity,false);
	}

	/**
	 * 下发配置信息
	 * isdel 是true的时候表示是删除时的配置文件
	 * isdel 是false的时候是非删除时的配置文件
	 */
	@Override
	public void sendConfig(IotsDistributionInterfaceEntity iotsDistributionInterfaceEntity,boolean isdel){
		// 拿到mqtt工具类
		MqttUtils mqttUtils = new MqttUtils();
		// 拿到消息模版
		MessageTemplates messageTemplates = new MessageTemplates();
		// 配置信息
		CommonConfig commonConfig = isdel ? new CommonConfig() : getConfig(iotsDistributionInterfaceEntity);
		// message消息
		Message message = messageTemplates.getMessage(4, messageTemplates.getMesBody(0,commonConfig));
		// 发布消息
		mqttUtils.publish(getTopic(iotsDistributionInterfaceEntity), messageTemplates.getMqttMessage(message));
		// 放进管理容器中
		MessageManage.add(message.getMsg_id(),message);
	}

	@Override
	public boolean insertListOfIotserver( List <Long> list ,long distribution_id) {
		int index = baseMapper.inserListOfIotserver(list,distribution_id);
		if (list.size() == index) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据实体信息得到配置信息
	 */
	private CommonConfig getConfig(IotsDistributionInterfaceEntity iotsDistributionInterfaceEntity){
		CommonConfig commonConfig = new CommonConfig();
		commonConfig.setServer_id(String.valueOf(iotsDistributionInterfaceEntity.getId()));
		commonConfig.setServer_name(iotsDistributionInterfaceEntity.getName());
		commonConfig.setServer_remark(iotsDistributionInterfaceEntity.getRemark());
		commonConfig.setServer_type("DISTRIBUTE");
		// 分发接口服务管理的物联网接口服务的topic集合
		List<String> list = baseMapper.selectKafkaTopicById(iotsDistributionInterfaceEntity.getId());
		commonConfig.setMac_address_list(list);
		return commonConfig;
	}

	/**
	 * 根据实体信息得到mqtt的发布主题
	 */
	private String getTopic(IotsDistributionInterfaceEntity iotsDistributionInterfaceEntity){
		return WebConfig.SERVERTYPE + "/DISTRIBUTE/" + iotsDistributionInterfaceEntity.getIp() + "/CONFIG";
	}

}
