package io.renren.modules.model.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.model.entity.IoserverCollectorEntity;
import io.renren.modules.model.entity.ModelEquipmentInfoEntity;

/**
 * 设备信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
public interface ModelEquipmentInfoService extends IService<ModelEquipmentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<ModelEquipmentInfoEntity> queryEqmForPid(Map<String, String> params);

    R syncData();

    List<ModelEquipmentInfoEntity> queryAllList();

    List<ModelEquipmentInfoEntity> equipmentAndVariables(Map<String, Object> id);

    void insertCollector(IoserverCollectorEntity entity);

    void noBindCollector(IoserverCollectorEntity entity);
}

