package io.renren.modules.iots.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.iots.entity.IotsCtrlInfoEntity;
import io.renren.modules.iots.entity.IotsIotserverEntity;

import java.util.List;
import java.util.Map;

/**
 * IOT Server端 ，即分组表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface IotsIotserverService extends IService<IotsIotserverEntity> {

	PageUtils queryPage(Map<String, Object> params);

	R sendAllIOTSConfig();

	R sendIOTSConfig(Map<String, String> params);

	R queryEqmVar(Map<String, String> params);

	List<IotsIotserverEntity> selectAll(Map<String, Object> params);

	/**
	 * 测试连接
	 * @param mac
	 */
	Boolean testConnection(String mac) throws Exception;

	/**
	 * 下发配置
	 * @param iotsIotserver
	 *
	 *
	 */
	void issuedByTheConfiguration(IotsIotserverEntity iotsIotserver);

	/**
	 * 修改的时候下发配置
	 * @param iotsIotserver
	 */
	void issuedConfigByUpdate(IotsIotserverEntity iotsIotserver);

	/**
	 * 删除时下发配置
	 * @param params
	 */
	void issuedConfigByDelete(Map<String, String> params);

	/*
	 * @Author zcy
	 * @Description //TODO 修改
	 * @Date 18:01 2018/11/20
	 * @Param
	 * @return
	 **/
	boolean updateInfoyId(IotsIotserverEntity iotsIotserverEntity);
	/**
	 * 设置物联网接口在线还是离线
	 * @param mac
	 * @param serverName
	 * @param i
	 */
	void setStatus(String mac, String serverName, Integer status);
}
