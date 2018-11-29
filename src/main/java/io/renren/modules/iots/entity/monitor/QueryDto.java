package io.renren.modules.iots.entity.monitor;

import java.io.Serializable;

public class QueryDto implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * ioserver 名字查询条件
     */
    private String ioserverName;
    /**
     * 设备名字 查询条件
     */
    private String equipmentName;
    /**
     * 变量名字查询条件 -- 展示不启用这个功能
     */
    private String variableName;
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 每页显示多少条
     */
    private Integer pageSize;
    /**
     * 0 正常  1异常 空 全部
     */
    private Integer status;

    public String getIoserverName() {
        return ioserverName;
    }

    public void setIoserverName(String ioserverName) {
        this.ioserverName = ioserverName;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
