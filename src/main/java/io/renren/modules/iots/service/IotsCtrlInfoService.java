package io.renren.modules.iots.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.iots.entity.IotsCtrlInfoEntity;

import java.util.Map;

/**
 * IOT Server端 ，即分组表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface IotsCtrlInfoService extends IService<IotsCtrlInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R newCtrlInfo(Map<String, String> params);

    R updateInfo(Map<String, String> params);

    R configSendData(Map<String, String> params) ;

    void issuedByTheConfiguration(IotsCtrlInfoEntity iotsCtrlInfo);

    void issuedByTheConfigurationByDelete(String[] ids);

    void issuedByTheConfigurationByUpdate(IotsCtrlInfoEntity iotsCtrlInfo);
}

