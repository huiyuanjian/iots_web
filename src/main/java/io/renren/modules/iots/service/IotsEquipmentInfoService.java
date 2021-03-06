package io.renren.modules.iots.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.iots.entity.EqmStateEntity;
import io.renren.modules.iots.entity.IotsEquipmentInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface IotsEquipmentInfoService extends IService<IotsEquipmentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<IotsEquipmentInfoEntity> queryEqmForPid(List<String> ids,String name);

	List<EqmStateEntity> queryEqmStateMap();

	IotsEquipmentInfoEntity selectByDyncId(Long id, String name);

    void deleteByIoserverId(String server_id);
}

