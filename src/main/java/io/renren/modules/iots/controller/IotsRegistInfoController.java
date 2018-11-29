package io.renren.modules.iots.controller;

import io.renren.common.utils.R;
import io.renren.modules.iots.entity.IotsRegistInfoEntity;
import io.renren.modules.iots.service.IotsRegistInfoServerI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册信息验证
 */
@RestController
@RequestMapping("registinfo")
public class IotsRegistInfoController {

    @Autowired
    private IotsRegistInfoServerI iotsRegistInfoServerI;

    /**
     * 验证 collect 的mac注册信息
     */
    @PostMapping("checkCollect")
    public R checkCollectRegistInfo(String mac){
        IotsRegistInfoEntity iotsRegistInfoEntity = iotsRegistInfoServerI.queryByTypeAndMac("COLLECT",mac);
        if (iotsRegistInfoEntity == null){
            return R.error(-1,"测试连接失败！请确认mac地址是否正确！");
        } else {
            return R.ok();
        }
    }

    /**
     * 验证 proxy 的mac注册信息
     */
    @PostMapping("checkProxy")
    public R checkProxyRegistInfo(String mac){
        IotsRegistInfoEntity iotsRegistInfoEntity = iotsRegistInfoServerI.queryByTypeAndMac("PROXY",mac);
        if (iotsRegistInfoEntity == null){
            return R.error(-1,"测试连接失败！请确认mac地址是否正确！");
        } else {
            return R.ok();
        }
    }


    /**
     * 验证 proxy 的mac注册信息
     */
    @PostMapping("checkCtrl")
    public R checkCtrlRegistInfo(String mac){
        IotsRegistInfoEntity iotsRegistInfoEntity = iotsRegistInfoServerI.queryByTypeAndMac("CONTROL",mac);
        if (iotsRegistInfoEntity == null){
            return R.error(-1,"测试连接失败！请确认mac地址是否正确！");
        } else {
            return R.ok();
        }
    }
}
