package io.renren.modules.model.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.renren.modules.model.entity.IoserverCollectorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.model.entity.ModelEquipmentInfoEntity;
import io.renren.modules.model.service.ModelEquipmentInfoService;

/**
 * 设备信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
@RestController
@RequestMapping("model/modelequipmentinfo")
public class ModelEquipmentInfoController {
	@Autowired
	private ModelEquipmentInfoService modelEquipmentInfoService;

	/**
	 * 列表
	 */
	@RequestMapping("/public/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = modelEquipmentInfoService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 根据同步ID查询设备和变量
	 * @return
	 */
	@RequestMapping("/public/equipmentAndVariables")
	public R equipmentAndVariables(@RequestParam Map<String, Object> params){
		List<ModelEquipmentInfoEntity> modelEquipmentInfo = modelEquipmentInfoService.equipmentAndVariables(params);
		return R.ok().put("entity", modelEquipmentInfo);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {
		Long id = Long.parseLong(params.get("id"));
		ModelEquipmentInfoEntity modelEquipmentInfo = modelEquipmentInfoService.selectById(id);
		return R.ok().put("entity", modelEquipmentInfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/public/save")
	public R save(@RequestBody ModelEquipmentInfoEntity modelEquipmentInfo) {
		modelEquipmentInfoService.insert(modelEquipmentInfo);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/public/update")
	public R update(@RequestBody ModelEquipmentInfoEntity modelEquipmentInfo) {
		ValidatorUtils.validateEntity(modelEquipmentInfo);
		modelEquipmentInfoService.updateAllColumnById(modelEquipmentInfo);// 全部更新, 注意：没有传入的字段会，全部默认修改为 null
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/public/delete")
	public R delete(@RequestParam Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		modelEquipmentInfoService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}

	@RequestMapping("/public/collector")
	public R insertCollector(IoserverCollectorEntity entity){
		modelEquipmentInfoService.insertCollector(entity);
		return R.ok();
	}

	@RequestMapping("/public/noBindCollector")
	public R noBindCollector(IoserverCollectorEntity entity){
		modelEquipmentInfoService.noBindCollector(entity);
		return R.ok();
	}

}
