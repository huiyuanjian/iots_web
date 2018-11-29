package io.renren.modules.iots.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.iots.dao.IotsIoserverInfoDao;
import io.renren.modules.iots.entity.*;
import io.renren.modules.iots.entity.common.CommonConfig;
import io.renren.modules.iots.entity.proxy.DeviceInfo;
import io.renren.modules.iots.entity.proxy.VarInfo;
import io.renren.modules.iots.service.*;
import io.renren.modules.iots.utils.config.WebConfig;
import io.renren.modules.iots.utils.mqtt.CtrlResult;
import io.renren.modules.iots.utils.mqtt.MessageManage;
import io.renren.modules.iots.utils.mqtt.MqttUtils;
import io.renren.modules.iots.utils.templates.MessageTemplates;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("iotsIoserverInfoService")
public class IotsIoserverInfoServiceImpl extends ServiceImpl<IotsIoserverInfoDao, IotsIoserverInfoEntity> implements IotsIoserverInfoService {

	@Autowired
	private IotsRegistInfoServerI iotsRegistInfoServerI;

	@Autowired
	private IotsIotserverService iotsIotserverService;

	@Autowired
	private IotsEquipmentInfoService iotsEquipmentInfoService;

	@Autowired
	private IotsVariableInfoService iotsVariableInfoService;

	MessageTemplates messageTemplates = new MessageTemplates();

	@Override
	public PageUtils queryPage(Map<String, Object> params) {

		String name = (String) params.get("name");

		Page<IotsIoserverInfoEntity> page = new Page<>();
		page.setRecords(this.baseMapper.selectAll(null));
		return new PageUtils(page);
	}

	@Override
	public List<IotsIoserverInfoEntity> queryIOServerForPid(Long pid) {

		return baseMapper.queryIOServerForPid(pid);
	}

	@Override
	public R syncEqmAndVar(Map<String, String> params){

		try{
			Long id = Long.parseLong(params.get("id"));// ioserver 的 id
			IotsIoserverInfoEntity IoserverEntity = baseMapper.selectById(id);
			//根据 ioserverinfo 查询物联网对应的mac 地址
			String mac = baseMapper.selectMacByIoserverId(IoserverEntity.getPid());
			List<String> macs = baseMapper.selectAllMac(IoserverEntity.getPid());
			IotsIotserverEntity iotsIotserverEntity = iotsIotserverService.selectById(IoserverEntity.getPid());
			// 消息体
			MesBody body = new MesBody();
			body.setSub_type(2);
			CommonConfig cfg = new CommonConfig();
			cfg.setServer_id(IoserverEntity.getId().toString());
			cfg.setServer_type("PROXY");
			cfg.setServer_name(IoserverEntity.getName());
			cfg.setServer_remark(IoserverEntity.getRemark());
			cfg.setServer_mac(IoserverEntity.getIp());
			cfg.setMac_address_list(macs);
			cfg.setKafka_topic(iotsIotserverEntity.getTopic());
			List<String> config = new ArrayList<>();
			config.add(JSONObject.toJSONString(cfg));
			body.setContext(config);

			// 消息
			Message message = new Message();
			message.setMsg_id(WebConfig.SERVERTYPE + System.currentTimeMillis());
			message.setSource_mac(WebConfig.MACADDRESS);
			message.setSource_type(WebConfig.SERVERTYPE);
			message.setCreate_time(System.currentTimeMillis());
			message.setCallback_id(""); // 管理mqtt时使用，用作key
			message.setMsg_type(4); // 控制消息类型
			message.setLicense(""); // TODO 还没确定怎么用
			message.setBody(body);

			// mqtt消息
			String msg = JSONObject.toJSONString(message);
			MqttMessage mqttMessage = new MqttMessage();
			mqttMessage.setQos(1);
			mqttMessage.setRetained(true);
			mqttMessage.setPayload(msg.getBytes());

			// 发布控制命令
			String topic = WebConfig.SERVERTYPE + "/COLLECT/" + mac + "/CONFIG";
			MqttUtils mqttUtils = new MqttUtils();
			mqttUtils.publish(topic,mqttMessage);
			// 放进管理容器中
			MessageManage.add(message.getMsg_id(),message);
			return R.ok().put("msg_id", message.getMsg_id());
		} catch (Exception e){
			return R.error("发生异常, 请联系管理员!");
		}
	}

	/**
	 *  根据 变量的id ，查询 IOServer 的数据 。传入参数为 变量的ids
	 */
	@Override
	public List<IotsIoserverInfoEntity> queryIOServerForVarId(Long[] varIds){
		return baseMapper.queryIOServerForVarId(varIds);
	}

	@Override
	public Page<IotsIoserverInfoEntity> roleIotServerInfo(Map<String, Object> params) {
		/*int page = Integer.parseInt(params.get("page").toString()) -1;
		//页大小
		int limit = Integer.parseInt(params.get("limit").toString());
		params.put("page",page);
		params.put("limit",limit);*/
		Page<IotsIoserverInfoEntity> p =new Page<IotsIoserverInfoEntity>();
		List<IotsIoserverInfoEntity> iotsIoserverInfoEntities = this.baseMapper.roleIotServerInfo(params);
		if (iotsIoserverInfoEntities != null && iotsIoserverInfoEntities.size() > 0){
			try{
				if (iotsIoserverInfoEntities.get(0) != null){
					p.setRecords(iotsIoserverInfoEntities);
				}
			}catch (Exception e){}
		}
		/*p.setTotal(this.baseMapper.roleIotServerInfoCount(params));
		p.setCurrent(page + 1);
		p.setSize(limit);*/
		return p;
	}
	@Override
	public Page<IotsIoserverInfoEntity> roleIotServerInfoList(Map<String, Object> params) {
		int page = Integer.parseInt(params.get("page").toString()) - 1;
		//页大小
		int limit = Integer.parseInt(params.get("limit").toString());
		params.put("page",page);
		params.put("limit",limit);
		Page<IotsIoserverInfoEntity> p =new Page<IotsIoserverInfoEntity>();
		List<IotsIoserverInfoEntity> iotsIoserverInfoEntities = this.baseMapper.roleIotServerInfo(params);
		if (iotsIoserverInfoEntities != null && iotsIoserverInfoEntities.size() > 0){
			try{
				if (iotsIoserverInfoEntities.get(0) != null){
					p.setRecords(iotsIoserverInfoEntities);
				}
			}catch (Exception e){}
		}
		p.setTotal(this.baseMapper.roleIotServerInfoListCount(params));
		p.setCurrent(page + 1);
		p.setSize(limit);
		return p;
	}

	@Override
	public Object queryIOServerAndIoServerInfo(Map<String, Object> params) {
		// 根据有没有请求参数有两套规格,1.没有
		if (params == null || params.size() == 0){
			List<IotsIotserverEntity> IotsIotservers = iotsIotserverService.selectAll(null);
			if (IotsIotservers != null && IotsIotservers.size() > 0){
				//遍历查询ioserverinfo
				for(IotsIotserverEntity entity : IotsIotservers){
					List<IotsIoserverInfoEntity> iotsIoserverInfoEntities = baseMapper.queryIOServerForPid(entity.getId());
					entity.setList(iotsIoserverInfoEntities);
				}
			}
			return IotsIotservers;
		}
		// 走到这说明有请求参数
		//根据参数查询 物联网接口有没有
		List<IotsIotserverEntity> name = iotsIotserverService.selectAll(params);
		if (name != null && name.size() > 0){
			// 说明有, 这里直接根据条件筛选ioserver和ioserverinfo
			List<IotsIotserverEntity> list = this.baseMapper.selectAllByName(params.get("name").toString());
			return list;
		}
		//只查询ioserverinfo
		List<IotsIotserverEntity> list = this.baseMapper.selectioserverInfoByName(params.get("name").toString());
		return list;
	}

	@Override
	public List<IotsIoserverInfoEntity> groupAndEquipmentAndVariable(Map<String, String> param) {
		List<IotsIoserverInfoEntity> iotsIoserverInfoEntities = null;
		if (param != null && param.size() > 0){
			String ids = param.get("ids");
			if (ids != null && !"".equals(ids)){
				List<String> idList = Arrays.asList(ids.trim().split(","));
				final List<Long> longIds = new ArrayList<>();
				for (String id: idList) {
					longIds.add(Long.valueOf(id.trim()).longValue());
				}
				iotsIoserverInfoEntities = this.baseMapper.selectByIds(longIds);
			} else {
				iotsIoserverInfoEntities = this.baseMapper.selectAll(null);
			}
		} else {
			iotsIoserverInfoEntities = this.baseMapper.selectAll(null);
		}
		//先查询所有的IOServerInfo
		if (iotsIoserverInfoEntities != null && iotsIoserverInfoEntities.size() > 0){
			for (IotsIoserverInfoEntity iotsIoserverInfoEntity : iotsIoserverInfoEntities) {
				String name = null;
				if (param != null && param.size() > 0){
					name = param.get("name");
				}
				//根据ID查询所有设备和设备下的变量
				Long iotsIoserverInfoId = iotsIoserverInfoEntity.getId();
				List<String> ids = new ArrayList<>(0);
				ids.add(String.valueOf(iotsIoserverInfoId));
				List<IotsEquipmentInfoEntity> iotsEquipmentInfoEntities = iotsEquipmentInfoService.queryEqmForPid(ids,name);
				// 将设备添加到设备
				iotsIoserverInfoEntity.setList(iotsEquipmentInfoEntities);
				if (iotsEquipmentInfoEntities != null && iotsEquipmentInfoEntities.size() > 0){
					// 根据ID查询所有变量
					iotsEquipmentInfoEntities.stream().forEach(iotsEquipmentInfoEntity -> {
						Long id = iotsEquipmentInfoEntity.getId();
						Map<String, String> map = new HashMap<String, String>();
						map.put("pid",String.valueOf(id));
						List<IotsVariableInfoEntity> iotsVariableInfoEntities = iotsVariableInfoService.queryVarForPid(map);
						iotsEquipmentInfoEntity.setList(iotsVariableInfoEntities);
					});
				}
			}
		}
		return iotsIoserverInfoEntities;
	}

	@Override
	public String callback(String key) {
		R r = new R();
		CtrlResult ctrlResult = MessageManage.get(key);
		if (ctrlResult == null) {
			r.put("code",4);
			r.put("msg","关键字无效！");
		} else if (!ctrlResult.isAnswer()){
			r.put("code",5);
			r.put("msg","正在执行，请稍后再试！");
		} else if (ctrlResult.isResult()){
			r.put("code",0);
			r.put("msg","执行成功！");
			MessageManage.remove(key);
		} else {
			r.put("code",1);
			r.put("msg","执行失败！");
			MessageManage.remove(key);
		}
		return JSONObject.toJSONString(r);
	}


	/**
	 * 订阅调用的同步方法
	 * @param context
	 */
	@Override
	public void sync(List<String> context){
		try {
			if (context != null && context.size() > 0){
				String json = context.get(0);
				//进行json解析
				CommonConfig commonConfig = JSONObject.parseObject(json, CommonConfig.class);
				// 获取设备
				List<DeviceInfo> device_list = commonConfig.getDevice_list();
				// 现根据设备ID 查询看有没有数据, 没有就新增, 有就修改
				// 先把当前ioserver下的所有设备和变量的状态 致为 1 删除状态
				iotsEquipmentInfoService.deleteByIoserverId(commonConfig.getServer_id());
				List<IotsVariableInfoEntity> varList = iotsVariableInfoService.selectVarByIoserverId(commonConfig.getServer_id());
				if (varList != null && varList.size() > 0){
					varList.stream().forEach(var -> {
						if (var != null){
							iotsVariableInfoService.deleteById(var.getId());
						}
					});
				}
				if (device_list != null && device_list.size() > 0){
					for(DeviceInfo deviceInfo : device_list){
						IotsEquipmentInfoEntity eqEntity = iotsEquipmentInfoService.selectByDyncId(Long.parseLong(commonConfig.getServer_id().trim()),deviceInfo.getName());
						if (eqEntity == null){
							//新增
							IotsEquipmentInfoEntity iotsEquipmentInfoEntity = new IotsEquipmentInfoEntity();
							iotsEquipmentInfoEntity.setDyncId((long) deviceInfo.getDevice_id());
							iotsEquipmentInfoEntity.setEnable(1);
							iotsEquipmentInfoEntity.setName(deviceInfo.getName());
							iotsEquipmentInfoEntity.setPid(Long.parseLong(commonConfig.getServer_id().trim()));
							iotsEquipmentInfoEntity.setIsdel(0);
							Date date = new Date();
							iotsEquipmentInfoEntity.setCreatetime(date);
							iotsEquipmentInfoEntity.setUpdatetime(date);
							iotsEquipmentInfoService.insert(iotsEquipmentInfoEntity);
						} else {
							//修改
							eqEntity.setDyncId((long) deviceInfo.getDevice_id());
							eqEntity.setEnable(1);
							eqEntity.setName(deviceInfo.getName());
							eqEntity.setIsdel(0);
							Date date = new Date();
							eqEntity.setUpdatetime(date);
							iotsEquipmentInfoService.updateById(eqEntity);
						}
					}
				}
				//获取变量
				List<VarInfo> var_list = commonConfig.getVar_list();
				if (var_list != null && var_list.size() > 0){
					for(VarInfo arInfo : var_list){
						IotsVariableInfoEntity varEntity = iotsVariableInfoService.selectByDyncId((long)arInfo.getVar_id(), arInfo.getName(), arInfo.getDevice_name(), Long.parseLong(commonConfig.getServer_id().trim()));
						if (varEntity == null){
							//新增
							IotsVariableInfoEntity iotsVariableInfoEntity = new IotsVariableInfoEntity();
							iotsVariableInfoEntity.setDyncId((long) arInfo.getVar_id());
							iotsVariableInfoEntity.setEnable(1);
							Long pid = iotsVariableInfoService.selectByEquiNameAndIoserverId(arInfo.getDevice_name(),Long.parseLong(commonConfig.getServer_id().trim()));
							iotsVariableInfoEntity.setName(arInfo.getName());
							iotsVariableInfoEntity.setPid(pid);
							iotsVariableInfoEntity.setIsdel(0);
							Date date = new Date();
							iotsVariableInfoEntity.setCreatetime(date);
							iotsVariableInfoEntity.setUpdatetime(date);
							iotsVariableInfoService.insert(iotsVariableInfoEntity);
						} else {
							//修改
							varEntity.setDyncId((long) arInfo.getVar_id());
							varEntity.setEnable(1);
							varEntity.setName(arInfo.getName());
							varEntity.setIsdel(0);
							Date date = new Date();
							varEntity.setUpdatetime(date);
							iotsVariableInfoService.updateById(varEntity);
						}
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 下发配置接口
	 */
	@Override
	public void issuedByTheConfiguration(Long pid, String name) {
		//根据物联网ID 查询物联网接口信息
		IotsIotserverEntity iotsIotserver = iotsIotserverService.selectById(pid);
		// 发布控制命令
		String topic = WebConfig.SERVERTYPE + "/COLLECT/" + iotsIotserver.getIp() + "/CONFIG";
		MqttUtils mqttUtils = new MqttUtils();
		MessageTemplates messageTemplates = new MessageTemplates();
		Message message = messageTemplates.getMessage(4, messageTemplates.getMesBody(1, getIotserver(pid)));
		mqttUtils.publish(topic,messageTemplates.getMqttMessage(message));

		Message message2 = messageTemplates.getMessage(4, messageTemplates.getMesBody(3, getIOServer(name)));
		mqttUtils.publish(topic,messageTemplates.getMqttMessage(message2));

		// 放进管理容器中
		// 下发控制的配置
		// 先根据 物联网接口ID 查询所有被配置的控制接口
		List<IotsCtrlInfoEntity> iotsCtrlInfoEntities = this.baseMapper.selectAllCtrl(pid);
		// 判断管辖的所有管辖该物联网接口的控制接口是不是空
		if (iotsCtrlInfoEntities != null && iotsCtrlInfoEntities.size() > 0){
			// 说明该物联网接口被控制接口控制, 没有控制接口不发送
			List<String> macs = baseMapper.selectAllMac(pid);
				try {
					for(IotsCtrlInfoEntity entity : iotsCtrlInfoEntities){
						new MqttUtils().publish("WEB/CONTROL/"+entity.getIp()+"/CONFIG", messageTemplates.getMqttMessage(packIngEntity(iotsIotserver.getIp(),entity, macs)));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		MessageManage.add(message.getMsg_id(),message);

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

	@Override
	public Boolean testConnection(String s) {
		IotsRegistInfoEntity proxy = iotsRegistInfoServerI.queryByTypeAndMac("PROXY", s);
		if (proxy == null){
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void issuedcfgByDelete(String id) {
		// 查询 pid
		Long pid = this.baseMapper.selectPid(id);
		String mac = this.baseMapper.selectMacByPid(id);
		// 查询物联网接口
		IotsIotserverEntity iotsIotserver = iotsIotserverService.selectById(pid);
		// 查询所有没有删除的ioserver
		List<String> macs = this.baseMapper.selectOther(id, pid);
		// 封装配置 给物联网
		CommonConfig cfg = new CommonConfig();
		cfg.setServer_id(iotsIotserver.getId().toString());
		cfg.setServer_type("COLLECT");
		cfg.setServer_name(iotsIotserver.getName());
		cfg.setServer_remark(iotsIotserver.getRemark());
		cfg.setMac_address_list(macs);
		// 给采集代理
		CommonConfig cfg2 = new CommonConfig();
		cfg2.setServer_mac(mac);
		// 发布控制命令
		String topic = WebConfig.SERVERTYPE + "/COLLECT/" + iotsIotserver.getIp() + "/CONFIG";
		MqttUtils mqttUtils = new MqttUtils();
		MessageTemplates messageTemplates = new MessageTemplates();
		Message message2 = messageTemplates.getMessage(4, messageTemplates.getMesBody(3, cfg2));
		mqttUtils.publish(topic,messageTemplates.getMqttMessage(message2));

		Message message = messageTemplates.getMessage(4, messageTemplates.getMesBody(1, cfg));
		mqttUtils.publish(topic,messageTemplates.getMqttMessage(message));
		// 放进管理容器中
		MessageManage.add(message.getMsg_id(),message);
	}

	@Override
	public void issuedByTheConfigurationByUpdate(Long pid, String name) {
		//根据物联网ID 查询物联网接口信息
		IotsIotserverEntity iotsIotserver = iotsIotserverService.selectById(pid);
		// 物联网接口的配置信息topic
		String topic = WebConfig.SERVERTYPE + "/COLLECT/" + iotsIotserver.getIp() + "/CONFIG";
		// 采集代理的配置信息
		CommonConfig commonConfig = getIOServer(name);
		// 发布
		MqttUtils mqttUtils = new MqttUtils();
		MessageTemplates messageTemplates = new MessageTemplates();
		Message message = messageTemplates.getMessage(4, messageTemplates.getMesBody(3, commonConfig));
		/*Message message2 = messageTemplates.getMessage(4, messageTemplates.getMesBody(3, cfg2));
		mqttUtils.publish(topic,messageTemplates.getMqttMessage(message2));*/
		mqttUtils.publish(topic,messageTemplates.getMqttMessage(message));
		// 放进管理容器中
		MessageManage.add(message.getMsg_id(),message);
	}

	/**
	 * 获得物联网接口信息
	 */
	private CommonConfig getIotserver(long pid){
		//根据物联网ID 查询物联网接口信息
		IotsIotserverEntity iotsIotserver = iotsIotserverService.selectById(pid);
		// 查询到之后 , 查询所有的ioserver mac地址
		List<String> macs = this.baseMapper.selectAllMac(pid);
		CommonConfig commonConfig = new CommonConfig();
		commonConfig.setServer_id(iotsIotserver.getId().toString());
		commonConfig.setServer_type("COLLECT");
		commonConfig.setServer_name(iotsIotserver.getName());
		commonConfig.setServer_remark(iotsIotserver.getRemark());
		commonConfig.setMac_address_list(macs);
		commonConfig.setServer_mac(iotsIotserver.getIp());
		commonConfig.setKafka_topic(iotsIotserver.getTopic());
		return commonConfig;
	}

	/**
	 * 获得采集代理的配置信息
	 */
	private CommonConfig getIOServer(String name){
		IotsIoserverInfoEntity iotsIoserverInfoEntity = this.baseMapper.selectMacByName(name);
		CommonConfig commonConfig = new CommonConfig();
		commonConfig.setServer_id(iotsIoserverInfoEntity.getId().toString());
		commonConfig.setServer_type("PROXY");
		commonConfig.setServer_name(iotsIoserverInfoEntity.getName());
		commonConfig.setServer_remark(iotsIoserverInfoEntity.getRemark());
		commonConfig.setServer_mac(iotsIoserverInfoEntity.getIp());
		return commonConfig;
	}
}
