package io.renren.modules.model.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.renren.modules.model.entity.IoserverCollectorEntity;
import io.renren.modules.model.entity.ModelEquipmentInfoEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 设备信息表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
public interface ModelEquipmentInfoDao extends BaseMapper<ModelEquipmentInfoEntity> {
	List<ModelEquipmentInfoEntity> queryEqmForPid(Long pid);

    Long selectByDyncId(@Param("modelEquipmentId") Long modelEquipmentId);

    List<ModelEquipmentInfoEntity> queryEqmByGid(@Param("ids") List<Long> ids, @Param("nowPage") int nowPage, @Param("pageSize") int pageSize, @Param("name") String name);

    void insertCollector(IoserverCollectorEntity entity);

    void noBindCollector(IoserverCollectorEntity entity);

    IoserverCollectorEntity selectCollector(Long dyncId);
}
