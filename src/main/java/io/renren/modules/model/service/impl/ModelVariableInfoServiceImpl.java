package io.renren.modules.model.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.datasources.DataSourceNames;
import io.renren.datasources.annotation.DataSource;
import io.renren.modules.model.dao.ModelVariableInfoDao;
import io.renren.modules.model.entity.ModelVariableInfoEntity;
import io.renren.modules.model.service.ModelVariableInfoService;


@Service("modelVariableInfoService")
public class ModelVariableInfoServiceImpl extends ServiceImpl<ModelVariableInfoDao, ModelVariableInfoEntity> implements ModelVariableInfoService {
	@Autowired
	private ModelVariableInfoService modelVariableInfoService;
	
	private static Logger logger = LoggerFactory.getLogger(ModelVariableInfoServiceImpl.class);
	
	/**
	 * 查询 第二个数据源的数据
	 *  
	 * @author lfy.xys
	 * @date 2018年6月26日
	 *
	 * @return
	 */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ModelVariableInfoEntity> page = modelVariableInfoService.selectPage(
                new Query<ModelVariableInfoEntity>(params).getPage(),
                new EntityWrapper<ModelVariableInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据 pid集合，查询所有变量
     */
    @DataSource(name = DataSourceNames.SECOND)
    @Override
    public List<ModelVariableInfoEntity> queryByIdsMap(Long[] pids){
    	return baseMapper.queryByIdsMap(pids);
    }


    /**
     * 根据 单个 pid，查询所有变量
     */
    @DataSource(name = DataSourceNames.SECOND)
	@Override
	public List<ModelVariableInfoEntity> queryVarForPid(Map<String, String> params) {
		return baseMapper.queryVarForPid(params);
	}
    
	/**
	 * 查询 第二个数据源的数据
	 *  
	 * @author lfy.xys
	 * @date 2018年6月26日
	 *
	 * @return
	 */
	@DataSource(name = DataSourceNames.SECOND)
	@Override
	public List<ModelVariableInfoEntity> queryAllList(){
		return baseMapper.selectList(null);
	} 
	
	/**
	 * 同步数据 -- 删除老的数据，把新的数据完全复制。
	 * 
	 * @author lfy.xys
	 * @date 2018年6月26日
	 *
	 * @return
	 */
	@Override
	public R syncData() {
		// 查询 模型的 库 的数据
		List<ModelVariableInfoEntity> modelEntitys = modelVariableInfoService.queryAllList();
		if (modelEntitys != null && modelEntitys.size() > 0){
			logger.info("查到数据个数 ：" + modelEntitys.size());
			modelEntitys.stream().forEach(modelEntity -> {
				//获取modelVariableId;
				Long modelVariableId = modelEntity.getId();
				// 根据ID查询本地数据
				Long nativeVariableId = this.baseMapper.selectByDyncId(modelVariableId);
				if (nativeVariableId != null){
					//修改
					modelEntity.setId(nativeVariableId);
					modelEntity.setDyncId(modelVariableId);
					this.baseMapper.updateById(modelEntity);
				}else{
					modelEntity.setId(nativeVariableId);
					modelEntity.setDyncId(modelVariableId);
					this.baseMapper.insert(modelEntity);
				}
			});
		}
		return R.ok().put("msg", "同步完成。");
	}

	@Override
	public void add(List<ModelVariableInfoEntity> entitys) {
		baseMapper.add(entitys);
	}
	
	
}
