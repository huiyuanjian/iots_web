package io.renren.modules.iots.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.renren.modules.iots.entity.IotsCtrlInfoEntity;
import io.renren.modules.iots.entity.IotsIoserverInfoEntity;
import io.renren.modules.iots.entity.IotsIotserverEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * IOServer信息表
 * 
 * @author lfy.xys
 * @email liufengyuan@hengtaiboyuan.com
 * @date 2018-06-11 16:42:09
 */
public interface IotsIoserverInfoDao extends BaseMapper<IotsIoserverInfoEntity> {

	List<IotsIoserverInfoEntity> queryIOServerForPid(Long pid);

	List<IotsIoserverInfoEntity> queryIOServerForVarId(Long[] varIds);

    List<IotsIoserverInfoEntity> roleIotServerInfo(Map<String, Object> params);

	List<IotsIoserverInfoEntity> roleIotServerInfoList(Map<String, Object> params);

    List<IotsIoserverInfoEntity> selectByParam(Map<String, Object> params);

	List<IotsIoserverInfoEntity> selectByPids(Map<String, Object> params);

	int roleIotServerInfoListCount(Map<String, Object> params);

	int roleIotServerInfoCount(Map<String, Object> params);

    List<IotsIoserverInfoEntity> selectAll(@Param("name") String name);

	List<IotsIoserverInfoEntity> selectByIds(@Param("ids") List<Long> ids);

    List<IotsIoserverInfoEntity> selectAllByIdsAndName(@Param("name")String name, @Param("ids")String ids);

	List<IotsIotserverEntity> selectAllByName(@Param("name") String name);

	List<IotsIotserverEntity> selectioserverInfoByName(@Param("name") String name);

    String selectMacByIoserverId(@Param("pid") Long pid);

    List<String> selectAllMac(@Param("pid") Long pid);

	Long selectPid(@Param("id") String id);

	List<String> selectOther(@Param("id") String id, @Param("pid") Long pid);

	List<String> queryMacById(String id);

	IotsIoserverInfoEntity selectMacByName(@Param("name") String name);

	String selectMacByPid(@Param("id") String id);

	List<IotsIotserverEntity> queryCollectByCondi(Map<String,Object> map);

	int queryCollectByCondiTotal(Map<String, Object> params);

	int updateInfoyId(IotsIotserverEntity iotsIotserverEntity);

	/**
	 * 根据物联网接口ID 查询所有的配置接口
	 * @param pid
	 * @return
	 */
    List<IotsCtrlInfoEntity> selectAllCtrl(@Param("pid") Long pid);
}
