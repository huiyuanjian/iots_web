package io.renren.modules.iots.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.iots.entity.CtrlIotsRelationEntity;
import io.renren.modules.iots.service.CtrlIotsRelationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * 控制端 和 IOTServer 的关系表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
@RestController
@RequestMapping("iots/ctrliotsrelation")
public class CtrlIotsRelationController {
	@Autowired
	private CtrlIotsRelationService ctrlIotsRelationService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("iots:ctrliotsrelation:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = ctrlIotsRelationService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 根据id查询 某一条信息
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {

		Long id = Long.parseLong(params.get("id"));
		CtrlIotsRelationEntity ctrlIotsRelation = ctrlIotsRelationService.selectById(id);
		return R.ok().put("entity", ctrlIotsRelation);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody CtrlIotsRelationEntity ctrlIotsRelation) {
		Long id = ctrlIotsRelation.getCtrlId();
		ctrlIotsRelationService.deleteByCtrlId(id);
		if (ctrlIotsRelation.getIotserverId() != null){
			ctrlIotsRelationService.insert(ctrlIotsRelation);
		}
		ctrlIotsRelationService.issuedByTheConfiguration(ctrlIotsRelation);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody CtrlIotsRelationEntity ctrlIotsRelation) {
		ValidatorUtils.validateEntity(ctrlIotsRelation);
		ctrlIotsRelationService.updateAllColumnById(ctrlIotsRelation);// 全部更新
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestParam Map<String, String> params) {

		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		ctrlIotsRelationService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}

}
