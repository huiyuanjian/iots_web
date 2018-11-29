package io.renren.modules.iots.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备状态历史记录表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-21 10:26:49
 */
@TableName("equipment_state_history")
public class EquipmentStateHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 设备id
	 */
	private Long eqmid;
	/**
	 * 状态（0 在线 ，1 不在线）
	 */
	private Integer state;
	/**
	 * 说明
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createtime;

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
	 * 设置：设备id
	 */
	public void setEqmid(Long eqmid) {
		this.eqmid = eqmid;
	}
	/**
	 * 获取：设备id
	 */
	public Long getEqmid() {
		return eqmid;
	}
	/**
	 * 设置：状态（0 在线 ，1 不在线）
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：状态（0 在线 ，1 不在线）
	 */
	public Integer getState() {
		return state;
	}
	/**
	 * 设置：说明
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：说明
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreatetime() {
		return createtime;
	}
}
