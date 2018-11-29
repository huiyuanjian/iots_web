package io.renren.modules.iots.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.modules.iots.entity.IotsDistributionInterfaceEntity;

/**
 * 分发接口管理
 */
public interface IotsDistributionInterfaceService extends IService<IotsDistributionInterfaceEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
