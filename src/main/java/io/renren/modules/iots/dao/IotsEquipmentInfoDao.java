package io.renren.modules.iots.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.renren.modules.iots.entity.EqmStateEntity;
import io.renren.modules.iots.entity.IotsEquipmentInfoEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 设备信息表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface IotsEquipmentInfoDao extends BaseMapper<IotsEquipmentInfoEntity> {

	List<IotsEquipmentInfoEntity> queryEqmForPid(@Param("ids") List<Long> ids,@Param("name") String name);
	
	List<EqmStateEntity> queryEqmStateMap();

    IotsEquipmentInfoEntity selectByDyncId(@Param("id") Long id, @Param("name") String name);

    void deleteByIoserverId(@Param("serverId") String serverId);
}
