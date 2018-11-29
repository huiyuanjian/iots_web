package io.renren.modules.iots.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.modules.iots.entity.monitor.QueryDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.iots.entity.IotsSystemMonitorEntity;
import io.renren.modules.iots.service.IotsSystemMonitorService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;



/**
 * 系统监控信息存储表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:08
 */
@RestController
@RequestMapping("iots/iotssystemmonitor")
public class IotsSystemMonitorController {
    @Autowired
    private IotsSystemMonitorService iotsSystemMonitorService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("iots:iotssystemmonitor:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = iotsSystemMonitorService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("iots:iotssystemmonitor:info")
    public R info(@PathVariable("id") Long id){
			IotsSystemMonitorEntity iotsSystemMonitor = iotsSystemMonitorService.selectById(id);

        return R.ok().put("iotsSystemMonitor", iotsSystemMonitor);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("iots:iotssystemmonitor:save")
    public R save(@RequestBody IotsSystemMonitorEntity iotsSystemMonitor){
        iotsSystemMonitor.setIsDel(0);
        iotsSystemMonitorService.insert(iotsSystemMonitor);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("iots:iotssystemmonitor:update")
    public R update(@RequestBody IotsSystemMonitorEntity iotsSystemMonitor){
    	ValidatorUtils.validateEntity(iotsSystemMonitor);
		//iotsSystemMonitorService.updateById(iotsSystemMonitor);//只更新不为空的值
        iotsSystemMonitor.setIsDel(0);
		iotsSystemMonitorService.updateAllColumnById(iotsSystemMonitor);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("iots:iotssystemmonitor:delete")
    public R delete(@RequestBody Long[] ids){
			iotsSystemMonitorService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    //查询ioserver设备监控

    /**
     * 查询IOServer 设备监控 分页
     */
    @RequestMapping("/ioServerEquipmentMonitoringNoPage")
    public R ioServerEquipmentMonitoringNoPage(QueryDto dto){
        return this.iotsSystemMonitorService.ioServerEquipmentMonitoringNoPage(dto);
    }

    /**
     * 查询IOServer 设备监控 不分页
     */
    @RequestMapping("/ioServerEquipmentMonitoring")
    public R ioServerEquipmentMonitoring(QueryDto dto){
        return this.iotsSystemMonitorService.ioServerEquipmentMonitoring(dto);
    }

    /**
     * 查询所有设备名字
     */
    @RequestMapping("/allEquipmentName")
    public R queryAllEquipmentName(){
        return this.iotsSystemMonitorService.queryAllEquipmentName();
    }


    // 查询模型设备监控

    /**
     * 查询模型 设备监控 分页
     * @return
     */
    @RequestMapping("/modelEquipmentMonitoringNoPage")
    public R modelEquipmentMonitoringNoPage(QueryDto dto){
        return this.iotsSystemMonitorService.modelEquipmentMonitoringNoPage(dto);
    }

    /**
     * 查询模型 设备监控 分页
     * @return
     */
    @RequestMapping("/modelEquipmentMonitoring")
    public R modelEquipmentMonitoring(QueryDto dto){
        return this.iotsSystemMonitorService.modelEquipmentMonitoring(dto);
    }

    /**
     * 查询模型所有设备名字
     */
    @RequestMapping("/allModelEquipmentName")
    public R queryModelAllEquipmentName(){
        return this.iotsSystemMonitorService.queryModelAllEquipmentName();
    }
}
