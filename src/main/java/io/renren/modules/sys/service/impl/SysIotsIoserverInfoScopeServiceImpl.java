package io.renren.modules.sys.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.sys.dao.SysIotsIoserverInfoScopeDao;
import io.renren.modules.sys.entity.SysIotsIoserverInfoEntity;
import io.renren.modules.sys.entity.SysIotsIoserverInfoScopeEntity;
import io.renren.modules.sys.service.SysIotsIoserverInfoScopeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysIotsIoserverInfoScopeServiceImpl extends ServiceImpl<SysIotsIoserverInfoScopeDao, SysIotsIoserverInfoScopeEntity> implements SysIotsIoserverInfoScopeService {
    @Override
    public void saveOrUpdate(Long roleId, List<Long> ioserverInfoScopeList) {
        //先删除角色与ioserverInfo关系
        deleteBatch(new Long[]{roleId});

        if(ioserverInfoScopeList.size() == 0){
            return ;
        }

        //保存角色与ioserverInfo关系
        List<SysIotsIoserverInfoScopeEntity> list = new ArrayList<>(ioserverInfoScopeList.size());
        for(Long ioserverInfoId : ioserverInfoScopeList){
            SysIotsIoserverInfoScopeEntity sysIotsIoserverInfoScopeEntity = new SysIotsIoserverInfoScopeEntity();
            sysIotsIoserverInfoScopeEntity.setRoleIoserverinfoIdScope(ioserverInfoId);
            sysIotsIoserverInfoScopeEntity.setRoleId(roleId);

            list.add(sysIotsIoserverInfoScopeEntity);
        }
        this.insertBatch(list);
    }

    @Override
    public int deleteBatch(Long[] roleIds) {
        return this.baseMapper.deleteBatch(roleIds);
    }

    @Override
    public List<Long> selectByRoleId(Long roleId) {
        return this.baseMapper.selectByRoleId(roleId);
    }
}
