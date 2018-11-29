package io.renren.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.sys.entity.SysIotsIoserverInfoEntity;

import java.util.List;

public interface SysIotsIoserverInfoDao extends BaseMapper<SysIotsIoserverInfoEntity> {

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

    List<Long> selectByRoleId(Long roleId);

    void deleteByIoserverIdAndRoleId(SysIotsIoserverInfoEntity entity);
}
