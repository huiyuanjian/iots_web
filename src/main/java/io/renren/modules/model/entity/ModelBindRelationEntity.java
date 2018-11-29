package io.renren.modules.model.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备信息表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 14:15:27
 */
@TableName("model_bind_relation")
public class ModelBindRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * web_id web端的变量id  ioserver变量ID  --id
	 */
	private Long webId;
	/**
	 * model_id model端的变量id  dyncId
	 */
	private Long modelId;
	/**
	 * 是否正确关联。 0 是，1 否。 同步之后，会出现删除的变量，则应该删除其关系。删除时候根据此字段删除。
	 */
	private Integer isflag;
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
	 * 设置：web_id web端的变量id
	 */
	public void setWebId(Long webId) {
		this.webId = webId;
	}
	/**
	 * 获取：web_id web端的变量id
	 */
	public Long getWebId() {
		return webId;
	}
	/**
	 * 设置：model_id model端的变量id
	 */
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	/**
	 * 获取：model_id model端的变量id
	 */
	public Long getModelId() {
		return modelId;
	}
	/**
	 * 设置：是否正确关联。 0 是，1 否。 同步之后，会出现删除的变量，则应该删除其关系。删除时候根据此字段删除。
	 */
	public void setIsflag(Integer isflag) {
		this.isflag = isflag;
	}
	/**
	 * 获取：是否正确关联。 0 是，1 否。 同步之后，会出现删除的变量，则应该删除其关系。删除时候根据此字段删除。
	 */
	public Integer getIsflag() {
		return isflag;
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
}
