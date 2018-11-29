package io.renren.timer;

import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.model.service.ModelEquipmentInfoService;
import io.renren.modules.model.service.ModelGroupInfoService;
import io.renren.modules.model.service.ModelVariableInfoService;

import java.util.TimerTask;

/**
 * 同步模型的定时任务
 * 
 * 如果直接数据查询，则不需要定时任务
 * 
 * @author lfy.xys
 * @date 2018年6月26日
 *
 */
public class SyncModelTimer extends TimerTask {

	/**
	 *  -> @AutoWired注入失败的原因:
	 * 	-> 继承TimerTask后，会直接运行run()方法，还没来得及执行标记进行注入
	 *
	 */
	private final ModelVariableInfoService modelVariableInfoService = (ModelVariableInfoService) SpringContextUtils.getBean("modelVariableInfoService");

	private final ModelGroupInfoService modelGroupInfoService = (ModelGroupInfoService) SpringContextUtils.getBean("modelGroupInfoService");

	private final ModelEquipmentInfoService modelEquipmentInfoService = (ModelEquipmentInfoService)SpringContextUtils.getBean("modelEquipmentInfoService");

	@Override
	public void run() {
		System.out.println("开始执行timer");
		this.syncModelData();
	}
	
	/**
	 * 同步数据库的数据
	 * @author lfy.xys
	 * @date 2018年6月26日
	 *
	 */
	public void syncModelData() {
		//同步三个表的数据, 分组, 设备, 变量
		modelGroupInfoService.syncData();      //分组
		modelEquipmentInfoService.syncData();  //设备
		modelVariableInfoService.syncData();   //变量
	}
}
