package io.renren.modules.iots.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.iots.dao.IotsEquipmentInfoDao;
import io.renren.modules.iots.entity.EqmStateEntity;
import io.renren.modules.iots.entity.IotsEquipmentInfoEntity;
import io.renren.modules.iots.service.IotsEquipmentInfoService;


@Service("iotsEquipmentInfoService")
public class IotsEquipmentInfoServiceImpl extends ServiceImpl<IotsEquipmentInfoDao, IotsEquipmentInfoEntity> implements IotsEquipmentInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<IotsEquipmentInfoEntity> page = this.selectPage(
                new Query<IotsEquipmentInfoEntity>(params).getPage(),
                new EntityWrapper<IotsEquipmentInfoEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<IotsEquipmentInfoEntity> queryEqmForPid(List<String> ids, String name) {
		final List<Long> list = new ArrayList<Long>();
		ids.stream().forEach(id -> {
			list.add(Long.valueOf(id.trim()).longValue());
		});
		return baseMapper.queryEqmForPid(list, name);
	}

	@Override
	public List<EqmStateEntity> queryEqmStateMap(){
		return baseMapper.queryEqmStateMap();
	}

	@Override
	public IotsEquipmentInfoEntity selectByDyncId(Long id, String name) {
		return this.baseMapper.selectByDyncId(id, name);
	}

	@Override
	public void deleteByIoserverId(String server_id) {
		this.baseMapper.deleteByIoserverId(server_id);
	}

}


