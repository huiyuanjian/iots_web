package io.renren.modules.iots.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.iots.entity.CtrlIotsRelationEntity;
import io.renren.modules.iots.entity.IotsRegistInfoEntity;
import io.renren.modules.iots.service.CtrlIotsRelationService;
import io.renren.modules.iots.service.IotsRegistInfoServerI;
import io.renren.modules.iots.utils.sys.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.iots.entity.IotsCtrlInfoEntity;
import io.renren.modules.iots.service.IotsCtrlInfoService;

/**
 * 控制信息 
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
@RestController
@RequestMapping("iots/iotsctrlinfo")
public class IotsCtrlInfoController {
	@Autowired
	private IotsCtrlInfoService iotsCtrlInfoService;

	@Autowired
	private CtrlIotsRelationService ctrlIotsRelationService;

	@Autowired
	private IotsRegistInfoServerI iotsRegistInfoServerI;

	/**
	 * 列表
	 */
	@RequestMapping("/public/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = iotsCtrlInfoService.queryPage(params);
		List<IotsCtrlInfoEntity> list = (List<IotsCtrlInfoEntity>) page.getList();
		if (list != null && list.size() > 0){
			list.stream().forEach(iotsCtrlInfoEntity ->{
				List<Long> idList = ctrlIotsRelationService.selectByPid(iotsCtrlInfoEntity.getId());
				iotsCtrlInfoEntity.setIoserverId(idList);
			});
		}
		return R.ok().put("page", page);
	}

	/**
	 * 根据id查询 某一条信息
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {

		Long id = Long.parseLong(params.get("id"));
		IotsCtrlInfoEntity iotsCtrlInfo = iotsCtrlInfoService.selectById(id);
		List<Long> ids = ctrlIotsRelationService.selectByPid(id);
		iotsCtrlInfo.setIoserverId(ids);
		return R.ok().put("entity", iotsCtrlInfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/public/save")
	public R save(@RequestBody IotsCtrlInfoEntity iotsCtrlInfo) {
		iotsCtrlInfo.setIsDel(0);
		iotsCtrlInfo.setField1(SysUtils.getIdentifyingCode());
		Date date = new Date();
		iotsCtrlInfo.setCreatetime(date);
		iotsCtrlInfo.setUpdatetime(date);
		iotsCtrlInfoService.insert(iotsCtrlInfo);
		iotsCtrlInfoService.issuedByTheConfiguration(iotsCtrlInfo);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/public/update")
	public R update(@RequestBody IotsCtrlInfoEntity iotsCtrlInfo) {
		ValidatorUtils.validateEntity(iotsCtrlInfo);
		iotsCtrlInfo.setIsDel(0);
		iotsCtrlInfo.setUpdatetime(new Date());
		iotsCtrlInfoService.updateAllColumnById(iotsCtrlInfo);
		iotsCtrlInfoService.issuedByTheConfigurationByUpdate(iotsCtrlInfo);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/public/delete")
	public R delete(@RequestParam Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		iotsCtrlInfoService.issuedByTheConfigurationByDelete(ids);
		iotsCtrlInfoService.deleteBatchIds(Arrays.asList(ids));
		// 然后把所有的该控制接口下的配置关系删掉
		ctrlIotsRelationService.deleteByCtrlId(Long.parseLong(ids[0]));
		return R.ok();
	}

	/*
	 * @Author zcy
	 * @Description //TODO 新增控制接口管理
	 * @Date 14:03 2018/11/15
	 * @Param name:名字，ip：，remark：描述  保存
	 * @return
	 **/
	@RequestMapping("/public/addInfo")
	public R newCtrlInfo(@RequestParam Map<String, String> params){
		return  iotsCtrlInfoService.newCtrlInfo(params);
	}

	/*
	 * @Author zcy
	 * @Description //TODO 修改控制接口管理
	 * @Date 14:03 2018/11/15
	 * @Param
	 * @return
	 **/
	@RequestMapping("/public/updateInfo")
	public R updateInfo(@RequestParam Map<String, String> params){
		return iotsCtrlInfoService.updateInfo(params);
	}

	/*
	 * @Author zcy
	 * @Description //TODO 配置接口 控制下发
	 * @Date 15:11 2018/11/15
	 * @Param 
	 * @return 
	 **/
	@RequestMapping("/public/configSendData")
	public R configSendData(@RequestParam Map<String, String> params){
		return iotsCtrlInfoService.configSendData(params);
	}

	/**
	 * 测试连接
	 * @param mac
	 * @return
	 */
	@RequestMapping("/public/connection")
	public R testConnection(String mac){
		try{
			String[] split = mac.split(":");
			IotsRegistInfoEntity proxy = iotsRegistInfoServerI.queryByTypeAndMac("CONTROL", split[0]);
			if(proxy != null ){
				return R.ok();
			}
		}catch (Exception e){}
		return R.error("连接失败");
	}


}
