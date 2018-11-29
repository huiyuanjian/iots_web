package io.renren.modules.iots.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备状态的bean
 * 
 * @author lfy.xys
 * @date 2018年6月20日
 *
 */
public class EqmStateEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * id
	 */
	private Long id;
	/**
	 * 更新时间
	 */
	private Date updatetime;
	/**
	 * 状态
	 */
	private Integer state;
		
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	
}
