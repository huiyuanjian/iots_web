package io.renren.modules.iots.dao;

import io.renren.modules.iots.entity.IotsCtrlInfoEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.Map;

/**
 * IOT Server端 ，即分组表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface IotsCtrlInfoDao extends BaseMapper<IotsCtrlInfoEntity> {

    int newCtrlInfo(Map<String, String> params);

    int updateInfo(Map<String, String> params);

    IotsCtrlInfoEntity getCtrlInfoById(String id);
}
