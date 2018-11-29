package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.sys.entity.SysIotsIoserverInfoEntity;

import java.util.List;

public interface SysIotsIoserverInfoService extends IService<SysIotsIoserverInfoEntity> {
    void saveOrUpdate(Long roleId, List<Long> ioserverInfoList);
    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

    List<Long> selectByRoleId(Long roleId);

    void deleteByIoserverIdAndRoleId(SysIotsIoserverInfoEntity entity);
}
