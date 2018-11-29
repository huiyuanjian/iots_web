package io.renren.modules.model.service.impl;

import io.renren.common.utils.R;
import io.renren.datasources.DataSourceNames;
import io.renren.datasources.annotation.DataSource;
import io.renren.modules.model.dao.ModelGroupInfoDao;
import io.renren.modules.model.entity.IoserverCollectorEntity;
import io.renren.modules.model.entity.ModelBindRelationEntity;
import io.renren.modules.model.entity.ModelVariableInfoEntity;
import io.renren.modules.model.service.ModelBindRelationService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.model.dao.ModelEquipmentInfoDao;
import io.renren.modules.model.entity.ModelEquipmentInfoEntity;
import io.renren.modules.model.service.ModelEquipmentInfoService;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("modelEquipmentInfoService")
public class ModelEquipmentInfoServiceImpl extends ServiceImpl<ModelEquipmentInfoDao, ModelEquipmentInfoEntity> implements ModelEquipmentInfoService {

    @Autowired
    private ModelEquipmentInfoService modelEquipmentInfoService;

    @Autowired
    private ModelGroupInfoDao modelGroupInfoDao;

    @Autowired
    private ModelEquipmentInfoDao modelEquipmentInfoDao;

    @Autowired
    private ModelBindRelationService modelBindRelationService;

    private static Logger logger = LoggerFactory.getLogger(ModelEquipmentInfoServiceImpl.class);


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	
    	// 根据pid查询
    	String pid = (String) params.get("pid");
    	
        Page<ModelEquipmentInfoEntity> page = this.selectPage(
                new Query<ModelEquipmentInfoEntity>(params).getPage(),
                new EntityWrapper<ModelEquipmentInfoEntity>()
                	.like(StringUtils.isNotBlank(pid), "pid", pid)
        );

        return new PageUtils(page);
    }
    
    @Override
    public List<ModelEquipmentInfoEntity> queryEqmForPid(Map<String, String> params){
    	String pid = params.get("pid");
		return baseMapper.queryEqmForPid(Long.parseLong(pid));
    }

    @Override
    public R syncData() {
        List<ModelEquipmentInfoEntity> modelEntitys = modelEquipmentInfoService.queryAllList();
        if (modelEntitys != null && modelEntitys.size() > 0){
            logger.info("查到数据个数 ：" + modelEntitys.size());
            modelEntitys.stream().forEach(modelEntity -> {
                // 获取model设备的ID
                Long modelEquipmentId = modelEntity.getId();
                //根据ID查询 本机的设备的ID
                Long nativeEquipId = this.baseMapper.selectByDyncId(modelEquipmentId);
                if (nativeEquipId != null){
                    //说明存在,就进行修改
                    modelEntity.setId(nativeEquipId);
                    modelEntity.setDyncId(modelEquipmentId);
                    this.baseMapper.updateById(modelEntity);
                } else {
                    //插入
                    modelEntity.setId(null);
                    modelEntity.setDyncId(modelEquipmentId);
                    this.baseMapper.insert(modelEntity);
                }
            });
        }
        return R.ok().put("msg", "同步完成。");
    }

    /**
     * 查询 第二个数据源的数据
     *
     * @author yeyaowen
     * @date 2018年11月5日
     *
     * @return
     */
    @DataSource(name = DataSourceNames.SECOND)
    @Override
    public List<ModelEquipmentInfoEntity> queryAllList() {
        return this.baseMapper.selectList(null);
    }

    @Override
    public List<ModelEquipmentInfoEntity> equipmentAndVariables(Map<String, Object> id) {
        //组id
        long dyncId= Long.parseLong(id.get("dyncId").toString());
        // 设备名字
        Object obj = id.get("name");
        String name = null;
        if (obj != null){
            if (obj.toString()!= null && !"".equals(obj.toString())){
                name = obj.toString();
            }
        }
        //当前页
        int nowPage = 0;//Integer.parseInt(columnMap.get("nowPage").toString());
        //页大小
        int pageSize = 0;//Integer.parseInt(columnMap.get("pageSize").toString());

        List<Long> dyncIds= new ArrayList<Long>();
        dyncIds = getRGroupId(dyncId,dyncIds);
        dyncIds.add(dyncId);
        List<ModelEquipmentInfoEntity> resultLi=null;
        if (!CollectionUtils.isEmpty(dyncIds)){
            //查询所对应的设备 变量信息
            resultLi= modelEquipmentInfoDao.queryEqmByGid(dyncIds,nowPage,pageSize,name);
            //处理设备重复数据
        }

        //遍历所有的设备载便利所有的变量, 设置变量是不是绑定的状态
        resultLi.stream().forEach(modelEquipmentInfoEntity -> {
            //查询采集器信息
            IoserverCollectorEntity collector = modelEquipmentInfoDao.selectCollector(modelEquipmentInfoEntity.getDyncId());
            if (collector != null){
                // 设置 ioserverId
                modelEquipmentInfoEntity.setIoserverId(collector.getIoserverId());
                // 设置ioserverName
                modelEquipmentInfoEntity.setIoserverName(collector.getIoserverName());
            } else {
                // 设置 ioserverId
                modelEquipmentInfoEntity.setIoserverId(0L);
                // 设置ioserverName
                modelEquipmentInfoEntity.setIoserverName("");
            }
            //获取变量 的集合
            List<ModelVariableInfoEntity> list = modelEquipmentInfoEntity.getList();
            //遍历变量
            list.stream().forEach(modelVariableInfoEntity -> {
                // 根据dyncID查询中间表, 看看是不是有绑定的变量, 如果没有, 设置是否绑定状态为1 , 有的话设置0
                List<ModelBindRelationEntity> count = modelBindRelationService.selectByDyncId(modelVariableInfoEntity.getDyncId());
                List<Long> ids = new ArrayList<>();
                for(ModelBindRelationEntity modelBindRelationEntity : count){
                    ids.add(modelBindRelationEntity.getWebId());
                }
                if (count == null || count.size() == 0){
                    //没有绑定的IOSERVER 设置绑定状态为1
                    modelVariableInfoEntity.setIsBind(1);
                } else {
                    modelVariableInfoEntity.setIsBind(0);
                    //把绑定的所有IOServer 变量ID放到集合
                    modelVariableInfoEntity.setBindIds(count);
                    modelVariableInfoEntity.setBindIoserverInfoIds(ids);
                }
            });
        });
        return resultLi;
    }


    /*
     * @Author zcy
     * @Description  递归查询
     * @Date 14:39 2018/10/30
     * @Param
     * @return
     **/

    public List<Long> getRGroupId(long id, List<Long> ids){
        //查询组下的所有子组id
        List<Long> dyncIds = modelGroupInfoDao.getEqIdsByGroup(id);
        if (!CollectionUtils.isEmpty(dyncIds)){
            ids.addAll(dyncIds);
            for (long dyncId:dyncIds){
                getRGroupId(dyncId,ids);
            }
        }
        return ids;
    }


    /**
     * 新增采集器
     */
    @Override
    public void insertCollector(IoserverCollectorEntity entity) {
        entity.setIsDel(0);
        modelEquipmentInfoDao.insertCollector(entity);
    }

    @Override
    public void noBindCollector(IoserverCollectorEntity entity) {
        modelEquipmentInfoDao.noBindCollector(entity);
    }
}
