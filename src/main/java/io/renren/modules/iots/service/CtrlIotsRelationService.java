package io.renren.modules.iots.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.iots.entity.CtrlIotsRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 控制端 和 IOTServer  的关系表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface CtrlIotsRelationService extends IService<CtrlIotsRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<Long> selectByPid(Long id);

    void deleteByCtrlId(Long id);

    void issuedByTheConfiguration(CtrlIotsRelationEntity ctrlIotsRelation);
}

