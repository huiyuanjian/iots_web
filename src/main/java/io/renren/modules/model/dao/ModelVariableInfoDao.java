package io.renren.modules.model.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.renren.modules.model.entity.ModelVariableInfoEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 变量信息表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
public interface ModelVariableInfoDao extends BaseMapper<ModelVariableInfoEntity> {
	
	List<ModelVariableInfoEntity> queryVarForPid(Map<String, String> params);

	List<ModelVariableInfoEntity> queryByIdsMap(Long[] pids);
	
	void deleteAllData();

	List<ModelVariableInfoEntity> queryAllList();

	void add(List<ModelVariableInfoEntity> entitys);

	Long selectByDyncId(@Param("modelVariableId") Long modelVariableId);
}
