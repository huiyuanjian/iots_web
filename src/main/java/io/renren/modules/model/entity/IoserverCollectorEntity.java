package io.renren.modules.model.entity;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * 采集管理采集器 实体类
 */
@TableName("ioserver_collector")
public class IoserverCollectorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long ioserverId;

    private String ioserverName;

    private Integer modelEquipId;

    @TableLogic
    private Integer isDel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIoserverId() {
        return ioserverId;
    }

    public void setIoserverId(Long ioserverId) {
        this.ioserverId = ioserverId;
    }

    public String getIoserverName() {
        return ioserverName;
    }

    public void setIoserverName(String ioserverName) {
        this.ioserverName = ioserverName;
    }

    public Integer getModelEquipId() {
        return modelEquipId;
    }

    public void setModelEquipId(Integer modelEquipId) {
        this.modelEquipId = modelEquipId;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}
