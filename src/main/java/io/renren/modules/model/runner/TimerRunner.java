package io.renren.modules.model.runner;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import io.renren.timer.SyncModelTimer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 创建timer定时任务
 * 
 * @author lfy.xys
 * @date 2018年6月26日
 *
 */
@Component
@Order(value = 2)  //配置执行顺序
public class TimerRunner  implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(TimerRunner.class);


	public void run(String... var1) throws Exception {
		logger.info("系统启动成功, --> 正在执行数据同步定时任务代码");
		Timer timer = new Timer();
		//10秒后开始执行，每分钟执行一次
		timer.schedule(new SyncModelTimer(), 10*1000, 60*1000);
		
	}
}
