package io.renren.modules.iots.dao;

import io.renren.modules.iots.entity.IotsIoserverInfoEntity;
import io.renren.modules.iots.entity.IotsSystemMonitorEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.iots.entity.IotsVariableInfoEntity;
import io.renren.modules.iots.entity.monitor.ChildAndParent;
import io.renren.modules.iots.entity.monitor.QueryDto;
import io.renren.modules.model.entity.ModelGroupInfoEntity;
import io.renren.modules.model.entity.ModelVariableInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统监控信息存储表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:08
 */
public interface IotsSystemMonitorDao extends BaseMapper<IotsSystemMonitorEntity> {

    /**
     * 根据ioserverName 和设备名字查询, 并且是分页查询
     * @param dto
     * @return
     */
    List<IotsIoserverInfoEntity> selectIoserverAndEqipmentByPageAndName(QueryDto dto);

    /**
     * 根据设备ID查询所有绑定的变量
     * @param id
     * @return
     */
    List<IotsVariableInfoEntity> selectVariableByEquiId(Long id);

    /**
     * 根据ioserverName 和设备名字查询总数
     * @param dto
     * @return
     */
    Integer selectCountIoserverAndEqipmentByPageAndName(QueryDto dto);

    /**
     * 没有插叙条件的分页
     * @param dto
     * @return
     */
    List<IotsIoserverInfoEntity> selectIoserverAndEqipment(QueryDto dto);

    /**
     * 查询没有条件的总数
     * @param dto
     * @return
     */
    Integer selectCountIoserverAndEqipment(QueryDto dto);

    /**
     * 根据ioserverName 查询并分页
     * @param dto
     * @return
     */
    List<IotsIoserverInfoEntity> selectIoserverAndEqipmentByIoserverName(QueryDto dto);

    /**
     * 根据ioserverName 查询总数
     * @param dto
     * @return
     */
    Integer selectCountIoserverAndEqipmentByIoserverName(QueryDto dto);

    /**
     * 根据equipName 查询并分页
     * @param dto
     * @return
     */
    List<IotsIoserverInfoEntity> selectIoserverAndEqipmentByEqipmentName(QueryDto dto);

    /**
     * 根据equipName 查询总数
     * @param dto
     * @return
     */
    Integer selectCountIoserverAndEqipmentByEqipmentName(QueryDto dto);

    /**
     * 查询所有的设备名字
     * @return
     */
    List<String> queryAllEquipmentName();

    /**
     * 查询所有的模型的 设备和变量 分页
     * @param dto
     * @return
     */
    List<ModelGroupInfoEntity> selectModelAndEqipmentByPageAndName(QueryDto dto);

    /**
     * 查询所有的模型的 设备和变量 总数
     * @param dto
     * @return
     */
    Integer selectCountModelAndEqipmentByPageAndName(QueryDto dto);

    /**
     * 查询所有的模型的 设备下的变量
     * @param dto
     * @return
     */
    List<ModelVariableInfoEntity> selectModelVariableByEquiId(Long dyncId);

    /**
     * 没有条件的模型
     * @param dto
     * @return
     */
    List<ModelGroupInfoEntity> selectModelAndEqipment(QueryDto dto);

    /**
     * 没有条件的模型分页
     * @param dto
     * @return
     */
    Integer selectCountModelAndEqipment(QueryDto dto);

    /**
     * 模型分组有参数 设备没有参数
     * @param dto
     * @return
     */
    List<ModelGroupInfoEntity> selectModelAndEqipmentByIoserverName(QueryDto dto);

    /**
     * 模型分组有参数 设备没有参数 总数
     * @param dto
     * @return
     */
    Integer selectCountModelAndEqipmentByIoserverName(QueryDto dto);

    /**
     * 模型分组无参数 设备有参数
     * @param dto
     * @return
     */
    List<ModelGroupInfoEntity> selectModelAndEqipmentByEqipmentName(QueryDto dto);

    /**
     * 模型分组无参数 设备有参数 总数
     * @param dto
     * @return
     */
    Integer selectCountModelAndEqipmentByEqipmentName(QueryDto dto);

    /**
     * 根据子节点查询父节点
     * @param id
     * @return
     */
    List<ChildAndParent> selectPNamesByChildId(Long id);

    /**
     * 模型监控所有设备名字
     * @return
     */
    List<String> queryModelAllEquipmentName();

    /**
     * 根据模型的变量的ID 查询绑定的ioserver的变量的ID
     * @param dyncId
     * @return
     */
    Long selectBindVarIdByMidelVarId(Long dyncId);
}
