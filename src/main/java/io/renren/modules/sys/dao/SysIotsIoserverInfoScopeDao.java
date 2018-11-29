package io.renren.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.sys.entity.SysIotsIoserverInfoScopeEntity;

import java.util.List;

public interface SysIotsIoserverInfoScopeDao extends BaseMapper<SysIotsIoserverInfoScopeEntity> {
    int deleteBatch(Long[] roleIds);

    List<Long> selectByRoleId(Long roleId);
}
