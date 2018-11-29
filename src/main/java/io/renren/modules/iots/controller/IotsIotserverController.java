package io.renren.modules.iots.controller;

import com.alibaba.fastjson.JSONObject;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.iots.entity.IotsIotserverEntity;
import io.renren.modules.iots.service.IotsIotserverService;
import io.renren.modules.iots.utils.sys.SysUserInfo;
import io.renren.modules.sys.entity.CacheUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IOT Server端 ，即分组表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
@RestController
@RequestMapping("iots/iotsiotserver")
public class IotsIotserverController {
	@Autowired
	private IotsIotserverService iotsIotserverService;

	@Autowired
	private SysUserInfo sysUserInfo;

	/**
	 * 列表
	 * 
	 * RequestParam 使用 a=1 格式 name=111
	 * 
	 * http://jv.hengtaiboyuan.com/iots/iotsiotserver/public/list
	 * http://localhost:8082/iots/iotsiotserver/public/list
	 */
	@RequestMapping("/public/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = iotsIotserverService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * 根据id查询 某一条信息
	 * 
	 * http://jv.hengtaiboyuan.com/iots/iotsiotserver/public/selectOne
	 * http://localhost:8082/iots/iotsiotserver/public/selectOne
	 */
	@RequestMapping("/public/selectById")
	public R selectById(@RequestParam Map<String, String> params) {

		Long id = Long.parseLong(params.get("id"));
		IotsIotserverEntity iotsIotserver = iotsIotserverService.selectById(id);
		return R.ok().put("entity", iotsIotserver);
	}

	/**
	 * 保存
	 * 
	 * RequestBody 必须使用json格式 { "limit":"1", "page":"1" }
	 * 
	 * http://jv.hengtaiboyuan.com/iots/iotsiotserver/public/save
	 * http://localhost:8082/iots/iotsiotserver/public/save
	 */
	@RequestMapping("/public/save")
	public R save(HttpServletRequest request,@RequestBody IotsIotserverEntity iotsIotserver) {
		CacheUser cacheUser = sysUserInfo.getNowUser(request);
		iotsIotserver.setUpdator(cacheUser.getUserId());
		iotsIotserver.setCreator(cacheUser.getUserId());
		iotsIotserver.setIsDel(0);
		Map<String, Object> map = new HashMap<>();
		map.put("ip",iotsIotserver.getIp());
		List<IotsIotserverEntity> iotsIotserverEntities = iotsIotserverService.selectByMap(map);
		if (iotsIotserverEntities != null && iotsIotserverEntities.size() > 0){
				return R.error("数据库已经存在该条记录!");
		}
		try{
			iotsIotserver.setTopic("COIIECT_" + iotsIotserver.getIp());
			boolean insert = iotsIotserverService.insert(iotsIotserver);
			if (insert){
				// 下发配置
				iotsIotserverService.issuedByTheConfiguration(iotsIotserver);
			} else {
				return R.error("新增物联网数据失败!");
			}
		}catch(Exception e){
			return R.error("新增物联网数据失败!");
		}
		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * http://jv.hengtaiboyuan.com/iots/iotsiotserver/public/save
	 * http://localhost:8082/iots/iotsiotserver/public/save
	 */
	@RequestMapping("/public/update")
	public R update(HttpServletRequest request,IotsIotserverEntity iotsIotserver) {
		CacheUser cacheUser = sysUserInfo.getNowUser(request);
		ValidatorUtils.validateEntity(iotsIotserver);
		iotsIotserver.setIsDel(0);
		try{
			iotsIotserver.setTopic("COIIECT_" + iotsIotserver.getIp());
			iotsIotserver.setUpdator(cacheUser.getUserId());
			boolean b = iotsIotserverService.updateInfoyId(iotsIotserver);// 全部更新
			if (b){
				// 下发配置
				iotsIotserverService.issuedConfigByUpdate(iotsIotserver);
			} else {
				R.error("修改数据失败!");
			}
		}catch (Exception e){
			R.error("修改数据失败!");
		}
		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * 改写，传递 字符串，例如 1,2,3,4 用 ‘,’隔开
	 * 
	 * http://jv.hengtaiboyuan.com/iots/iotsiotserver/public/delete
	 * http://localhost:8082/iots/iotsiotserver/public/delete
	 */
	@RequestMapping("/public/delete")
	public R delete(@RequestParam Map<String, String> params) {
		try{
			iotsIotserverService.issuedConfigByDelete(params);
		}catch (Exception e){
			return R.error("删除失败!");
		}
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids) {
		iotsIotserverService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}
	
	
	/**
	 * 根据 ids，查询需要发送配置 的 IOTS
	 * 
	 * @author lfy.xys
	 * @date 2018年6月20日
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/public/sendIOTSConfig")
	public R sendIOTSConfig(@RequestParam Map<String, String> params) {
		return iotsIotserverService.sendIOTSConfig(params);
	}
	
	/**
	 * 查询分组下 的 设备 和 变量信息
	 * 
	 * http://localhost:8082/iots/iotsiotserver/public/queryEqmVar
	 * 
	 * @author lfy.xys
	 * @date 2018年6月25日
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping("/public/queryEqmVar")
	public R queryEqmVar(@RequestParam Map<String, String> params) {
		return iotsIotserverService.queryEqmVar(params);
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
			Boolean bool = iotsIotserverService.testConnection(split[0]);
			if (bool){
				return R.ok();
			}
		}catch (Exception e){}
		return R.error("连接失败");
	}


	public static void main(String[] args) {
		Long[] ids = { 1L, 2L, 3L };
		System.out.println(JSONObject.toJSONString(ids));
	}
}
