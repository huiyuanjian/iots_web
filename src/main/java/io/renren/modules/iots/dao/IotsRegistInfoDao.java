package io.renren.modules.iots.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.iots.entity.IotsRegistInfoEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 服务信息注册
 */
public interface IotsRegistInfoDao  extends BaseMapper<IotsRegistInfoEntity> {

    IotsRegistInfoEntity queryByTypeAndMac(@Param("serverType") String serverType, @Param("mac") String mac);
}
