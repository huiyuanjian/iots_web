package io.renren.modules.iots.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.iots.entity.IotsSystemMonitorEntity;
import io.renren.modules.iots.entity.monitor.QueryDto;

import java.util.Map;

/**
 * 系统监控信息存储表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:08
 */
public interface IotsSystemMonitorService extends IService<IotsSystemMonitorEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 用于查询 ioserver 设备 变量 有分页的
     * @param dto
     * @return
     */
    R ioServerEquipmentMonitoringNoPage(QueryDto dto);

    /**
     * 用于查询 ioserver 设备 变量
     * @param dto
     * @return
     */
    R ioServerEquipmentMonitoring(QueryDto dto);

    /**
     * 查询所有的设备名字
     * @return
     */
    R queryAllEquipmentName();

    /**
     * 查询模型端监控 并分页
     * @param dto
     * @return
     */
    R modelEquipmentMonitoringNoPage(QueryDto dto);

    /**
     * 查询模型端监控 未分页
     * @param dto
     * @return
     */
    R modelEquipmentMonitoring(QueryDto dto);

    /**
     * 模型监控所有设备名字
     * @return
     */
    R queryModelAllEquipmentName();
}

