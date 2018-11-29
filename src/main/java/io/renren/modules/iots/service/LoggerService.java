package io.renren.modules.iots.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.modules.iots.entity.LogEntity;
import io.renren.modules.iots.entity.log.LoggerDto;
import io.renren.modules.iots.entity.log.LoggerResult;
import io.renren.modules.iots.entity.log.ServerInfo;

import java.util.List;

public interface LoggerService extends IService<LogEntity> {
    /**
     * 根据接口类型 查询 接口下边的服务
     * @param dto
     * @return
     */
    List<ServerInfo> selectByType(LoggerDto dto);

    /**
     * 新增
     * @param serverLog
     * @return
     */
    boolean save(LogEntity serverLog);

    /**
     * 修改日志
     * @param serverLog
     * @return
     */
    boolean updateLogger(LogEntity serverLog);

    /**
     * 批量插入
     * @param logEntities
     * @return
     */
    boolean saveList(List<LogEntity> logEntities);

    /**
     * 根据类型和mac地址查询日志数据
     * @param mac
     * @param serverType
     * @return
     */
    List<LoggerResult> selectLoggerByServerNameAndMac(String mac, String serverType, String searchTime);
}
