package io.renren.modules.iots.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.modules.iots.dao.LoggerDao;
import io.renren.modules.iots.entity.LogEntity;
import io.renren.modules.iots.entity.log.LoggerDto;
import io.renren.modules.iots.entity.log.LoggerResult;
import io.renren.modules.iots.entity.log.ServerInfo;
import io.renren.modules.iots.service.LoggerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("loggerService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class LoggerServiceImpl extends ServiceImpl<LoggerDao, LogEntity> implements LoggerService {

    @Override
    public List<ServerInfo> selectByType(LoggerDto dto) {
        String type = dto.getType();
        List<ServerInfo> list = null;
        switch (type){
            case "COLLECT" :
                list = this.selectCollect(dto);
                break;
            case "PROXY":
                list = this.selectProxy(dto);
                break;
            case "DISTRIBUTE":
                list = this.selectDistribute(dto);
                break;
            case "WEB":
                //list = this.selectWeb(dto);
                break;
            case "CONTROL":
                list = this.selectControl(dto);
                break;
            default: break;
        }
        return list;
    }

    /**
     * 查询物联网接口
     * @return
     */
    private List<ServerInfo> selectCollect(LoggerDto dto){
        return this.baseMapper.selectCollect(dto);
    }

    /**
     * 查询采集代理
     * @return
     */
    private List<ServerInfo> selectProxy(LoggerDto dto){
        return this.baseMapper.selectProxy(dto);
    }

    /**
     * 查询分发接口的
     * @return
     */
    private List<ServerInfo> selectDistribute(LoggerDto dto){
        return this.baseMapper.selectDistribute(dto);
    }

    /**
     * 查询web的
     * @return
     */
    private List<ServerInfo> selectWeb(LoggerDto dto){
        return this.baseMapper.selectWeb(dto);
    }

    /**
     * 查询控制接口的
     * @return
     */
    private List<ServerInfo> selectControl(LoggerDto dto){
        return this.baseMapper.selectControl(dto);
    }


    /**
     * 插入单个日志
     * @param serverLog
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean save(LogEntity serverLog) {
        try {
            List<LogEntity> list = new ArrayList<>();
            list.add(serverLog);
            boolean result = this.saveList(list);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 日志批量插入
     * @param logEntities
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveList(List<LogEntity> logEntities) {
        try {
            if (logEntities != null && logEntities.size() > 0){
                Integer insert = this.baseMapper.saveList(logEntities);
                if (insert == 0){
                    return false;
                }
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改日志
     * @param serverLog
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateLogger(LogEntity serverLog) {
        try {
            Integer insert = this.baseMapper.updateById(serverLog);
            if (insert == 0){
                return false;
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据服务类型和mac地址查询日志数据
     * @param mac
     * @param serverType
     * @return
     */
    @Override
    public List<LoggerResult> selectLoggerByServerNameAndMac(String mac, String serverType, String searchTime) {
        return this.baseMapper.selectLoggerByServerNameAndMac(mac, serverType,searchTime);
    }
}
