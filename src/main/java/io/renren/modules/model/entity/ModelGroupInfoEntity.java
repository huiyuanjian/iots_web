package io.renren.modules.model.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分组表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
@TableName("model_group_info")
public class ModelGroupInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * 用户数据同步区别model 和本机的ID
	 */
	private Long dyncId;
	/**
	 * 分组名字
	 */
	private String name;
	/**
	 * 分组对应的kafka主题
	 */
	private String topic;
	/**
	 * ip地址
	 */
	private String ip;
	/**
	 * port端口
	 */
	private Integer port;
	/**
	 * 状态
	 */
	private Integer state;
	/**
	 * 是否叶子节点
	 */
	private Integer isleaf;
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
	private Date createtime;
	/**
	 * 更新者ID
	 */
	private Long updator;
	/**
	 * 更新时间
	 */
	private Date updatetime;
	/**
	 * 是否启用（0 表示未启用  1 表示已启用 ）
	 */
	private Integer enable;

	/**
	 * 删除标记
	 */
	private Integer isDel;

	@TableField(exist=false)
	private List<ModelGroupInfoEntity> children = new ArrayList<>(0);

	@TableField(exist=false)
	private List<ModelEquipmentInfoEntity> list = new ArrayList<>(0);

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
	 * 设置：ip地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * 获取：ip地址
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 设置：port端口
	 */
	public void setPort(Integer port) {
		this.port = port;
	}
	/**
	 * 获取：port端口
	 */
	public Integer getPort() {
		return port;
	}
	/**
	 * 设置：状态
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：状态
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

	public Integer getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}

	public Long getDyncId() {
		return dyncId;
	}

	public void setDyncId(Long dyncId) {
		this.dyncId = dyncId;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public List<ModelGroupInfoEntity> getChildren() {
		return children;
	}

	public void setChildren(List<ModelGroupInfoEntity> children) {
		this.children = children;
	}

	public List<ModelEquipmentInfoEntity> getList() {
		return list;
	}

	public void setList(List<ModelEquipmentInfoEntity> list) {
		this.list = list;
	}
}
