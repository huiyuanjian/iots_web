package io.renren.modules.iots.controller;

import io.renren.common.utils.R;
import io.renren.modules.iots.entity.LogEntity;
import io.renren.modules.iots.entity.log.LoggerDto;
import io.renren.modules.iots.entity.log.ServerInfo;
import io.renren.modules.iots.entity.log.ServerLog;
import io.renren.modules.iots.service.LoggerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @remark : 日志处理类
 * @author : yeyaowen
 * @date : 2018-11-21 13:31
 * @version : v1.0
 */
@RestController
@RequestMapping("/logger")
public class LoggerController {

    @Resource
    private LoggerService loggerService;

    /**
     * 根据服务类型查询下面的服务
     * @param dto
     * @return
     * @作者: yeyaowen
     */
    @GetMapping("/selectByType")
    public R selectByType(LoggerDto dto){
        if (LoggerDto.isEmpty(dto)){
            return R.error("服务类型为空, 请上传!");
        }
        List<ServerInfo> serverInfos = loggerService.selectByType(dto);
        return R.ok().put("serverInfos", serverInfos);
    }

    /**
     * 根据ID 删除日志
     * @param id
     * @return
     */
    @GetMapping("delete")
    public R deleteLogger(Long id){
        loggerService.deleteById(id);
        return R.ok();
    }

    /**
     * 保存日志
     * @param serverLog
     * @return
     */
    @PostMapping("/save")
    public R save(LogEntity serverLog){
        boolean result = loggerService.save(serverLog);
        if (result){
            return R.ok();
        } else {
            return R.error("新增失败");
        }
    }

    @PostMapping("/saveList")
    public R saveList(List<LogEntity> logEntities){
        boolean result = loggerService.saveList(logEntities);
        if (result){
            return R.ok();
        } else {
            return R.error("新增失败");
        }
    }

    /**
     * 修改日志
     * @param serverLog
     * @return
     */
    @PostMapping("/update")
    public R update(LogEntity serverLog){
        boolean result = loggerService.updateLogger(serverLog);
        if (result){
            return R.ok();
        } else {
            return R.error("新增失败");
        }
    }

    /**
     * 根据服务mac地址和服务名字查询日志
     * @param mac
     * @param serverType
     * @return
     */
    @GetMapping("/selectLoggerByServerNameAndMac")
    public R selectLoggerByServerNameAndMac(String mac, @RequestParam("serverType") String serverType, String searchTime){
        if (StringUtils.isEmpty(serverType)){
            return R.error("请上传服务类型!");
        }
        return R.ok().put("result", loggerService.selectLoggerByServerNameAndMac(mac, serverType, searchTime));
    }
}
