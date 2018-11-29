package io.renren.modules.model.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.model.entity.ModelGroupInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 分组表
 *
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-25 10:52:39
 */
public interface ModelGroupInfoService extends IService<ModelGroupInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

	R queryEqmVar(Map<String, String> params);

    R syncData();

    List<ModelGroupInfoEntity> queryAllList();

    List<ModelGroupInfoEntity> queryTree(Map<String, Object> params);
}

