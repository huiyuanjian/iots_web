/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.sys.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.annotation.SysLog;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.Assert;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.sys.entity.CacheUser;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserRoleService;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import sun.misc.Cache;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Value("${token}")
	private String TOKEN;

	@Autowired
	private RedisTemplate redisTemplate;

	private ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysUserService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/password")
	public R password(HttpServletRequest request, String password, String newPassword){

		Cookie[] cookies = request.getCookies();
		Cookie loginCookie = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(TOKEN)) {
				loginCookie = cookie;
			}
		}
		String loginToken = loginCookie.getValue();
		String user1 = (String) redisTemplate.opsForValue().get(TOKEN + loginToken);
		CacheUser u = null;
		try {
			u = objectMapper.readValue(user1.trim(), CacheUser.class);
		} catch (IOException e) {
			return R.error();
		}
		Assert.isBlank(newPassword, "新密码不为能空");

		//原密码
		password = ShiroUtils.sha256(password, u.getSalt());
		//新密码
		newPassword = ShiroUtils.sha256(newPassword, u.getSalt());
				
		//更新密码
		boolean flag = sysUserService.updatePassword(u.getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}

		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.selectById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	public R save(HttpServletRequest request, @RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		Cookie[] cookies = request.getCookies();
		Cookie loginCookie = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(TOKEN)) {
				loginCookie = cookie;
			}
		}
		String loginToken = loginCookie.getValue();
		String user1 = (String) redisTemplate.opsForValue().get(TOKEN + loginToken);
		CacheUser u = null;
		try {
			u = objectMapper.readValue(user1.trim(), CacheUser.class);
		} catch (IOException e) {
			return R.error();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("username",user.getUsername());
		List<SysUserEntity> sysUserEntities = sysUserService.selectByMap(map);
		if (sysUserEntities !=null && sysUserEntities.size() > 0){
			return R.error("用户已经存在!");
		}
		sysUserService.save(user, u.getUserId());
		
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	public R update(HttpServletRequest request,SysUserEntity user){

		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		Cookie[] cookies = request.getCookies();
		Cookie loginCookie = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(TOKEN)) {
				loginCookie = cookie;
			}
		}
		String loginToken = loginCookie.getValue();
		String user1 = (String) redisTemplate.opsForValue().get(TOKEN + loginToken);
		CacheUser u = null;
		try {
			u = objectMapper.readValue(user1.trim(), CacheUser.class);
		} catch (IOException e) {
			return R.error();
		}
		/*Map<String, Object> map = new HashMap<>();
		map.put("username", user.getUsername());
		List<SysUserEntity> sysUserEntities = sysUserService.selectByMap(map);
		if (sysUserEntities != null && sysUserEntities.size() > 0){
			return R.error("用户名已经存在, 请重新输入");
		}*/
		sysUserService.update(user,u.getUserId());
		
		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	public R delete(HttpServletRequest request,@RequestBody Long[] userIds){
		Cookie[] cookies = request.getCookies();
		Cookie loginCookie = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(TOKEN)) {
				loginCookie = cookie;
			}
		}
		String loginToken = loginCookie.getValue();
		String user1 = (String) redisTemplate.opsForValue().get(TOKEN + loginToken);
		CacheUser u = null;
		try {
			u = objectMapper.readValue(user1.trim(), CacheUser.class);
		} catch (IOException e) {
			return R.error();
		}
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}

		if(ArrayUtils.contains(userIds, u.getUserId())){
			return R.error("当前用户不能删除");
		}
		sysUserService.deleteBatchIds(Arrays.asList(userIds));
		return R.ok();
	}

	/**
	 * 个人中心修改
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping("/updatePersonalDetails")
	public R updatePersonalDetails(HttpServletRequest request,SysUserEntity user){
		Cookie[] cookies = request.getCookies();
		Cookie loginCookie = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(TOKEN)) {
				loginCookie = cookie;
			}
		}
		String loginToken = loginCookie.getValue();
		String user1 = (String) redisTemplate.opsForValue().get(TOKEN + loginToken);
		CacheUser u = null;
		try {
			u = objectMapper.readValue(user1.trim(), CacheUser.class);
		} catch (IOException e) {
			e.printStackTrace();
			return R.error();
		}
		/*Map<String, Object> map = new HashMap<>();
		map.put("username", user.getUsername());
		List<SysUserEntity> sysUserEntities = sysUserService.selectByMap(map);
		if (sysUserEntities != null && sysUserEntities.size() > 0){
			return R.error("用户名已经存在, 请重新输入");
		}*/
		// 根据ID 查询用户
		SysUserEntity sysUserEntity = sysUserService.selectById(u.getUserId());
		sysUserEntity.setUsername(user.getUsername());
		sysUserEntity.setMobile(user.getMobile());
		sysUserService.updateById(sysUserEntity);
		return R.ok();
	}
}
