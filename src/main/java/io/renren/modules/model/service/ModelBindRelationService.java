package io.renren.modules.model.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.model.entity.ModelBindRelationEntity;

/**
 * 设备信息表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 14:15:27
 */
public interface ModelBindRelationService extends IService<ModelBindRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<ModelBindRelationEntity> queryBindRelation(Long[] modelIds);

    List<ModelBindRelationEntity> queryAllList();

    List<ModelBindRelationEntity> selectByDyncId(Long dyncId);

    void bind(Map<String, String> param);

    void noBind(Map<String, String> param);
}

