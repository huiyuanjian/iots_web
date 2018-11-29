package io.renren.modules.iots.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.iots.dao.IotsRegistInfoDao;
import io.renren.modules.iots.entity.IotsRegistInfoEntity;
import io.renren.modules.iots.service.IotsRegistInfoServerI;
import org.springframework.stereotype.Service;

/**
 * 注册信息业务实现类
 */
@Service("iotsRegistInfoServerImpl")
public class IotsRegistInfoServerImpl extends ServiceImpl<IotsRegistInfoDao, IotsRegistInfoEntity> implements IotsRegistInfoServerI {

    @Override
    public IotsRegistInfoEntity queryByTypeAndMac( String serverType,String mac ) {
        return baseMapper.queryByTypeAndMac(serverType,mac);
    }

    @Override
    public void addOne( IotsRegistInfoEntity iotsRegistInfoEntity ) {
        IotsRegistInfoEntity regist = baseMapper.queryByTypeAndMac(iotsRegistInfoEntity.getServerType(),iotsRegistInfoEntity.getMacAddress());
        if (regist == null) {
            baseMapper.insert(iotsRegistInfoEntity);
        }
    }
}
