package io.renren.modules.iots.service.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.iots.dao.IotsDistributionInterfaceDao;
import io.renren.modules.iots.entity.IotsDistributionInterfaceEntity;
import io.renren.modules.iots.service.IotsDistributionInterfaceService;

/**
 * 分发接口管理
 */
@Service
public class IotsDistributionInterfaceServiceImpl extends ServiceImpl<IotsDistributionInterfaceDao, IotsDistributionInterfaceEntity> implements IotsDistributionInterfaceService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String name = (String) params.get("name");
    	String state = (String) params.get("state");
    	String id = (String) params.get("id");
    	
        Page<IotsDistributionInterfaceEntity> page = this.selectPage(
                new Query<IotsDistributionInterfaceEntity>(params).getPage(),
                new EntityWrapper<IotsDistributionInterfaceEntity>()
                	.like(StringUtils.isNotBlank(name), "name", name)
                	.eq(StringUtils.isNotBlank(state), "state", state)
                	.eq(StringUtils.isNotBlank(id), "id", id)
        );

        return new PageUtils(page);
	}

}
