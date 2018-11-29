package io.renren.modules.iots.entity.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IoserverDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  ioserver ID
     */
    private Long id;

    /**
     * ioserver Name
     */
    private String name;

    /**
     * 设备的集合
     */
    private List<EquipmentDto> equipmentDtoList = new ArrayList<EquipmentDto>();

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
        this.name = name;
    }

    public List<EquipmentDto> getEquipmentDtoList() {
        return equipmentDtoList;
    }

    public void setEquipmentDtoList(List<EquipmentDto> equipmentDtoList) {
        this.equipmentDtoList = equipmentDtoList;
    }
}
