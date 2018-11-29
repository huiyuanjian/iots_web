package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.sys.dao.SysIotsIoserverInfoDao;
import io.renren.modules.sys.entity.SysIotsIoserverInfoEntity;
import io.renren.modules.sys.entity.SysRoleDeptEntity;
import io.renren.modules.sys.service.SysIotsIoserverInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysIotsIoserverInfoServiceImpl extends ServiceImpl<SysIotsIoserverInfoDao, SysIotsIoserverInfoEntity> implements SysIotsIoserverInfoService {
    @Override
    public void saveOrUpdate(Long roleId, List<Long> ioserverInfoList) {
        //先删除角色与ioserverInfo关系
        deleteBatch(new Long[]{roleId});

        if(ioserverInfoList.size() == 0){
            return ;
        }

        //保存角色与ioserverInfo关系
        List<SysIotsIoserverInfoEntity> list = new ArrayList<>(ioserverInfoList.size());
        for(Long ioserverInfoId : ioserverInfoList){
            SysIotsIoserverInfoEntity sysIotsIoserverInfoEntity = new SysIotsIoserverInfoEntity();
            sysIotsIoserverInfoEntity.setIoserverInfoId(ioserverInfoId);
            sysIotsIoserverInfoEntity.setRoleId(roleId);

            list.add(sysIotsIoserverInfoEntity);
        }
        this.insertBatch(list);
    }

    @Override
    public int deleteBatch(Long[] roleIds){

        return this.baseMapper.deleteBatch(roleIds);
    }

    @Override
    public List<Long> selectByRoleId(Long roleId) {
        return this.baseMapper.selectByRoleId(roleId);
    }

    @Override
    public void deleteByIoserverIdAndRoleId(SysIotsIoserverInfoEntity entity) {
        this.baseMapper.deleteByIoserverIdAndRoleId(entity);
    }
}
