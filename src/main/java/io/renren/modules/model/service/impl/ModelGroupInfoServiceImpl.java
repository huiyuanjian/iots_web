package io.renren.modules.model.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.datasources.DataSourceNames;
import io.renren.datasources.annotation.DataSource;
import io.renren.modules.iots.entity.IotsIoserverInfoEntity;
import io.renren.modules.iots.service.IotsIoserverInfoService;
import io.renren.modules.iots.service.IotsVariableInfoService;
import io.renren.modules.model.dao.ModelGroupInfoDao;
import io.renren.modules.model.entity.ModelBindRelationEntity;
import io.renren.modules.model.entity.ModelEquipmentInfoEntity;
import io.renren.modules.model.entity.ModelGroupInfoEntity;
import io.renren.modules.model.entity.ModelVariableInfoEntity;
import io.renren.modules.model.service.ModelBindRelationService;
import io.renren.modules.model.service.ModelEquipmentInfoService;
import io.renren.modules.model.service.ModelGroupInfoService;
import io.renren.modules.model.service.ModelVariableInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("modelGroupInfoService")
public class ModelGroupInfoServiceImpl extends ServiceImpl<ModelGroupInfoDao, ModelGroupInfoEntity> implements ModelGroupInfoService {
	@Autowired
	private ModelEquipmentInfoService modelEquipmentInfoService;
	@Autowired
	private ModelVariableInfoService modelVariableInfoService;
	@Autowired
	private ModelBindRelationService modelBindRelationService;
	@Autowired
	private IotsVariableInfoService iotsVariableInfoService;
	@Autowired
	private IotsIoserverInfoService iotsIoserverInfoService;

	@Autowired
	private ModelGroupInfoService modelGroupInfoService;

	private static Logger logger = LoggerFactory.getLogger(ModelGroupInfoServiceImpl.class);

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<ModelGroupInfoEntity> page = this.selectPage(new Query<ModelGroupInfoEntity>(params).getPage(), new EntityWrapper<ModelGroupInfoEntity>());

		return new PageUtils(page);
	}

	/**
	 * 查询分组下 的 设备 和 变量信息
	 * 
	 * params {"pid" : "ioserver的id"}
	 * 
	 * @author lfy.xys
	 * @date 2018年6月25日
	 *
	 * @param params
	 * @return
	 */
	@Override
	public R queryEqmVar(Map<String, String> params) {

		// 1、先查询 设备
		List<ModelEquipmentInfoEntity> eqmEntitys = modelEquipmentInfoService.queryEqmForPid(params);

		int n = eqmEntitys.size();
		if (n == 0) {
			// 没有设备数据
			return R.ok().put("child", eqmEntitys);
		}

		Long[] pids = new Long[n];// 设备的 id集合。
		for (int i = 0; i < n; i++) {
			pids[i] = eqmEntitys.get(i).getId();
		}

		// 2、查询 所有设备的 变量的 集合。
		List<ModelVariableInfoEntity> varEntitys = modelVariableInfoService.queryByIdsMap(pids);

		// 3、根据 变量的 设备id，分配
		// 循环 遍历 数量小的数据。 所以 数量大的 数据 放外面。
		for (ModelVariableInfoEntity varEntity : varEntitys) {
			for (ModelEquipmentInfoEntity eqmEntity : eqmEntitys) {
				// 设备id = 变量pid，匹配上则放置到 子list中
				if (varEntity.getPid() == eqmEntity.getId()) {
					eqmEntity.getList().add(varEntity);
				}
			}
		}

		return R.ok().put("child", eqmEntitys);
	}


	/**
	 * TODO 发送 ioserver 的配置
	 * 
	 * 只能 叶子节点的
	 * params {"pid" : "分组的id"}
	 * 
	 * @author lfy.xys
	 * @date 2018年6月25日
	 *
	 * @param params
	 * @return
	 */
	public R sendIOServerConfig(Map<String, String> params) {

		// 1、遍历  分组 查询设备
		List<ModelEquipmentInfoEntity> eqmEntitys = modelEquipmentInfoService.queryEqmForPid(params);
		
		// 2、设置 设备的 id集合
		Set<Long> eqmIdsSet = new HashSet<Long>(); 
		for (ModelEquipmentInfoEntity eqmEntity : eqmEntitys) {
			eqmIdsSet.add(eqmEntity.getId());
		}
		
		// 3、 根据ids，查询 所有 绑定的变量的 关系
		Long[] eqmIdsLong = new Long[eqmIdsSet.size()];
		for (int i = 0; i < eqmIdsSet.size(); i++) {
			eqmIdsLong[0] =  eqmIdsSet.iterator().next();
		}
		List<ModelBindRelationEntity> modelBindRelationEntitys = modelBindRelationService.queryBindRelation(eqmIdsLong);
		
		// 4、设置 web端 变量的 id集合
		Set<Long> webVarIdsSet = new HashSet<Long>(); 
		for (ModelBindRelationEntity relationEntity : modelBindRelationEntitys) {
			webVarIdsSet.add(relationEntity.getWebId());
		}
		
		// 5、根据 web端真正 的 变量集合，查询所有 IOServer
		Long[] webVarIdsLong = new Long[webVarIdsSet.size()];
		for (int i = 0; i < eqmIdsSet.size(); i++) {
			webVarIdsLong[0] =  webVarIdsSet.iterator().next();
		}
		List<IotsIoserverInfoEntity> IOServerEntitys = iotsIoserverInfoService.queryIOServerForVarId(webVarIdsLong);
		
		// 6、获取这些 ios的 ip
		Set<String> ipSet = new HashSet<String>(); 
		for (IotsIoserverInfoEntity IOServerEntity : IOServerEntitys) {
			ipSet.add(IOServerEntity.getIp());
		}
		
		// 7、 TODO 查询 这些 ip下的 eqm + var。  一个ip对应一个 ioserver。 每个 ip 发送 各自的 ioserver
		
		
		
		// 8、 TODO 打包 eqm+var，发送到 iots
		
		
		return null;
	}
	
	/**
	 * 5 采集代理配置消息  6 采集代理配置消息返回值
	 */
//	public void getIOTPBean(CollectionAgentConfig entity) {
//		// TODO
//	}

	/**
	 * 查询 第二个数据源的数据
	 * @author yeyaowen
	 * @date 2018年11月5日
	 * @return
	 */
	@DataSource(name = DataSourceNames.SECOND)
	@Override
	public List<ModelGroupInfoEntity> queryAllList() {
		return this.baseMapper.selectList(null);
	}


	/**
	 * @author yeyaowen
	 * 分组数据同步方法
	 * @return
	 */
	@Override
	public R syncData() {
		// 查询 模型的 库 的数据
		List<ModelGroupInfoEntity> modelEntitys = modelGroupInfoService.queryAllList();
		if (modelEntitys != null && modelEntitys.size() > 0){
			logger.info("查到数据个数 ：" + modelEntitys.size());
			modelEntitys.stream().forEach(modelEntity -> {
				// 获取 model 分组ID
				Long modelGroupId = modelEntity.getId();
				//根据modelGroupId 进行本地数据查询
				Long nativeGroupId = this.baseMapper.selectByDyncId(modelGroupId);
				if (nativeGroupId != null){
					//修改
					modelEntity.setId(nativeGroupId);
					modelEntity.setDyncId(modelGroupId);
					this.baseMapper.updateById(modelEntity);
				} else {
					modelEntity.setId(nativeGroupId);
					modelEntity.setDyncId(modelGroupId);
					this.baseMapper.insert(modelEntity);
				}
			});
		}
		return R.ok().put("msg", "同步完成。");
	}

	@Override
	public List<ModelGroupInfoEntity> queryTree(Map<String, Object> params) {
		//获取所有的节点
		List<ModelGroupInfoEntity> list = this.baseMapper.queryTree(null);
		if (list != null && list.size() > 0){
			//找到所有的1级节点
			List<ModelGroupInfoEntity> mList = null;
			if (params == null || params.size() == 0){
				mList = new ArrayList<>();
				for (ModelGroupInfoEntity modelGroupInfoEntity : list) {
					if (modelGroupInfoEntity.getPid() == 0L || modelGroupInfoEntity.getPid() == null){
						mList.add(modelGroupInfoEntity);
					}
				}
			} else {
				mList = this.baseMapper.queryTree(params);
			}
			//为一级节点设置子节点准备递归
			for (ModelGroupInfoEntity model : mList) {
				//获取父菜单下所有子菜单调用getChildList
				List<ModelGroupInfoEntity> childList = getChildList(String.valueOf(model.getDyncId()),list);
				model.setChildren(childList);
			}
			return mList;
		}
		return null;
	}

	private List<ModelGroupInfoEntity> getChildList(String id,List<ModelGroupInfoEntity> mList){
		//构建子菜单
		List<ModelGroupInfoEntity> childList = new ArrayList<>();
		//遍历传入的list
		for (ModelGroupInfoEntity m : mList) {
			//所有菜单的父id与传入的根节点id比较，若相等则为该级菜单的子菜单
			if (String.valueOf(m.getPid()).equals(id)){
				childList.add(m);
			}
		}
		//递归
		for (ModelGroupInfoEntity m:childList) {
			m.setChildren(getChildList(String.valueOf(m.getDyncId()),mList));
		}
		if (childList.size() == 0){
			return null;
		}
		return childList;
	}
}
