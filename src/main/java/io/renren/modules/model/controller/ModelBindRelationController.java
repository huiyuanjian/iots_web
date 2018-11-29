package io.renren.modules.model.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.model.entity.ModelBindRelationEntity;
import io.renren.modules.model.service.ModelBindRelationService;

/**
 * 变量绑定关系表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 14:15:27
 */
@RestController
@RequestMapping("model/modelbindrelation")
public class ModelBindRelationController {
	@Autowired
	private ModelBindRelationService modelBindRelationService;

	/**
	 * 列表
	 */
	@RequestMapping("/public/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = modelBindRelationService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {
		Long id = Long.parseLong(params.get("id"));
		ModelBindRelationEntity modelBindRelation = modelBindRelationService.selectById(id);
		return R.ok().put("entity", modelBindRelation);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/public/save")
	public R save(@RequestBody ModelBindRelationEntity modelBindRelation) {
		modelBindRelationService.insert(modelBindRelation);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/public/update")
	public R update(@RequestBody ModelBindRelationEntity modelBindRelation) {
		ValidatorUtils.validateEntity(modelBindRelation);
		modelBindRelationService.updateAllColumnById(modelBindRelation);// 全部更新, 注意：没有传入的字段会，全部默认修改为 null
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/public/delete")
	public R delete(@RequestParam Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		modelBindRelationService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}


	/**
	 * 采集管理的变量绑定
	 * @return
	 */
	@RequestMapping("/public/bind")
	public R bind(@RequestParam Map<String, String> param){
		modelBindRelationService.bind(param);
		return R.ok();
	}

	/**
	 * 采集管理的变量解除绑定
	 * @return
	 */
	@RequestMapping("/public/noBind")
	public R noBind(@RequestParam Map<String, String> param){
		modelBindRelationService.noBind(param);
		return R.ok();
	}

}
