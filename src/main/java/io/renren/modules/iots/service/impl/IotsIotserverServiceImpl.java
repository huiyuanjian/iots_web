package io.renren.modules.iots.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.ParamUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.iots.dao.IotsIoserverInfoDao;
import io.renren.modules.iots.dao.IotsIotserverDao;
import io.renren.modules.iots.entity.*;
import io.renren.modules.iots.entity.common.CommonConfig;
import io.renren.modules.iots.service.*;
import io.renren.modules.iots.utils.config.WebConfig;
import io.renren.modules.iots.utils.mqtt.MessageManage;
import io.renren.modules.iots.utils.mqtt.MqttUtils;
import io.renren.modules.iots.utils.sys.SysUtils;
import io.renren.modules.iots.utils.templates.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service("iotsIotserverService")
@Slf4j
public class IotsIotserverServiceImpl extends ServiceImpl<IotsIotserverDao, IotsIotserverEntity> implements IotsIotserverService {
	@Autowired
	private IotsIotserverService iotsIotserverService;
	@Autowired
	private IotsIoserverInfoService iotsIoserverInfoService;	
	@Autowired
	private IotsEquipmentInfoService iotsEquipmentInfoService;
	@Autowired
	private IotsVariableInfoService iotsVariableInfoService;

	@Autowired
	private IotsRegistInfoServerI iotsRegistInfoServerI;

	@Resource
	private IotsIoserverInfoDao iotsIoserverDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
		Object currentPage = params.get("page");
		Object limit = params.get("limit");
		Page<IotsIotserverEntity> pageModel= new Page<>();
    	if (currentPage != null && limit != null){
			int pageSize =Integer.parseInt(params.get("limit").toString());
			int page =(Integer.parseInt(params.get("page").toString())-1)*pageSize;
			params.put("page",0);
			params.put("limit",10);
			List<IotsIotserverEntity> results = iotsIoserverDao.queryCollectByCondi(params);
			int total = iotsIoserverDao.queryCollectByCondiTotal(params);
			pageModel.setRecords(results);
			pageModel.setCurrent(0);
			pageModel.setTotal(total);
			pageModel.setSize(10);
		} else {
			List<IotsIotserverEntity> results = this.baseMapper.selectAll(null);
			pageModel.setRecords(results);
		}
        return new PageUtils(pageModel);
    }
    
    /**
	 * 发送所有连接 代理 的 配置到 IOTS
	 * 
	 * @author lfy.xys
	 * @date 2018年6月7日
	 *
	 * @return
	 */
	@Override
	public R sendAllIOTSConfig() {

		// 查询 所有 IOTS
		List<IotsIotserverEntity> IotsList = iotsIotserverService.selectList(null);
		
		// 每个IOTS 发送一个数据
		for (IotsIotserverEntity IotsEntity : IotsList) {
			// 查询数据库，根据 iots的id  得到的  绑定的 ioserver。 即 使用pid查询。
			List<IotsIoserverInfoEntity> list = iotsIoserverInfoService.queryIOServerForPid(IotsEntity.getId());
			
			if(list.size() == 0) {
				continue;
			}
			
//			SingleSocketWriteThread.sendMsg( IotsEntity.getIp(), IotsEntity.getPort(), getIOTPBean(list));
		}

		return R.ok().put("result", ParamUtils.TIP_TRUE).put("msg", "已经发送配置。");
	}

	
	/**
	 * 根据 ids，查询需要发送配置 的 IOTS
	 * 
	 * @author lfy.xys
	 * @date 2018年6月7日
	 *
	 * @param params
	 * @return
	 */
	@Override
	public R sendIOTSConfig(Map<String, String> params) {

		// ids 为 IOTS 的id集合，逗号分割。
		String idsStr = (String) params.get("ids");
		String[] ids = idsStr.split(",");
		
		// 每个IOTS 发送一个数据
		for (String id : ids) {
			IotsIotserverEntity IotsEntity = iotsIotserverService.selectById(Long.parseLong(id));
			
			// 查询数据库，根据 iots的id  得到的  绑定的 ioserver。 即 使用pid查询。
			List<IotsIoserverInfoEntity> list = iotsIoserverInfoService.queryIOServerForPid(Long.parseLong(id));
			
			if(list.size() == 0) {
				continue;
			}
			
//			SingleSocketWriteThread.sendMsg( IotsEntity.getIp(), IotsEntity.getPort(), getIOTPBean(list));

		}
		
		return R.ok().put("result", ParamUtils.TIP_TRUE).put("msg", "已经发送配置。");
	}

	@Override
	public R queryEqmVar(Map<String, String> params) {
		return null;
	}

	/**
	 * 查询分组下 的 设备 和 变量信息
	 * 
	 * @author lfy.xys
	 * @date 2018年6月25日
	 *
	 * @param params
	 * @return
	 */
	/*@Override
	public R queryEqmVar(@RequestParam Map<String, String> params) {

		// 1、先查询 设备
		List<IotsEquipmentInfoEntity> eqmEntitys = iotsEquipmentInfoService.queryEqmForPid(params);
		int n = eqmEntitys.size();
		if(n == 0) {
			// 没有设备数据
			return R.ok().put("child", eqmEntitys);
		}
		
		Long[] pids = new Long[n];// 设备的 id集合。
		for (int i = 0; i < n; i++) {
			pids[i] = eqmEntitys.get(i).getId();
		}

		// 2、查询 所有设备的 变量的 集合。
		List<IotsVariableInfoEntity> varEntitys = iotsVariableInfoService.queryByPidsMap(pids);

		// 3、根据 变量的 设备id，分配
		// 循环 遍历 数量小的数据。 所以 数量大的 数据 放外面。
		for (IotsVariableInfoEntity varEntity : varEntitys) {
			for (IotsEquipmentInfoEntity eqmEntity : eqmEntitys) {
				// 设备id = 变量pid，匹配上则放置到 子list中
				if (varEntity.getPid() == eqmEntity.getId()) {
					eqmEntity.getList().add(varEntity);
				}
			}
		}

		return R.ok().put("child", eqmEntitys);
	}*/

	@Override
	public List<IotsIotserverEntity> selectAll(Map<String, Object> params) {
		return this.baseMapper.selectAll(params);
	}

	/**
	 * 测试连接
	 * @param mac
	 */
	@Override
	public Boolean testConnection(String mac) throws Exception {
		IotsRegistInfoEntity proxy = iotsRegistInfoServerI.queryByTypeAndMac("COLLECT", mac);
		if (proxy == null){
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 封装CommonConfig
	 * @param serverType
	 * @param iotsIotserver
	 * @return
	 */
	private CommonConfig getCommonConfig(String serverType, IotsIotserverEntity iotsIotserver){
		CommonConfig cfg = new CommonConfig();
		List<String> macs = this.baseMapper.selectMacsById(iotsIotserver.getId());
		cfg.setServer_id(iotsIotserver.getId().toString());
		cfg.setServer_type(serverType);
		cfg.setServer_name(iotsIotserver.getName());
		cfg.setServer_remark(iotsIotserver.getRemark());
		cfg.setMac_address_list(macs);
		cfg.setKafka_topic(iotsIotserver.getTopic());
		cfg.setServer_mac(iotsIotserver.getIp());
		return cfg;
	}

	/**
	 * 发布消息
	 * @param
	 * @param mac
	 */
	private void sendMessage(String serverType, IotsIotserverEntity iotsIotserver, String mac){
		String topic = WebConfig.SERVERTYPE + "/COLLECT/" + mac + "/CONFIG";
		MqttUtils mqttUtils = new MqttUtils();
		MessageTemplates messageTemplates = new MessageTemplates();
		Message message = messageTemplates.getMessage(4, messageTemplates.getMesBody(1, getCommonConfig(serverType, iotsIotserver)));
		mqttUtils.publish(topic,messageTemplates.getMqttMessage(message));
		// 放进管理容器中
		log.info("放进管理容器中");
		MessageManage.add(message.getMsg_id(),message);
	}

	/**
	 * 下发配置
	 * @param iotsIotserver
	 */
	@Override
	public void issuedByTheConfiguration(IotsIotserverEntity iotsIotserver) {
		// 先根据名字和mac地址查询数据
		Map<String, Object> map = new HashMap();
		map.put("name", iotsIotserver.getName());
		map.put("ip", iotsIotserver.getIp());
		List<IotsIotserverEntity> iotsIotserverEntities = this.baseMapper.selectByMap(map);
		IotsIotserverEntity iotsIotserverEntity = iotsIotserverEntities.get(0);
		String mac = iotsIotserverEntity.getIp();
		sendMessage("COLLECT", iotsIotserverEntity, mac);
	}

	@Override
	public void issuedConfigByUpdate(IotsIotserverEntity iotsIotserver) {
		// 先根据ID 查询所有的IOSERVER 的mac 地址
		List<String> macs = this.baseMapper.selectMacsById(iotsIotserver.getId());
		// 下发配置
		String mac = iotsIotserver.getIp();
		sendMessage("COLLECT", iotsIotserver, mac);
	}

	/**
	 * 删除时下发配置
	 * @param
	 */
	@Override
	public void issuedConfigByDelete(Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		IotsIotserverEntity iotsIotserverEntity = iotsIotserverService.selectById(ids[0]);
		boolean b = iotsIotserverService.deleteBatchIds(Arrays.asList(ids));
		CommonConfig cfg = new CommonConfig();
		MessageTemplates messageTemplates = new MessageTemplates();
		MqttUtils mqttUtils = new MqttUtils();
		if (b){
			// 查询所有的ioserver 并下发配置
			List<IotsIoserverInfoEntity> entitys = this.baseMapper.selectAllIoserver(iotsIotserverEntity.getId());
			for(IotsIoserverInfoEntity iotsIoserveriInfo : entitys){
				Message message = messageTemplates.getMessage(4, messageTemplates.getMesBody(3, cfg));
				// 发布控制命令
				String topic = WebConfig.SERVERTYPE + "/COLLECT/" + iotsIoserveriInfo.getIp() + "/CONFIG";
				mqttUtils.publish(topic,messageTemplates.getMqttMessage(message));
				MessageManage.add(message.getMsg_id(),message);
			}
		}
		// 发送 物联网的配置
		Message message = messageTemplates.getMessage(4, messageTemplates.getMesBody(1, cfg));
		// 发布控制命令
		String topic = WebConfig.SERVERTYPE + "/COLLECT/" + iotsIotserverEntity.getIp() + "/CONFIG";
		mqttUtils.publish(topic,messageTemplates.getMqttMessage(message));
		MessageManage.add(message.getMsg_id(),message);
	}

	public boolean updateInfoyId(IotsIotserverEntity iotsIotserverEntity){
		int result= iotsIoserverDao.updateInfoyId(iotsIotserverEntity);
		return result>0?true:false;
	}

	@Override
	public void setStatus(String mac, String serverName, Integer status) {
		this.baseMapper.setStatus(mac, serverName, status);
	}

}
