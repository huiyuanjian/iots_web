package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.sys.entity.SysIotsIoserverInfoScopeEntity;

import java.util.List;

public interface SysIotsIoserverInfoScopeService extends IService<SysIotsIoserverInfoScopeEntity> {
    void saveOrUpdate(Long roleId, List<Long> ioserverInfoScopeList);
    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

    List<Long> selectByRoleId(Long roleId);
}
