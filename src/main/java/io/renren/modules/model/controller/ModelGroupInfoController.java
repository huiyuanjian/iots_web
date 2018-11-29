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
import io.renren.modules.model.entity.ModelGroupInfoEntity;
import io.renren.modules.model.service.ModelGroupInfoService;

/**
 * 分组表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
@RestController
@RequestMapping("model/modelgroupinfo")
public class ModelGroupInfoController {
	@Autowired
	private ModelGroupInfoService modelGroupInfoService;
	

	/**
	 * 列表
	 */
	@RequestMapping("/public/list")
	public R list(@RequestParam Map<String, Object> params) {
		List<ModelGroupInfoEntity> list = modelGroupInfoService.queryTree(params);
		return R.ok().put("list", list);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {
		Long id = Long.parseLong(params.get("id"));
		ModelGroupInfoEntity modelGroupInfo = modelGroupInfoService.selectById(id);
		return R.ok().put("entity", modelGroupInfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/public/save")
	public R save(@RequestBody ModelGroupInfoEntity modelGroupInfo) {
		modelGroupInfoService.insert(modelGroupInfo);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/public/update")
	public R update(@RequestBody ModelGroupInfoEntity modelGroupInfo) {
		ValidatorUtils.validateEntity(modelGroupInfo);
		modelGroupInfoService.updateAllColumnById(modelGroupInfo);// 全部更新, 注意：没有传入的字段会，全部默认修改为 null
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/public/delete")
	public R delete(@RequestParam Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		modelGroupInfoService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}

	/**
	 * 查询分组下 的 设备 和 变量信息
	 * 
	 * http://localhost:8082/model/modelgroupinfo/public/queryEqmVar
	 * 
	 * @author lfy.xys
	 * @date 2018年6月25日
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/public/queryEqmVar")
	public R queryEqmVar(@RequestParam Map<String, String> params) {
		return modelGroupInfoService.queryEqmVar(params);
	}

}
