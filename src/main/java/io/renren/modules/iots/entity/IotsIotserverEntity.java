package io.renren.modules.iots.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * IOT Server端 ，即分组表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
@TableName("iots_iotserver")
public class IotsIotserverEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 分组名字
	 */
	private String name;

	/**
	 * 采集周期
	 */
	private Integer cycle;
	/**
	 * 分组对应的kafka主题
	 */
	private String topic;
	/**
	 * 状态
	 */
	private Integer state;
	/**
	 * ip
	 */
	private String ip;
	/**
	 * ip
	 */
	private Integer port;
	/**
	 * 说明
	 */
	private String remark;
	/**
	 * 父节点id（当值为0时，表示没有父节点，即最高级别）
	 */
	private Long pid;
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
	 * 是否启用（0 表示未启用  1 表示已启用 ）
	 */
	private Integer enable;
	/**
	 * 预留字段1
	 * zcy
	 * 暂用作 操作人 名
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
	 * 删除标记
	 */
	@TableLogic
	private Integer isDel;

	@TableField(exist=false)
	private List<IotsIoserverInfoEntity> list = new ArrayList<IotsIoserverInfoEntity>();

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
	 * 设置：分组名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：分组名字
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：分组对应的kafka主题
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	/**
	 * 获取：分组对应的kafka主题
	 */
	public String getTopic() {
		return topic;
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
	 * 设置：父节点id（当值为0时，表示没有父节点，即最高级别）
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/**
	 * 获取：父节点id（当值为0时，表示没有父节点，即最高级别）
	 */
	public Long getPid() {
		return pid;
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
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

	public List<IotsIoserverInfoEntity> getList() {
		return list;
	}

	public void setList(List<IotsIoserverInfoEntity> list) {
		this.list = list;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getCycle() {
		return cycle;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}
}
