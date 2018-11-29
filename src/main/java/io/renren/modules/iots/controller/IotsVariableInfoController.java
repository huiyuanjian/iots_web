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
import io.renren.modules.iots.entity.IotsVariableInfoEntity;
import io.renren.modules.iots.service.IotsVariableInfoService;

/**
 * 变量信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:08
 */
@RestController
@RequestMapping("iots/iotsvariableinfo")
public class IotsVariableInfoController {
	@Autowired
	private IotsVariableInfoService iotsVariableInfoService;

	/**
	 * 列表
	 */
	@RequestMapping("/public/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = iotsVariableInfoService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 根据id查询 某一条信息
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {
		Long id = Long.parseLong(params.get("id"));
		IotsVariableInfoEntity iotsVariableInfo = iotsVariableInfoService.selectById(id);
		return R.ok().put("entity", iotsVariableInfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/public/save")
	public R save(@RequestBody IotsVariableInfoEntity iotsVariableInfo) {
		iotsVariableInfo.setIsdel(0);
		iotsVariableInfoService.insert(iotsVariableInfo);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/public/update")
	public R update(@RequestBody IotsVariableInfoEntity iotsVariableInfo) {
		ValidatorUtils.validateEntity(iotsVariableInfo);
		iotsVariableInfo.setIsdel(0);
		iotsVariableInfoService.updateAllColumnById(iotsVariableInfo);// 全部更新
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/public/delete")
	public R delete(@RequestParam Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		iotsVariableInfoService.deleteBatchIds(Arrays.asList(ids));
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
		List<IotsVariableInfoEntity> list = iotsVariableInfoService.queryVarForPid(params);
		return R.ok().put("list", list);
	}
	
}
