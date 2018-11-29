package io.renren.modules.iots.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

import io.renren.modules.iots.entity.IotsDistributionInterfaceEntity;
import io.renren.modules.iots.entity.IotsDistributionInterfaceIotserverEntity;

/**
 * 分发接口-物联网接口-关系
 */
public interface IotsDistributionInterfaceIotserverService extends IService<IotsDistributionInterfaceIotserverEntity> {

	void insertBatch2(List<IotsDistributionInterfaceIotserverEntity> entityList);

    List<Long> selectByDistributionId(Long id);

    void deleteByDistributionId(Long id);

    void issuedByTheConfiguration(List<IotsDistributionInterfaceIotserverEntity> entityList);

    void setStatus(Long distributionId);

    /**
     * 下发配置信息（默认是非删除时的配置信息）
     */
    void sendConfig( IotsDistributionInterfaceEntity iotsDistributionInterfaceEntity);

    /**
     * 下发配置信息
     * isdel 是true的时候表示是删除时的配置文件
     * isdel 是false的时候是非删除时的配置文件
     */
    void sendConfig( IotsDistributionInterfaceEntity iotsDistributionInterfaceEntity,boolean isdel);

    /**
     * 批量新增分发接口与物联网接口的关联关系
     * list 是物联网接口的id的集合
     * distribution_id 是分发接口的id
     */
    boolean insertListOfIotserver(List<Long> list,long distribution_id);
}
