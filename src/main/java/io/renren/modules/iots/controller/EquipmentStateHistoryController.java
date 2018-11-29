package io.renren.modules.iots.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.iots.entity.EquipmentStateHistoryEntity;
import io.renren.modules.iots.service.EquipmentStateHistoryService;



/**
 * 设备状态历史记录表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-21 10:26:49
 */
@RestController
@RequestMapping("iots/equipmentstatehistory")
public class EquipmentStateHistoryController {
    @Autowired
    private EquipmentStateHistoryService equipmentStateHistoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = equipmentStateHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
			EquipmentStateHistoryEntity equipmentStateHistory = equipmentStateHistoryService.selectById(id);

        return R.ok().put("equipmentStateHistory", equipmentStateHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody EquipmentStateHistoryEntity equipmentStateHistory){
			equipmentStateHistoryService.insert(equipmentStateHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody EquipmentStateHistoryEntity equipmentStateHistory){
    	ValidatorUtils.validateEntity(equipmentStateHistory);
		//equipmentStateHistoryService.updateById(equipmentStateHistory);//只更新不为空的值
		equipmentStateHistoryService.updateAllColumnById(equipmentStateHistory);//全部更新, 注意：没有传入的字段会，全部默认修改为 null
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
			equipmentStateHistoryService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
