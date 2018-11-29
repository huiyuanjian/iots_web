package io.renren.modules.iots.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.iots.entity.LogEntity;
import io.renren.modules.iots.entity.log.LoggerDto;
import io.renren.modules.iots.entity.log.LoggerResult;
import io.renren.modules.iots.entity.log.ServerInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoggerDao extends BaseMapper<LogEntity> {
    /**
     * 查询物联网接口
     * @param dto
     * @return
     */
    List<ServerInfo> selectCollect(LoggerDto dto);

    /**
     * 查询采集的
     * @param dto
     * @return
     */
    List<ServerInfo> selectProxy(LoggerDto dto);

    /**
     * 查询分发的
     * @param dto
     * @return
     */
    List<ServerInfo> selectDistribute(LoggerDto dto);

    /**
     * 查询web的
     * @param dto
     * @return
     */
    List<ServerInfo> selectWeb(LoggerDto dto);

    /**
     * 查询控制的
     * @param dto
     * @return
     */
    List<ServerInfo> selectControl(LoggerDto dto);

    /**
     * 批量插入
     * @param logEntities
     * @return
     */
    Integer saveList(@Param("logEntities") List<LogEntity> logEntities);

    /**
     * 根据服务类型和mac地址查询日志数据
     * @param mac
     * @param serverType
     * @return
     */
    List<LoggerResult> selectLoggerByServerNameAndMac(@Param("mac") String mac, @Param("serverType") String serverType, @Param("searchTime") String searchTime);
}
