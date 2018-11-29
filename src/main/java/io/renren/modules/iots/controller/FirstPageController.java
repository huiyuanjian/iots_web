package io.renren.modules.iots.controller;

import io.renren.common.utils.R;
import io.renren.modules.iots.entity.status.StatusInfo;
import io.renren.modules.iots.service.FirstPageServerI;
import io.renren.modules.iots.utils.sys.SystemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页监控控制类
 */
@RestController
@RequestMapping("firstPageController")
public class FirstPageController {

    @Autowired
    private FirstPageServerI firstPageServerImpl;

    /**
     * 获得所有的服务状态信息
     */
    @PostMapping("/getAllServer")
    public R getAllServer(){
        return R.ok().put("result",firstPageServerImpl.getAllServer());
    }

    /**
     * 获得所有的服务状态信息
     * status : 0 正常, 1 异常 全部为空
     * timeSort 排序 0 升序, 1 降序
     * currentPage  当前页码
     * pageSize 每页显示条数
     * serverType  服务类型
     */
    @PostMapping("/getServerByType")
    public R getServerByType(String serverType, Integer status, Integer timeSort, Integer currentPage, Integer pageSize){
        return R.ok().put("result",firstPageServerImpl.getServerByType(serverType,status,timeSort,currentPage,pageSize));
    }

    /**
     * 根据服务的类型和mac地址获得服务的状态信息
     */
    @PostMapping("/getStatusInfoByTypeAndMac")
    public R getStatusInfoByTypeAndMac(String serverType,String mac){
        if (serverType != null && !"".equals(serverType)){
            return R.ok().put("result",firstPageServerImpl.getStatusInfoByMac(serverType,mac));
        } else {
            return R.ok().put("result",firstPageServerImpl.getStatusInfoByMac(mac));
        }
    }

    /**
     * 根据服务的类型和mac地址获得服务的状态信息
     */
    @PostMapping("/getStatusInfoByMac")
    public R getStatusInfoByMac(String mac){
        return R.ok().put("result",firstPageServerImpl.getStatusInfoByMac(mac));
    }


    // TODO 这里是测试首页web监控的代码, 之后可以删除
    @GetMapping("/test")
    public R test(){
        StatusInfo info = null;
        for(int i = 0; i < 5; i++){
            info = new StatusInfo();
            info.setHost("8080");
            info.setMacAddress("9829A60691AB" + i);
            info.setServerType("WEB");
            info.setServerName("物联网接口-" + i);
            info.setRemark("测试首页监控");
            SystemCache.put(info);
        }
        for(int i = 0; i < 5; i++){
            info = new StatusInfo();
            info.setHost("8081");
            info.setMacAddress("A8206633A8DC" + i);
            info.setServerType("CONTROL");
            info.setServerName("控制接口-" + i);
            info.setRemark("测试首页监控");
            SystemCache.put(info);
        }
        return  R.ok();
    }
}
