package io.renren.modules.iots.dao;

import io.renren.modules.iots.entity.CtrlIotsRelationEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 控制端 和 IOTServer  的关系表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface CtrlIotsRelationDao extends BaseMapper<CtrlIotsRelationEntity> {

    List<Long> selectByPid(@Param("id") Long id);

    void deleteByCtrlId(@Param("id") Long id);
}
