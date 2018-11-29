package io.renren.modules.iots.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.iots.entity.IotsIoserverInfoEntity;
import io.renren.modules.iots.entity.IotsIotserverEntity;

/**
 * IOServer信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface IotsIoserverInfoService extends IService<IotsIoserverInfoEntity> {

	PageUtils queryPage(Map<String, Object> params);

	List<IotsIoserverInfoEntity> queryIOServerForPid(Long pid);

	R syncEqmAndVar(Map<String, String> params);

	List<IotsIoserverInfoEntity> queryIOServerForVarId(Long[] varIds);

	Page<IotsIoserverInfoEntity> roleIotServerInfo(Map<String, Object> params);

	Page<IotsIoserverInfoEntity> roleIotServerInfoList(Map<String, Object> params);

	Object queryIOServerAndIoServerInfo(Map<String, Object> params);

	List<IotsIoserverInfoEntity> groupAndEquipmentAndVariable(Map<String, String> param);

	String callback(String key);

	void sync(List<String> context);

	void issuedByTheConfiguration(Long pid, String name);

	Boolean testConnection(String s);

	void issuedcfgByDelete(String id);

	void issuedByTheConfigurationByUpdate(Long pid, String name);
}
