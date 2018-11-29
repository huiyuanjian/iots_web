package io.renren.modules.iots.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.common.utils.ExcelUtils;
import io.renren.modules.iots.entity.IotsDistributionInterfaceIotserverEntity;
import io.renren.modules.iots.entity.IotsRegistInfoEntity;
import io.renren.modules.iots.service.IotsDistributionInterfaceIotserverService;
import io.renren.modules.iots.service.IotsRegistInfoServerI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.iots.entity.IotsDistributionInterfaceEntity;
import io.renren.modules.iots.service.IotsDistributionInterfaceService;

import javax.servlet.http.HttpServletResponse;

/**
 * 分发接口管理
 */
@RestController
@RequestMapping("iots/distributioninterface")
public class IotsDistributionInterfaceController {
	
	@Autowired
	private IotsDistributionInterfaceService iotsDistributionInterfaceService;

	@Autowired
	private IotsDistributionInterfaceIotserverService iotsDistributionInterfaceIotserverService;

	@Autowired
	private IotsRegistInfoServerI iotsRegistInfoServerI;
	
	/**
	 * 列表
	 * @param params
	 * @return
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = iotsDistributionInterfaceService.queryPage(params);
		List<IotsDistributionInterfaceEntity> list = (List<IotsDistributionInterfaceEntity>) page.getList();
		if (list != null && list.size() > 0){
			list.stream().forEach(entity -> {
				List<Long> configIds = iotsDistributionInterfaceIotserverService.selectByDistributionId(entity.getId());
				entity.setConfigIds(configIds);
				if (configIds == null || configIds.size() == 0){
					entity.setState(1);
				}
			});
		}
		return R.ok().put("page", page);
	}
	
	/**
	 * 根据id查询
	 * @param params
	 * @return
	 */
	@RequestMapping("/selectById")
	public R selectById(@RequestParam Map<String, String> params) {
		Long id = Long.parseLong(params.get("id"));
		IotsDistributionInterfaceEntity entity = iotsDistributionInterfaceService.selectById(id);
		return R.ok().put("entity", entity);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	@RequestMapping("/save")
	public R save(IotsDistributionInterfaceEntity entity) {
		entity.setIsDel(0);
		entity.setState(1);
		Date date = new Date();
		entity.setCreateTime(date);
		entity.setUpdateTime(date);
		iotsDistributionInterfaceService.insert(entity);
		// 下发配置信息
		iotsDistributionInterfaceIotserverService.sendConfig(entity);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(IotsDistributionInterfaceEntity entity) {
		// TODO 第一步，修改基本信息
		entity.setIsDel(0);
		entity.setUpdateTime(new Date());
		boolean updateEntity = iotsDistributionInterfaceService.updateById(entity);
		List<Long> configIds = iotsDistributionInterfaceIotserverService.selectByDistributionId(entity.getId());
		if (configIds == null || configIds.size() == 0){
			entity.setState(1);
		}
		// 如果更新成功则向下执行
		if (updateEntity) {
			// TODO 第二步，更新与物联网接口的关联关系
			// 先删除原有数据
			//iotsDistributionInterfaceIotserverService.deleteByDistributionId(entity.getId());// TODO 删除操作，没有返回值，无法判断是否执行成功，此处有缺陷
			// 再新增新的数据
			//iotsDistributionInterfaceIotserverService.insertListOfIotserver(entity.getConfigIds(),entity.getId());
			// TODO 第三步，下发配置信息（如果第二步执行成功则执行此操作，如果不成功则执行返回操作失败。这些操作本应该在server层实现，需要加事务控制，因为很多代码已经写完了，再加上时间比较紧，来不及整改了，此处有缺陷）
			// 下发配置信息
			iotsDistributionInterfaceIotserverService.sendConfig(entity);
			return R.ok();
		} else {
			return R.error("修改失败！");
		}

//		//ValidatorUtils.validateEntity(entity);
//		//iotsDistributionInterfaceService.updateAllColumnById(entity);// 全部更新
//		// 这里只修改配置的信息
//		List<Long> configIds = entity.getConfigIds();
//		//先把原来的删除掉
//		iotsDistributionInterfaceIotserverService.deleteByDistributionId(entity.getId());
//		if (configIds != null && configIds.size() > 0){
//			configIds.stream().forEach(id -> {
//				IotsDistributionInterfaceIotserverEntity en = new IotsDistributionInterfaceIotserverEntity();
//				en.setIotserverId(id);
//				en.setDistributionId(entity.getId());
//				iotsDistributionInterfaceIotserverService.insert(en);
//			});
//		}
//
//		return R.ok();
	}
	
	/**
	 * 批量删除(TODO 这种写法用的着吗？)
	 */
	@RequestMapping("/deletes")
	public R delete(@RequestParam Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		iotsDistributionInterfaceService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(Long id) {
		// 先查出来备下发配置用
		IotsDistributionInterfaceEntity iotsDistributionInterfaceEntity = iotsDistributionInterfaceService.selectById(id);
		// 删除（TODO 这些操作本应在server层完成，加上事务，以后有时间改进吧）
		boolean isdel = iotsDistributionInterfaceService.deleteById(id);
		// 此处最好将对应的关联关系也删除 TODO 相应的删除操作加上才完整，不然数据库会留有很多垃圾数据（TODO 这些操作本应在server层完成，加上事务，以后有时间改进吧）
		iotsDistributionInterfaceIotserverService.deleteByDistributionId(id);
		if (isdel) {
			// 下发配置信息
			iotsDistributionInterfaceIotserverService.sendConfig(iotsDistributionInterfaceEntity,true);
			return R.ok();
		}
		return R.error("删除失败！");
	}

	@RequestMapping("exportExcel")
	public void exportExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
		PageUtils page = iotsDistributionInterfaceService.queryPage(params);
		List list = page.getList();
		ExcelUtils.exportExcel(list, "mytitle", "mysheetName", IotsDistributionInterfaceEntity.class, "fileName.xls", response);
	}

	/**
	 * 测试连接
	 * @param mac
	 * @return
	 */
	@RequestMapping("/public/connection")
	public R testConnection(String mac) {
		try {
			String[] split = mac.split(":");
			IotsRegistInfoEntity proxy = iotsRegistInfoServerI.queryByTypeAndMac("DISTRIBUTE", split[0]);
			if (proxy != null) {
				return R.ok();
			}
		} catch (Exception e) {
		}
		return R.error("连接失败");
	}

}
