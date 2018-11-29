package io.renren.modules.iots.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 变量信息表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:08
 */
@TableName("iots_variable_info")
public class IotsVariableInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * 同步ID
	 */
	private Long dyncId;
	
	/**
	 * 设备id
	 */
	@TableField(exist=false)
	private Long eqmId;
	/**
	 * 代理id
	 */
	@TableField(exist=false)
	private Long IOServerId;
	/**
	 * ip
	 */
	@TableField(exist=false)
	private String ip;
	
	
	
	/**
	 * 变量名字
	 */
	private String name;
	/**
	 * parentid ，所属设备的id
	 */
	private Long pid;
	/**
	 * 采集周期，默认为300，单位为秒（s）
	 */
	private Long cycle;
	/**
	 * 说明
	 */
	private String remark;
	/**
	 * 变量状态(包括是否需要采集等)
	 */
	private String variableStatus;
	/**
	 * 是否启用（0 未启用 1 已启用）
	 */
	private Integer enable;
	/**
	 * 创建者ID
	 */
	private Long creator;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createtime;
	/**
	 * 更新者ID
	 */
	private Long updator;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatetime;
	/**
	 * 是否已删除（0 表示未删除  1 表示已删除 ）
	 */
	private Integer isdel;
	/**
	 * 预留字段1
	 */
	private String field1;
	/**
	 * 预留字段2
	 */
	private String field2;
	/**
	 * 预留字段3
	 */
	private String field3;

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
	 * 设置：变量名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：变量名字
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：parentid ，所属设备的id
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/**
	 * 获取：parentid ，所属设备的id
	 */
	public Long getPid() {
		return pid;
	}
	/**
	 * 设置：采集周期，默认为300，单位为秒（s）
	 */
	public void setCycle(Long cycle) {
		this.cycle = cycle;
	}
	/**
	 * 获取：采集周期，默认为300，单位为秒（s）
	 */
	public Long getCycle() {
		return cycle;
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
	 * 设置：变量状态(包括是否需要采集等)
	 */
	public void setVariableStatus(String variableStatus) {
		this.variableStatus = variableStatus;
	}
	/**
	 * 获取：变量状态(包括是否需要采集等)
	 */
	public String getVariableStatus() {
		return variableStatus;
	}
	/**
	 * 设置：是否启用（0 未启用 1 已启用）
	 */
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	/**
	 * 获取：是否启用（0 未启用 1 已启用）
	 */
	public Integer getEnable() {
		return enable;
	}
	/**
	 * 设置：创建者ID
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建者ID
	 */
	public Long getCreator() {
		return creator;
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
	/**
	 * 设置：更新者ID
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	/**
	 * 获取：更新者ID
	 */
	public Long getUpdator() {
		return updator;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdatetime() {
		return updatetime;
	}
	/**
	 * 设置：是否已删除（0 表示未删除  1 表示已删除 ）
	 */
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	/**
	 * 获取：是否已删除（0 表示未删除  1 表示已删除 ）
	 */
	public Integer getIsdel() {
		return isdel;
	}
	/**
	 * 设置：预留字段1
	 */
	public void setField1(String field1) {
		this.field1 = field1;
	}
	/**
	 * 获取：预留字段1
	 */
	public String getField1() {
		return field1;
	}
	/**
	 * 设置：预留字段2
	 */
	public void setField2(String field2) {
		this.field2 = field2;
	}
	/**
	 * 获取：预留字段2
	 */
	public String getField2() {
		return field2;
	}
	/**
	 * 设置：预留字段3
	 */
	public void setField3(String field3) {
		this.field3 = field3;
	}
	/**
	 * 获取：预留字段3
	 */
	public String getField3() {
		return field3;
	}
	public Long getEqmId() {
		return eqmId;
	}
	public void setEqmId(Long eqmId) {
		this.eqmId = eqmId;
	}
	public Long getIOServerId() {
		return IOServerId;
	}
	public void setIOServerId(Long iOServerId) {
		IOServerId = iOServerId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getDyncId() {
		return dyncId;
	}

	public void setDyncId(Long dyncId) {
		this.dyncId = dyncId;
	}
}
