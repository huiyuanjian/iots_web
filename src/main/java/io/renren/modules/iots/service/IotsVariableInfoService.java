package io.renren.modules.iots.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.iots.entity.IotsEquipmentInfoEntity;
import io.renren.modules.iots.entity.IotsVariableInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 变量信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:08
 */
public interface IotsVariableInfoService extends IService<IotsVariableInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<IotsVariableInfoEntity> queryVarForPid(Map<String, String> params);

	List<IotsVariableInfoEntity> queryByPidsMap(Long[] pids);
	
	List<IotsVariableInfoEntity> queryByIdsMap(Long[] pids);

	IotsVariableInfoEntity selectByDyncId(Long varId, String varName, String equiName, Long id);

	Long selectByEquiNameAndIoserverId(String device_name, long parseLong);

    List<IotsVariableInfoEntity> selectVarByIoserverId(String server_id);
}

