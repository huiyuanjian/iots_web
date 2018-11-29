package io.renren.modules.model.dao;

import io.renren.modules.model.entity.ModelGroupInfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 分组表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
public interface ModelGroupInfoDao extends BaseMapper<ModelGroupInfoEntity> {

    Long selectByDyncId(@Param("modelGroupId") Long modelGroupId);

    List<ModelGroupInfoEntity> queryTree(Map<String, Object> params);

    List<Long> getEqIdsByGroup(@Param("dyncId") Long dyncId);
}
