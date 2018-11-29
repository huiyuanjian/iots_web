package io.renren.modules.iots.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 控制端 和 IOTServer  的关系表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
@TableName("ctrl_iots_relation")
public class CtrlIotsRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 控制端的id
	 */
	private Long ctrlId;
	/**
	 * IotServer的id
	 */
	private Long iotserverId;

	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：控制端的id
	 */
	public void setCtrlId(Long ctrlId) {
		this.ctrlId = ctrlId;
	}
	/**
	 * 获取：控制端的id
	 */
	public Long getCtrlId() {
		return ctrlId;
	}
	/**
	 * 设置：IotServer的id
	 */
	public void setIotserverId(Long iotserverId) {
		this.iotserverId = iotserverId;
	}
	/**
	 * 获取：IotServer的id
	 */
	public Long getIotserverId() {
		return iotserverId;
	}
}
