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
 * IOServer信息表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
@TableName("iots_ioserver_info")
public class IotsIoserverInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * IOServer的名字
	 */
	private String name;

	/**
	 * 分组名字
	 */
	private String iotServerName;
	/**
	 * parentid ， 所属的分组的 id 
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
	 * IOServer厂商类型id
	 */
	private Long ioserverType;
	/**
	 * ip地址
	 */
	private String ip;
	/**
	 *  端口
	 */
	private Integer port;
	/**
	 * ioserver的状态
	 */
	private Integer state;
	/**
	 * 创建者id
	 */
	private Long creator;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createtime;
	/**
	 * 更新者id
	 */
	private Long updator;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatetime;
	/**
	 * 是否启用（0 表示未启用  1 表示已启用 ）
	 */
	private Integer enable;
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
	 * 删除标记 0未删除 1已删除
	 */
	@TableLogic
	private Integer isDel;

	/**
	 * 保存所有的设备和变量
	 */
	@TableField(exist = false)
	private List<IotsEquipmentInfoEntity> list = new ArrayList<>(0);

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
	 * 设置：IOServer的名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：IOServer的名字
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：parentid ， 所属的分组的 id 
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/**
	 * 获取：parentid ， 所属的分组的 id 
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
	 * 设置：IOServer厂商类型id
	 */
	public void setIoserverType(Long ioserverType) {
		this.ioserverType = ioserverType;
	}
	/**
	 * 获取：IOServer厂商类型id
	 */
	public Long getIoserverType() {
		return ioserverType;
	}
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 设置：创建者id
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建者id
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
	 * 设置：更新者id
	 */
	public void setUpdator(Long updator) {
		this.updator = updator;
	}
	/**
	 * 获取：更新者id
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
	 * 设置：是否启用（0 表示未启用  1 表示已启用 ）
	 */
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	/**
	 * 获取：是否启用（0 表示未启用  1 表示已启用 ）
	 */
	public Integer getEnable() {
		return enable;
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

	public String getIotServerName() {
		return iotServerName;
	}

	public void setIotServerName(String iotServerName) {
		this.iotServerName = iotServerName;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public List<IotsEquipmentInfoEntity> getList() {
		return list;
	}

	public void setList(List<IotsEquipmentInfoEntity> list) {
		this.list = list;
	}
}
