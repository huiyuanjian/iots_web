package io.renren.modules.iots.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.iots.entity.EquipmentStateHistoryEntity;

import java.util.Map;

/**
 * 设备状态历史记录表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-21 10:26:49
 */
public interface EquipmentStateHistoryService extends IService<EquipmentStateHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

