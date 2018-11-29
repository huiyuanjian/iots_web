package io.renren.modules.iots.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.iots.entity.IotsEquipmentInfoEntity;
import io.renren.modules.iots.service.IotsEquipmentInfoService;

/**
 * 设备信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
@RestController
@RequestMapping("iots/iotsequipmentinfo")
public class IotsEquipmentInfoController {
	@Autowired
	private IotsEquipmentInfoService iotsEquipmentInfoService;

	/**
	 * 列表
	 */
	@RequestMapping("/public/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = iotsEquipmentInfoService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 根据id查询 某一条信息
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {

		Long id = Long.parseLong(params.get("id"));
		IotsEquipmentInfoEntity iotsEquipmentInfo = iotsEquipmentInfoService.selectById(id);
		return R.ok().put("entity", iotsEquipmentInfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/public/save")
	public R save(@RequestBody IotsEquipmentInfoEntity iotsEquipmentInfo) {
		iotsEquipmentInfo.setIsdel(0);
		iotsEquipmentInfoService.insert(iotsEquipmentInfo);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/public/update")
	public R update(@RequestBody IotsEquipmentInfoEntity iotsEquipmentInfo) {
		ValidatorUtils.validateEntity(iotsEquipmentInfo);
		iotsEquipmentInfo.setIsdel(0);
		iotsEquipmentInfoService.updateAllColumnById(iotsEquipmentInfo);// 全部更新
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/public/delete")
	public R delete(@RequestParam Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		iotsEquipmentInfoService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}

	/**
	 * 根据 IOServer的id ，查询 子eqm 的数据 。传入参数为pid
	 * 
	 * @author lfy.xys
	 * @date 2018年6月19日
	 *
	 * @param params
	 * @return
	 */
	/*@RequestMapping("/public/queryEqmForPid")
	public R queryEqmForPid(@RequestBody List<String> ids) {
		List<IotsEquipmentInfoEntity> list = iotsEquipmentInfoService.queryEqmForPid(ids);
		return R.ok().put("list", list);
	}*/
	
}
