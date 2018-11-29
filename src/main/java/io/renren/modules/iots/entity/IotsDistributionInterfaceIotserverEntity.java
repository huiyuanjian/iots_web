package io.renren.modules.iots.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 分发接口-物联网接口-关系
 */
@TableName("iots_distribution_iotserver")
public class IotsDistributionInterfaceIotserverEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;

    private Long distributionId;

    private Long iotserverId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(Long distributionId) {
        this.distributionId = distributionId;
    }

    public Long getIotserverId() {
        return iotserverId;
    }

    public void setIotserverId(Long iotserverId) {
        this.iotserverId = iotserverId;
    }

	@Override
	public String toString() {
		return "IotsDistributionInterfaceIotserverEntity [id=" + id + ", distributionId=" + distributionId
				+ ", iotserverId=" + iotserverId + "]";
	}
    
}
