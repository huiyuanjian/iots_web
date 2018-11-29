package io.renren.modules.iots.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.R;
import io.renren.modules.iots.entity.IotsDistributionInterfaceIotserverEntity;
import io.renren.modules.iots.service.IotsDistributionInterfaceIotserverService;

/**
 * TODO 为什么要单独弄一个这样的controller呢？完全可以放在分发接口管理的controller里呀！
 * 分发接口-物联网接口-关系
 */
@RestController
@RequestMapping("iots/distributioniotserver")
public class IotsDistributionInterfaceIotserverController {
	
	@Autowired
	private IotsDistributionInterfaceIotserverService iotsDistributionInterfaceIotserverService;
	
	/**
	 * 列表
	 * @param distributionId
	 * @return
	 */
	@RequestMapping("/list")
	public R list(Long distributionId) {
		Map<String, Object> columnMap = new HashMap<>();
		columnMap.put("distribution_id", distributionId);
		List<IotsDistributionInterfaceIotserverEntity> entity = iotsDistributionInterfaceIotserverService.selectByMap(columnMap);
		return R.ok().put("entity", entity);
	}
	
	/**
	 * 批量插入
	 * @param entityList
	 * @return
	 */
	@RequestMapping("/insertBatch")
	public R insertBatch(@RequestBody List<IotsDistributionInterfaceIotserverEntity> entityList) {
		iotsDistributionInterfaceIotserverService.deleteByDistributionId(entityList.get(0).getDistributionId());
		iotsDistributionInterfaceIotserverService.insertBatch2(entityList);
		iotsDistributionInterfaceIotserverService.issuedByTheConfiguration(entityList);
		iotsDistributionInterfaceIotserverService.setStatus(entityList.get(0).getDistributionId());
		return R.ok();
	}
}
