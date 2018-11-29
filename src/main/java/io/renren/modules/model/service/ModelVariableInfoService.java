package io.renren.modules.model.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.model.entity.ModelVariableInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 变量信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
public interface ModelVariableInfoService extends IService<ModelVariableInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<ModelVariableInfoEntity> queryByIdsMap(Long[] pids);

	List<ModelVariableInfoEntity> queryVarForPid(Map<String, String> params);

	List<ModelVariableInfoEntity> queryAllList();

	void add(List<ModelVariableInfoEntity> entitys);

	R syncData();
}

