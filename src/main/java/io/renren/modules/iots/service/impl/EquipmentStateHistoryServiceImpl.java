package io.renren.modules.iots.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.iots.dao.EquipmentStateHistoryDao;
import io.renren.modules.iots.entity.EquipmentStateHistoryEntity;
import io.renren.modules.iots.service.EquipmentStateHistoryService;


@Service("equipmentStateHistoryService")
public class EquipmentStateHistoryServiceImpl extends ServiceImpl<EquipmentStateHistoryDao, EquipmentStateHistoryEntity> implements EquipmentStateHistoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<EquipmentStateHistoryEntity> page = this.selectPage(
                new Query<EquipmentStateHistoryEntity>(params).getPage(),
                new EntityWrapper<EquipmentStateHistoryEntity>()
        );

        return new PageUtils(page);
    }

}
