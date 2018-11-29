package io.renren.modules.iots.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.renren.modules.iots.entity.IotsEquipmentInfoEntity;
import io.renren.modules.iots.entity.IotsVariableInfoEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 变量信息表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:08
 */
public interface IotsVariableInfoDao extends BaseMapper<IotsVariableInfoEntity> {

	List<IotsVariableInfoEntity> queryVarForPid(Map<String, String> params);

	List<IotsVariableInfoEntity> queryByPidsMap(Long[] pids);

	List<IotsVariableInfoEntity> queryByIdsMap(Long[] pids);

	IotsVariableInfoEntity selectByDyncId(@Param("varId") Long varId, @Param("varName") String varName, @Param("equiName") String equiName, @Param("id") Long id);

	Long selectByEquiNameAndIoserverId(@Param("deviceName") String deviceName, @Param("id") Long id);

	List<IotsVariableInfoEntity> selectVarByIoserverId(@Param("serverId") String serverId);
}
