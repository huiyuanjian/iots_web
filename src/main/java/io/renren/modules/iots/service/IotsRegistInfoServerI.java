package io.renren.modules.iots.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.iots.entity.IotsRegistInfoEntity;

/**
 * 注册信息业务接口
 */
public interface IotsRegistInfoServerI extends IService<IotsRegistInfoEntity> {

    // 根据mac地址查询注册信息
    IotsRegistInfoEntity queryByTypeAndMac(String serverType,String mac);

    // 新增
    void addOne(IotsRegistInfoEntity iotsRegistInfoEntity);

}
