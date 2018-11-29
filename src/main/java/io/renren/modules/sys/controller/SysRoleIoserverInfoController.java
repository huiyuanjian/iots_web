package io.renren.modules.sys.controller;

import io.renren.common.utils.R;
import io.renren.modules.sys.entity.SysIotsIoserverInfoEntity;
import io.renren.modules.sys.service.SysIotsIoserverInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sys/role/ioserverInfo")
public class SysRoleIoserverInfoController {

    @Resource
    private SysIotsIoserverInfoService service;

    @RequestMapping(value="/delete")
    public R delete(SysIotsIoserverInfoEntity entity){
        service.deleteByIoserverIdAndRoleId(entity);
        return R.ok();
    }
}
