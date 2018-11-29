package io.renren.modules.iots.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import io.renren.modules.iots.entity.IotsRegistInfoEntity;
import io.renren.modules.iots.service.IotsRegistInfoServerI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.iots.entity.IotsIoserverInfoEntity;
import io.renren.modules.iots.service.IotsIoserverInfoService;

/**
 * IOServer信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
@RestController
@RequestMapping("iots/iotsioserverinfo")
public class IotsIoserverInfoController {
	@Autowired
	private IotsIoserverInfoService iotsIoserverInfoService;

	@Autowired
	private IotsRegistInfoServerI iotsRegistInfoServerI;

	/**
	 * 列表
	 */
	@RequestMapping("/public/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = iotsIoserverInfoService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 查询所有的IOServer 和IOServer 下的设备和变量  也是根据ioserverID集合查询所有设备和变量的公共接口
	 */
	@RequestMapping("/public/groupAndEquipmentAndVariable")
	public R groupAndEquipmentAndVariable(@RequestParam Map<String, String> param){
		List<IotsIoserverInfoEntity> list = iotsIoserverInfoService.groupAndEquipmentAndVariable(param);
		return R.ok().put("list",list);
	}

	/**
	 * 根据id查询 某一条信息
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {
		Long id = Long.parseLong(params.get("id"));
		IotsIoserverInfoEntity iotsIoserverInfo = iotsIoserverInfoService.selectById(id);
		return R.ok().put("entity", iotsIoserverInfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/public/save")
	public R save(@RequestBody IotsIoserverInfoEntity iotsIoserverInfo) {
		iotsIoserverInfo.setIsDel(0);
		Map<String, Object> map = new HashMap<>();
		map.put("name", iotsIoserverInfo.getName());
		List<IotsIoserverInfoEntity> iotsIoserverInfoEntities = iotsIoserverInfoService.selectByMap(map);
		if (iotsIoserverInfoEntities != null && iotsIoserverInfoEntities.size() > 0) return R.error("数据已存在!");
		boolean insert = iotsIoserverInfoService.insert(iotsIoserverInfo);
		if (insert){
			// 新增成功, 下发配置
			iotsIoserverInfoService.issuedByTheConfiguration(iotsIoserverInfo.getPid(), iotsIoserverInfo.getName());
		}
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/public/update")
	public R update(@RequestBody IotsIoserverInfoEntity iotsIoserverInfo) {
		ValidatorUtils.validateEntity(iotsIoserverInfo);
		// TODO 测试方便暂时写死
		//iotsIoserverInfo.setIsDel(0);
		//iotsIoserverInfo.setId(364L);
		//iotsIoserverInfo.setPid(50L);
		//iotsIoserverInfo.setIp("8C164526BBC6");
		boolean b = iotsIoserverInfoService.updateAllColumnById(iotsIoserverInfo);// 全部更新
		if (b){
			// 新增成功, 下发配置
			iotsIoserverInfoService.issuedByTheConfigurationByUpdate(iotsIoserverInfo.getPid(), iotsIoserverInfo.getName());
		}
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/public/delete")
	public R delete(@RequestParam Map<String, String> params) {
		String idStr = params.get("ids");
		String[] ids = idStr.split(",");
		// TODO 虽然是删除多个的方法, 但是页面上现在是单个删除
		boolean b = iotsIoserverInfoService.deleteBatchIds(Arrays.asList(ids));
		if (b){
			iotsIoserverInfoService.issuedcfgByDelete(ids[0]);
		}
		return R.ok();
	}

	/**
	 * 根据 分组的id ，查询 子IOServer 的数据 。传入参数为pid
	 *
	 * @author lfy.xys
	 * @date 2018年6月19日
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/public/queryIOServerForPid")
	public R queryIOServerForPid(@RequestParam Map<String, String> params) {

		Long pid = Long.parseLong((String) params.get("pid"));

		List<IotsIoserverInfoEntity> list = iotsIoserverInfoService.queryIOServerForPid(pid);
		return R.ok().put("list", list);
	}

	/**
	 * 同步 IOServer 的设备和变量
	 *
	 * http://localhost:8082/iots/iotsioserverinfo/public/syncEqmAndVar
	 *
	 * @author lfy.xys
	 * @date 2018年6月20日
	 *
	 */
	@RequestMapping("/public/syncEqmAndVar")
	public R syncEqmAndVar(@RequestParam Map<String, String> params) {
		return iotsIoserverInfoService.syncEqmAndVar(params);
	}

	/**
	 * 执行结果回调
	 */
	@PostMapping("/callback")
	public String callback(@RequestParam(value = "key")String key){
		if (key != null && !"".equals(key)) {
			return iotsIoserverInfoService.callback(key);
		} else {
			return JSONObject.toJSONString(R.error("同步发生错误"));
		}
	}


	/**
	 * 查询当前用户下的ioserverInfo
	 */
	@RequestMapping("/iotServerInfo/only")
	public R roleIotServerInfo(@RequestParam Map<String, Object> params){
		Page<IotsIoserverInfoEntity> list = iotsIoserverInfoService.roleIotServerInfo(params);
		return R.ok().put("list",list);
	}

	/**
	 * //查询当前用户下的所有可选择的ioserverInfo
	 *  2018-11-08  改为查询所有的ioserver
	 */
	@RequestMapping("/iotServerInfo/list")
	public R roleIotServerInfoList(@RequestParam Map<String, Object> params){

		PageUtils pageUtils = iotsIoserverInfoService.queryPage(params);
		return R.ok().put("list",pageUtils);
	}

	/**
	 * 查询采集分组
	 * @param params
	 * @return
	 */
	@RequestMapping("/public/queryIOServerAndIOServerInfo")
	public R queryIOServerAndIoServerInfo(@RequestParam Map<String, Object> params){
		Object list = iotsIoserverInfoService.queryIOServerAndIoServerInfo(params);
		return R.ok().put("list",list);
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
			IotsRegistInfoEntity proxy = iotsRegistInfoServerI.queryByTypeAndMac("PROXY", split[0]);
			if(proxy != null ){
				return R.ok();
			}
		}catch (Exception e){}
		return R.error("连接失败");
	}
}
