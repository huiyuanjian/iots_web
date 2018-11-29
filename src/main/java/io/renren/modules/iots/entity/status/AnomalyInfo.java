package io.renren.modules.iots.entity.status;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务的异常信息
 */
public class AnomalyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date latestReportingTime;

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

    public Date getLatestReportingTime() {
        return latestReportingTime;
    }

    public void setLatestReportingTime(Date latestReportingTime) {
        this.latestReportingTime = latestReportingTime;
    }
}
