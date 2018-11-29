package io.renren.modules.iots.entity.status;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 服务的状态信息实体类
 */
public class ServerStatus implements Serializable {

    private String name;

    /**
     * 状态  0 正常 1 异常
     */
    private Integer status;

    private Integer totalNum;

    private Integer AnomalyTotalNum;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date latestReportingTime;

    private List<AnomalyInfo> anomalyInfos = new ArrayList<AnomalyInfo>(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getAnomalyTotalNum() {
        return AnomalyTotalNum;
    }

    public void setAnomalyTotalNum(Integer anomalyTotalNum) {
        AnomalyTotalNum = anomalyTotalNum;
    }

    public Date getLatestReportingTime() {
        return latestReportingTime;
    }

    public void setLatestReportingTime(Date latestReportingTime) {
        this.latestReportingTime = latestReportingTime;
    }

    public List<AnomalyInfo> getAnomalyInfos() {
        return anomalyInfos;
    }

    public void setAnomalyInfos(List<AnomalyInfo> anomalyInfos) {
        this.anomalyInfos = anomalyInfos;
    }
}
