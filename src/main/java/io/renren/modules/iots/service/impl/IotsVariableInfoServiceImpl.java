package io.renren.modules.iots.service.impl;

import java.util.List;
import java.util.Map;

import io.renren.modules.iots.entity.IotsEquipmentInfoEntity;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.iots.dao.IotsVariableInfoDao;
import io.renren.modules.iots.entity.IotsVariableInfoEntity;
import io.renren.modules.iots.service.IotsVariableInfoService;


@Service("iotsVariableInfoService")
public class IotsVariableInfoServiceImpl extends ServiceImpl<IotsVariableInfoDao, IotsVariableInfoEntity> implements IotsVariableInfoService {
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<IotsVariableInfoEntity> page = this.selectPage(
                new Query<IotsVariableInfoEntity>(params).getPage(),
                new EntityWrapper<IotsVariableInfoEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<IotsVariableInfoEntity> queryVarForPid(Map<String, String> params) {
		return baseMapper.queryVarForPid(params);
	}

	/**
	 * 查询 指定设备的 变量的 集合。
	 */
	@Override
	public List<IotsVariableInfoEntity> queryByPidsMap(Long[] pids) {
		return baseMapper.queryByPidsMap(pids);
	}

	/**
	 * 查询 指定变量的 集合
	 */
	@Override
	public List<IotsVariableInfoEntity> queryByIdsMap(Long[] pids) {
		return baseMapper.queryByIdsMap(pids);
	}

	@Override
	public IotsVariableInfoEntity selectByDyncId(Long varId, String varName, String equiName, Long id) {
		return this.baseMapper.selectByDyncId(varId,varName,equiName,id);
	}

	@Override
	public Long selectByEquiNameAndIoserverId(String device_name, long parseLong) {
		return this.baseMapper.selectByEquiNameAndIoserverId(device_name,parseLong);
	}

	@Override
	public List<IotsVariableInfoEntity> selectVarByIoserverId(String server_id) {
		return this.baseMapper.selectVarByIoserverId(server_id);
	}
}
