package io.renren.modules.iots.entity.log;

import io.renren.modules.iots.entity.LogEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志查询返回结果及
 */
@Data
public class LoggerResult {

    /**
     * 根据日志的日期分组
     */
    private String groupingTime;

    /**
     * 该日期下的所有日志信息
     */
    private List<LogEntity> list = new ArrayList<>();
}
