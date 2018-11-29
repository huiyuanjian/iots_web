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

package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.annotation.DataFilter;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.SysRoleDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysRoleEntity;
import io.renren.modules.sys.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 角色
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:45:12
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysRoleDao sysRoleDao;

	@Autowired
	private SysIotsIoserverInfoScopeService sysIotsIoserverInfoScopeService;

	@Autowired
	private SysIotsIoserverInfoService sysIotsIoserverInfoService;

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String)params.get("roleName");
		String str = (String)params.get("status");
		Integer status = null;
		if (str != null && !"".equals(str)){
			status = Integer.parseInt(str);
		}
		Page<SysRoleEntity> page = this.selectPage(
			new Query<SysRoleEntity>(params).getPage(),
			new EntityWrapper<SysRoleEntity>()
				.like(StringUtils.isNotBlank(roleName),"role_name", roleName)
					.eq(status != null,"status", status)
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
		);

		for(SysRoleEntity sysRoleEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysRoleEntity.getDeptId());
			if(sysDeptEntity != null){
				sysRoleEntity.setDeptName(sysDeptEntity.getName());
			}
			//查询
			List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(sysRoleEntity.getRoleId());
			List<Long> deptIds = sysRoleDeptService.selectByRoleId(sysRoleEntity.getRoleId());
			List<Long> userIds = sysUserRoleService.selectByRoleId(sysRoleEntity.getRoleId());
			List<Long> iosserverInfoIds = sysIotsIoserverInfoService.selectByRoleId(sysRoleEntity.getRoleId());
			List<Long> iosserverInfoScopeIds = sysIotsIoserverInfoScopeService.selectByRoleId(sysRoleEntity.getRoleId());
			sysRoleEntity.setDeptIdList(deptIds);
			sysRoleEntity.setUserList(userIds);
			sysRoleEntity.setIoserverInfoList(iosserverInfoIds);
			sysRoleEntity.setIoserverInfoScopeList(iosserverInfoScopeIds);
			sysRoleEntity.setMenuIdList(menuIdList);
		}
		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysRoleEntity role) {
		role.setCreateTime(new Date());
		this.insert(role);

		//保存角色与菜单关系
		if (role.getMenuIdList() != null && role.getMenuIdList().size() > 0){
			sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
		}

		//保存角色与部门关系
		//sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());

		//保存角色与当前角色下的ioserverInfo 的关系
		if (role.getIoserverInfoList() != null && role.getIoserverInfoList().size() > 0){
			sysIotsIoserverInfoService.saveOrUpdate(role.getRoleId(),role.getIoserverInfoList());
		}

		//保存角色与当前角色下的ioserverInfo 可选择的关系
		if (role.getIoserverInfoScopeList() != null && role.getIoserverInfoScopeList().size() > 0){
			sysIotsIoserverInfoScopeService.saveOrUpdate(role.getRoleId(),role.getIoserverInfoScopeList());
		}

		//更新角色与用户的关系
		if (role.getUserList() != null && role.getUserList().size() > 0){
			sysUserRoleService.saveOrUpdate(role.getRoleId(),role.getUserList());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysRoleEntity role) {
		this.updateAllColumnById(role);

		//更新角色与菜单关系
		if (role.getMenuIdList() != null && role.getMenuIdList().size() > 0){
			sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
		}

		//保存角色与部门关系
		//sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());

		//保存角色与当前角色下的ioserverInfo 的关系
		if (role.getIoserverInfoList() != null && role.getIoserverInfoList().size() > 0){
			sysIotsIoserverInfoService.saveOrUpdate(role.getRoleId(),role.getIoserverInfoList());
		}

		//保存角色与当前角色下的ioserverInfo 可选择的关系
		if (role.getIoserverInfoScopeList() != null && role.getIoserverInfoScopeList().size() > 0){
			sysIotsIoserverInfoScopeService.saveOrUpdate(role.getRoleId(),role.getIoserverInfoScopeList());
		}

		//更新角色与用户的关系
		if (role.getUserList() != null && role.getUserList().size() > 0){
			sysUserRoleService.saveOrUpdate(role.getRoleId(),role.getUserList());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		//删除角色
		this.deleteBatchIds(Arrays.asList(roleIds));

		//删除角色与菜单关联
		sysRoleMenuService.deleteBatch(roleIds);

		//删除角色与部门关联
		//sysRoleDeptService.deleteBatch(roleIds);

		//删除角色与用户关联
		sysUserRoleService.deleteBatch(roleIds);

		sysIotsIoserverInfoService.deleteBatch(roleIds);

		sysIotsIoserverInfoScopeService.deleteBatch(roleIds);


	}

	/*
	 * @Author zcy
	 * @Description //TODO 根据角色名 查找角色信息
	 * @Date 12:15 2018/11/21
	 * @Param
	 * @return
	 **/
	public List<SysRoleEntity> getRoleName(SysRoleEntity role){
		return sysRoleDao.getRoleName(role);
	}
}
