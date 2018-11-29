package io.renren.modules.iots.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * IOT Server端 ，即分组表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
@TableName("iots_ctrl_info")
public class IotsCtrlInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * parentId , 所属 IOTServer的id
	 */
	private Long pid;
	/**
	 * 控制端名字
	 */
	private String name;
	/**
	 * 控制端ip
	 * mac 地址
	 */
	private String ip;
	/**
	 * 控制端 port
	 */
	private Integer port;
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
	 * 预留字段1
	 * zcy
	 * 识别码
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
	 * 删除标记 0未删除 1已删除
	 */
	@TableLogic
	private Integer isDel;

	/**
	 * 表示控制接口和物联网接口的关系
	 */
	@TableField(exist=false)
	private List<Long> ioserverId = new ArrayList<>();

	/*
	 * @Author zcy
	 *  描述信息
	 * @Date 14:12 2018/11/15
	 **/
	private String remark;

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
	 * 设置：parentId , 所属 IOTServer的id
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/**
	 * 获取：parentId , 所属 IOTServer的id
	 */
	public Long getPid() {
		return pid;
	}
	/**
	 * 设置：控制端名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：控制端名字
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：控制端ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * 获取：控制端ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 设置：控制端 port
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	/**
	 * 获取：控制端 port
	 */
	public Integer getPort() {
		return port;
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

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public List<Long> getIoserverId() {
		return ioserverId;
	}

	public void setIoserverId(List<Long> ioserverId) {
		this.ioserverId = ioserverId;
	}
	/*
	* 描述
	 */
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
