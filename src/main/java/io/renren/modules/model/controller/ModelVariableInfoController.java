package io.renren.modules.model.controller;

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
import io.renren.modules.iots.entity.IotsVariableInfoEntity;
import io.renren.modules.model.entity.ModelVariableInfoEntity;
import io.renren.modules.model.service.ModelVariableInfoService;

/**
 * 变量信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
@RestController
@RequestMapping("model/modelvariableinfo")
public class ModelVariableInfoController {
	@Autowired
	private ModelVariableInfoService modelVariableInfoService;

	/**
	 * 列表
	 */
	@RequestMapping("/public/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = modelVariableInfoService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {
		Long id = Long.parseLong(params.get("id"));
		ModelVariableInfoEntity modelVariableInfo = modelVariableInfoService.selectById(id);
		return R.ok().put("entity", modelVariableInfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/public/save")
	public R save(@RequestBody ModelVariableInfoEntity modelVariableInfo) {
		modelVariableInfoService.insert(modelVariableInfo);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/public/update")
	public R update(@RequestBody ModelVariableInfoEntity modelVariableInfo) {
		ValidatorUtils.validateEntity(modelVariableInfo);
		modelVariableInfoService.updateAllColumnById(modelVariableInfo);// 全部更新, 注意：没有传入的字段会，全部默认修改为 null
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/public/delete")
	public R delete(@RequestParam Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		modelVariableInfoService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}

	/**
	 * 根据 设备的id ，查询 子var变量 的数据 。传入参数为pid
	 * 
	 * @author lfy.xys
	 * @date 2018年6月19日
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/public/queryVarForPid")
	public R queryVarForPid(@RequestParam Map<String, String> params) {
		List<ModelVariableInfoEntity> list = modelVariableInfoService.queryVarForPid(params);
		return R.ok().put("list", list);
	}
}
