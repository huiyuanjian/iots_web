package io.renren.modules.iots.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.renren.modules.iots.entity.IotsDistributionInterfaceIotserverEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分发接口-物联网接口-关系
 */
public interface IotsDistributionInterfaceIotserverDao extends BaseMapper<IotsDistributionInterfaceIotserverEntity> {

    List<Long> selectByDistributionId(Long id);

    void deleteByDistributionId(Long id);

    void setStatus(@Param("id") Long distributionId);

    /**
     * 根据分发接口服务的server_id查询其管理的所有物联网接口的kafka主题
     */
    List<String> selectKafkaTopicById(@Param("id") long id);

    /**
     * 批量新增分发接口与物联网接口的关联关系
     * list 是物联网接口的id的集合
     * distribution_id 是分发接口的id
     */
    int inserListOfIotserver(@Param("list") List<Long> list,@Param("distribution_id") long distribution_id);
}
