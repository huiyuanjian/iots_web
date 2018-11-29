package io.renren.modules.iots.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统监控信息存储表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:08
 */
@TableName("iots_system_monitor")
public class IotsSystemMonitorEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 所属系统
	 */
	private Long systemId;
	/**
	 * 所属系统的ip
	 */
	private String systemIp;
	/**
	 * 内存总量
	 */
	private String totalmemory;
	/**
	 * 内存使用量
	 */
	private String usedMemory;
	/**
	 * cpu个数
	 */
	private Integer cpuLength;
	/**
	 * cpu的id
	 */
	private Integer cpuId;
	/**
	 * cpu使用率
	 */
	private String cpuUsed;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 删除标记 0未删除 1已删除
	 */
	@TableLogic
	private Integer isDel;

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
	 * 设置：所属系统
	 */
	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}
	/**
	 * 获取：所属系统
	 */
	public Long getSystemId() {
		return systemId;
	}
	/**
	 * 设置：所属系统的ip
	 */
	public void setSystemIp(String systemIp) {
		this.systemIp = systemIp;
	}
	/**
	 * 获取：所属系统的ip
	 */
	public String getSystemIp() {
		return systemIp;
	}
	/**
	 * 设置：内存总量
	 */
	public void setTotalmemory(String totalmemory) {
		this.totalmemory = totalmemory;
	}
	/**
	 * 获取：内存总量
	 */
	public String getTotalmemory() {
		return totalmemory;
	}
	/**
	 * 设置：内存使用量
	 */
	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}
	/**
	 * 获取：内存使用量
	 */
	public String getUsedMemory() {
		return usedMemory;
	}
	/**
	 * 设置：cpu个数
	 */
	public void setCpuLength(Integer cpuLength) {
		this.cpuLength = cpuLength;
	}
	/**
	 * 获取：cpu个数
	 */
	public Integer getCpuLength() {
		return cpuLength;
	}
	/**
	 * 设置：cpu的id
	 */
	public void setCpuId(Integer cpuId) {
		this.cpuId = cpuId;
	}
	/**
	 * 获取：cpu的id
	 */
	public Integer getCpuId() {
		return cpuId;
	}
	/**
	 * 设置：cpu使用率
	 */
	public void setCpuUsed(String cpuUsed) {
		this.cpuUsed = cpuUsed;
	}
	/**
	 * 获取：cpu使用率
	 */
	public String getCpuUsed() {
		return cpuUsed;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
}
