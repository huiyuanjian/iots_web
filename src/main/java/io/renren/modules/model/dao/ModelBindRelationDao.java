package io.renren.modules.model.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.renren.modules.iots.entity.IotsEquipmentInfoEntity;
import io.renren.modules.iots.entity.IotsIoserverInfoEntity;
import io.renren.modules.iots.entity.IotsIotserverEntity;
import io.renren.modules.iots.entity.IotsVariableInfoEntity;
import io.renren.modules.iots.entity.monitor.ChildAndParent;
import io.renren.modules.model.entity.ModelBindRelationEntity;
import io.renren.modules.model.entity.ModelEquipmentInfoEntity;
import io.renren.modules.model.entity.ModelVariableInfoEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 设备信息表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 14:15:27
 */
public interface ModelBindRelationDao extends BaseMapper<ModelBindRelationEntity> {
	
	List<ModelBindRelationEntity> queryBindRelation(Long[] modelIds);

    void deleteByDyncId(@Param("id") Long id, @Param("webId") Long webId);

    Long selectByModelId(@Param("modelId") Long modelId);

    List<ModelBindRelationEntity> selectByDyncId(@Param("dyncId") Long dyncId);

    IotsIoserverInfoEntity selectIoserverByVarid(@Param("varId") String varId);

    IotsEquipmentInfoEntity selectEquiByDyncId(@Param("dyncId") String dyncId);

    IotsVariableInfoEntity selectVarByDyncId(@Param("dyncId") String dyncId);

    ModelEquipmentInfoEntity selectModelEquByDyncId(@Param("dyncId") String dyncId);

    ModelVariableInfoEntity selectModelVarByDyncId(@Param("dyncId")String dyncId);

    List<ChildAndParent> selectPNamesByChildId(@Param("id") Long id);

    List<ModelEquipmentInfoEntity> selectAllEqui(@Param("id") Long id);

    List<ModelVariableInfoEntity> selectAllVarByEquiId(@Param("dyncId") Long dyncId);

    List<Long> selectAllBindVarIds(@Param("dyncId") Long dyncId,@Param("disableId") Long disableId);

    String selectMacByIoserverInfoId(@Param("pid") Long pid);
}
