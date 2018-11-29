package io.renren.modules.iots.entity.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EquipmentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    private Long equipmentId;

    /**
     * 设备名字
     */
    private String equipmentName;

    /**
     * 变量状态 0 正常 1 异常
     */
    private Integer status;

    private List<VarizableDto> varizableDtos = new ArrayList<VarizableDto>();

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<VarizableDto> getVarizableDtos() {
        return varizableDtos;
    }

    public void setVarizableDtos(List<VarizableDto> varizableDtos) {
        this.varizableDtos = varizableDtos;
    }
}
