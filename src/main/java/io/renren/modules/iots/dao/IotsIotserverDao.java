package io.renren.modules.iots.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.iots.entity.IotsCtrlInfoEntity;
import io.renren.modules.iots.entity.IotsIoserverInfoEntity;
import io.renren.modules.iots.entity.IotsIotserverEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * IOT Server端 ，即分组表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface IotsIotserverDao extends BaseMapper<IotsIotserverEntity> {

    List<IotsIotserverEntity> selectAll(Map<String, Object> params);

    List<String> selectByMac(@Param("mac") String mac);

    List<String> selectMacsById(@Param("pid") Long pid);

    List<IotsIoserverInfoEntity> selectAllIoserver(@Param("id") Long id);

    IotsIotserverEntity getCollectById(@Param("id") Long id);

    void setStatus(@Param("mac") String mac, @Param("serverName") String serverName, @Param("status") Integer status);
}
