package io.renren.modules.iots.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 分发接口管理
 */
@TableName("iots_distribution_interface")
public class IotsDistributionInterfaceEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@TableId
	private Long id;

	@Excel(name="分发接口服务名称", orderNum = "0")
    private String name;

    @Excel(name="描述", orderNum = "1")
    private String remark;

    @Excel(name="下发分发配置", orderNum = "2")
    private Integer state;

    @Excel(name="ip", orderNum = "3")
    private String ip;

    @Excel(name="端口", orderNum = "4")
    private String port;

    @Excel(name="创建者", orderNum = "5")
    private Long creator;

    @Excel(name="创建时间", orderNum = "6")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name="更新者", orderNum = "7")
    private Long updator;

    @Excel(name="更新时间", orderNum = "8")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableLogic
    private Integer isDel;

    @TableField(exist = false)
    private List<Long> configIds = new ArrayList<>(0);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
        this.ip = ip == null ? null : ip.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdator() {
        return updator;
    }

    public void setUpdator(Long updator) {
        this.updator = updator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public List<Long> getConfigIds() {
        return configIds;
    }

    public void setConfigIds(List<Long> configIds) {
        this.configIds = configIds;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}
