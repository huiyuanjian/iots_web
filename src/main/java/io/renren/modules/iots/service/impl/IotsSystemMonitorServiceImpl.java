package io.renren.modules.iots.service.impl;

import io.renren.common.utils.R;
import io.renren.modules.iots.entity.*;
import io.renren.modules.iots.entity.monitor.*;
import io.renren.modules.logger.utils.RedisUtils;
import io.renren.modules.model.entity.ModelEquipmentInfoEntity;
import io.renren.modules.model.entity.ModelGroupInfoEntity;
import io.renren.modules.model.entity.ModelVariableInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.iots.dao.IotsSystemMonitorDao;
import io.renren.modules.iots.service.IotsSystemMonitorService;


@Service("iotsSystemMonitorService")
@Slf4j
public class IotsSystemMonitorServiceImpl extends ServiceImpl<IotsSystemMonitorDao, IotsSystemMonitorEntity> implements IotsSystemMonitorService {

    // ioserver redis 存的key
    private final String IOSERVER_VARIABLE_KEY = "ioserverVariable";

    @Value("${spring.redis.host}")
    private String REDIS_HOST;

    @Value("${spring.redis.port}")
    private String REDIS_PORT;

    @Value("${spring.redis.password}")
    private String REDIS_PASSWORD;

    /**
     * JedisPool
     */
    private RedisUtils redisUtils = null;

    {
        //redisUtils = new RedisUtils(REDIS_HOST.trim(),Integer.parseInt(REDIS_PORT.trim()),REDIS_PASSWORD.trim());
        redisUtils = new RedisUtils("10.10.20.171",6379,"123456");
        System.out.println("redis-> host: " + REDIS_HOST);
        System.out.println("redis-> port: " + REDIS_PORT);
        System.out.println("redis-> password: " + REDIS_PASSWORD);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<IotsSystemMonitorEntity> page = this.selectPage(
                new Query<IotsSystemMonitorEntity>(params).getPage(),
                new EntityWrapper<IotsSystemMonitorEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 用于查询 ioserver 设备 变量
     * @param dto
     * @return
     */
    @Override
    public R ioServerEquipmentMonitoringNoPage(QueryDto dto) {
        // 1. 先判断有没有分页参数, 没有分页参数查询的就是ioserver设备监控的数据, 反之是服务监控中的ioserver设备监控
        if (dto == null){
            log.info("这里什么参数都没有, 不知道是请求的ioserver设备的哪个接口");
            return R.error("请输入分页参数!");
        }
        // 2.说明参数封装实体类中的参数不为空，判断有没有分页参数,有的话是ioserver设备监控的数据, 反之是服务监控中的ioserver设备监控
        if (dto.getCurrentPage() != null && dto.getPageSize() != null){
            log.info("开始查询ioserver设备监控的数据");
            // 获取其他参数 TODO 这里的请求暂时不考虑 变量名字查询条件
            String ioserverName = dto.getIoserverName();
            log.info("ioserverName是: " + ioserverName);
            String equipmentName = dto.getEquipmentName();
            log.info("equipmentName是: " + equipmentName);
            if (!StringUtils.isBlank(ioserverName) && !StringUtils.isBlank(equipmentName)){
                //创建一个集合用来存放IotsIoserverInfoDto
                List<IoserverDto> ioserverDtos = new ArrayList<>();
                OurPage page = new OurPage();
                page.setCurrentPage(dto.getCurrentPage());
                page.setPageSize(dto.getPageSize());
                ResultPage resultPage = new ResultPage();
                resultPage.setPage(page);
                resultPage.setList(ioserverDtos);
                log.info("说明这两个请求参数都不是null");
                // 3.先查询ioserver和变量,并分页
                dto.setCurrentPage(dto.getCurrentPage() - 1);
                List<IotsIoserverInfoEntity> entitys = this.baseMapper.selectIoserverAndEqipmentByPageAndName(dto);
                // 查询 总数
                Integer count = this.baseMapper.selectCountIoserverAndEqipmentByPageAndName(dto);
                // 总页数  (totalCount + pageSize - 1) / pageSize
                Integer totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
                page.setTotalSize(count);
                page.setTotalPage(totalPage);
                IoserverDto ioserverDto = null;
                for (IotsIoserverInfoEntity entity : entitys) {
                    ioserverDto = new IoserverDto();
                    //封装ioserverDto
                    ioserverDto.setId(entity.getId());
                    ioserverDto.setName(entity.getName());
                    ioserverDtos.add(ioserverDto);
                    //创建一个集合用来保存设备
                    // 获取变量
                    List<IotsEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                    //创建设备dto集合
                    List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                    ioserverDto.setEquipmentDtoList(equipmentDtoList);
                    // 遍历
                    for (IotsEquipmentInfoEntity iotsEquipmentInfoEntity: iotsEquipmentList) {
                        // 创建设备dto
                        EquipmentDto equipmentDto = new EquipmentDto();
                        equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                        equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                        //根据设备ID 查询所有绑定的变量信息
                        List<IotsVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectVariableByEquiId(iotsEquipmentInfoEntity.getId());
                        //创建封装变量dto的集合
                        List<VarizableDto> varizableDtoList = new ArrayList<>();
                        equipmentDto.setVarizableDtos(varizableDtoList);
                        //创建一个变量状态标记
                        boolean varStatusFlag = false;
                        //遍历变量
                        for (IotsVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                            //创建 变量dto
                            VarizableDto varizableDto = new VarizableDto();
                            varizableDto.setVarizableId(iotsVariableInfoEntity.getId());
                            varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + iotsVariableInfoEntity.getId(), iotsVariableInfoEntity.getId().toString());
                            if (variable != null && !"".equals(variable)){
                                varizableDto.setVarizableValue(variable);
                                varStatusFlag = true;
                            }
                            varizableDtoList.add(varizableDto);
                        }
                        //判断整体的状态值
                        equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                        equipmentDtoList.add(equipmentDto);
                    }
                }
                return R.ok().put("page", resultPage);
            }

            //3. 这里执行没有查询参数的
            if (StringUtils.isBlank(ioserverName) && StringUtils.isBlank(equipmentName)) {
                //创建一个集合用来存放IotsIoserverInfoDto
                List<IoserverDto> ioserverDtos = new ArrayList<>();
                OurPage page = new OurPage();
                page.setCurrentPage(dto.getCurrentPage());
                page.setPageSize(dto.getPageSize());
                ResultPage resultPage = new ResultPage();
                resultPage.setPage(page);
                resultPage.setList(ioserverDtos);
                log.info("说明这两个请求参数都是null");
                // 3.先查询ioserver和变量,并分页
                dto.setCurrentPage(dto.getCurrentPage() - 1);
                List<IotsIoserverInfoEntity> entitys = this.baseMapper.selectIoserverAndEqipment(dto);
                // 查询 总数
                Integer count = this.baseMapper.selectCountIoserverAndEqipment(dto);
                // 总页数  (totalCount + pageSize - 1) / pageSize
                Integer totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
                page.setTotalSize(count);
                page.setTotalPage(totalPage);
                IoserverDto ioserverDto = null;
                for (IotsIoserverInfoEntity entity : entitys) {
                    ioserverDto = new IoserverDto();
                    //封装ioserverDto
                    ioserverDto.setId(entity.getId());
                    ioserverDto.setName(entity.getName());
                    ioserverDtos.add(ioserverDto);
                    //创建一个集合用来保存设备
                    // 获取变量
                    List<IotsEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                    //创建设备dto集合
                    List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                    ioserverDto.setEquipmentDtoList(equipmentDtoList);
                    // 遍历
                    for (IotsEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                        // 创建设备dto
                        EquipmentDto equipmentDto = new EquipmentDto();
                        equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                        equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                        //根据设备ID 查询所有绑定的变量信息
                        List<IotsVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectVariableByEquiId(iotsEquipmentInfoEntity.getId());
                        //创建封装变量dto的集合
                        List<VarizableDto> varizableDtoList = new ArrayList<>();
                        equipmentDto.setVarizableDtos(varizableDtoList);
                        //创建一个变量状态标记
                        boolean varStatusFlag = false;
                        //遍历变量
                        for (IotsVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                            //创建 变量dto
                            VarizableDto varizableDto = new VarizableDto();
                            varizableDto.setVarizableId(iotsVariableInfoEntity.getId());
                            varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + iotsVariableInfoEntity.getId(), iotsVariableInfoEntity.getId().toString());
                            if (variable != null && !"".equals(variable)) {
                                varizableDto.setVarizableValue(variable);
                                varStatusFlag = true;
                            }
                            varizableDtoList.add(varizableDto);
                        }
                        //判断整体的状态值
                        equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                        equipmentDtoList.add(equipmentDto);
                    }
                }
                return R.ok().put("page", resultPage);
            }

            //4. ioserver有参数 设备没有参数
            if (!StringUtils.isBlank(ioserverName) && StringUtils.isBlank(equipmentName)) {
                //创建一个集合用来存放IotsIoserverInfoDto
                List<IoserverDto> ioserverDtos = new ArrayList<>();
                OurPage page = new OurPage();
                page.setCurrentPage(dto.getCurrentPage());
                page.setPageSize(dto.getPageSize());
                ResultPage resultPage = new ResultPage();
                resultPage.setPage(page);
                resultPage.setList(ioserverDtos);
                log.info("ioserver有参数, 设备没有参数");
                // 3.先查询ioserver和变量,并分页
                dto.setCurrentPage(dto.getCurrentPage() - 1);
                List<IotsIoserverInfoEntity> entitys = this.baseMapper.selectIoserverAndEqipmentByIoserverName(dto);
                // 查询 总数
                Integer count = this.baseMapper.selectCountIoserverAndEqipmentByIoserverName(dto);
                // 总页数  (totalCount + pageSize - 1) / pageSize
                Integer totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
                page.setTotalSize(count);
                page.setTotalPage(totalPage);
                IoserverDto ioserverDto = null;
                for (IotsIoserverInfoEntity entity : entitys) {
                    ioserverDto = new IoserverDto();
                    //封装ioserverDto
                    ioserverDto.setId(entity.getId());
                    ioserverDto.setName(entity.getName());
                    ioserverDtos.add(ioserverDto);
                    //创建一个集合用来保存设备
                    // 获取变量
                    List<IotsEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                    //创建设备dto集合
                    List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                    ioserverDto.setEquipmentDtoList(equipmentDtoList);
                    // 遍历
                    for (IotsEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                        // 创建设备dto
                        EquipmentDto equipmentDto = new EquipmentDto();
                        equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                        equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                        //根据设备ID 查询所有绑定的变量信息
                        List<IotsVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectVariableByEquiId(iotsEquipmentInfoEntity.getId());
                        //创建封装变量dto的集合
                        List<VarizableDto> varizableDtoList = new ArrayList<>();
                        equipmentDto.setVarizableDtos(varizableDtoList);
                        //创建一个变量状态标记
                        boolean varStatusFlag = false;
                        //遍历变量
                        for (IotsVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                            //创建 变量dto
                            VarizableDto varizableDto = new VarizableDto();
                            varizableDto.setVarizableId(iotsVariableInfoEntity.getId());
                            varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + iotsVariableInfoEntity.getId(), iotsVariableInfoEntity.getId().toString());
                            if (variable != null && !"".equals(variable)) {
                                varizableDto.setVarizableValue(variable);
                                varStatusFlag = true;
                            }
                            varizableDtoList.add(varizableDto);
                        }
                        //判断整体的状态值
                        equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                        equipmentDtoList.add(equipmentDto);
                    }
                }
                return R.ok().put("page", resultPage);
            }

            //5. ioserver没有参数 设备有参数
            if (StringUtils.isBlank(ioserverName) && !StringUtils.isBlank(equipmentName)) {
                //创建一个集合用来存放IotsIoserverInfoDto
                List<IoserverDto> ioserverDtos = new ArrayList<>();
                OurPage page = new OurPage();
                page.setCurrentPage(dto.getCurrentPage());
                page.setPageSize(dto.getPageSize());
                ResultPage resultPage = new ResultPage();
                resultPage.setPage(page);
                resultPage.setList(ioserverDtos);
                log.info("ioserver没有有参数, 设备有参数");
                // 3.先查询ioserver和变量,并分页
                dto.setCurrentPage(dto.getCurrentPage() - 1);
                List<IotsIoserverInfoEntity> entitys = this.baseMapper.selectIoserverAndEqipmentByEqipmentName(dto);
                // 查询 总数
                Integer count = this.baseMapper.selectCountIoserverAndEqipmentByEqipmentName(dto);
                // 总页数  (totalCount + pageSize - 1) / pageSize
                Integer totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
                page.setTotalSize(count);
                page.setTotalPage(totalPage);
                IoserverDto ioserverDto = null;
                for (IotsIoserverInfoEntity entity : entitys) {
                    ioserverDto = new IoserverDto();
                    //封装ioserverDto
                    ioserverDto.setId(entity.getId());
                    ioserverDto.setName(entity.getName());
                    ioserverDtos.add(ioserverDto);
                    //创建一个集合用来保存设备
                    // 获取变量
                    List<IotsEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                    //创建设备dto集合
                    List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                    ioserverDto.setEquipmentDtoList(equipmentDtoList);
                    // 遍历
                    for (IotsEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                        // 创建设备dto
                        EquipmentDto equipmentDto = new EquipmentDto();
                        equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                        equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                        //根据设备ID 查询所有绑定的变量信息
                        List<IotsVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectVariableByEquiId(iotsEquipmentInfoEntity.getId());
                        //创建封装变量dto的集合
                        List<VarizableDto> varizableDtoList = new ArrayList<>();
                        equipmentDto.setVarizableDtos(varizableDtoList);
                        //创建一个变量状态标记
                        boolean varStatusFlag = false;
                        //遍历变量
                        for (IotsVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                            //创建 变量dto
                            VarizableDto varizableDto = new VarizableDto();
                            varizableDto.setVarizableId(iotsVariableInfoEntity.getId());
                            varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + iotsVariableInfoEntity.getId(), iotsVariableInfoEntity.getId().toString());
                            if (variable != null && !"".equals(variable)) {
                                varizableDto.setVarizableValue(variable);
                                varStatusFlag = true;
                            }
                            varizableDtoList.add(varizableDto);
                        }
                        //判断整体的状态值
                        equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                        equipmentDtoList.add(equipmentDto);
                    }
                }
                return R.ok().put("page", resultPage);
            }
        }
        return R.error("请输入分页参数");
    }

    /**
     * 用于查询ioserver的无分页的
     * @param dto
     * @return
     */
    @Override
    public R ioServerEquipmentMonitoring(QueryDto dto) {
        // 1. 判断参数
        if (dto == null){
            log.info("这里什么参数都没有, 不知道是请求的ioserver设备的哪个接口");
            return R.error("这里什么参数都没有, 不知道是请求的ioserver设备的哪个接口!");
        }
        String equipmentName = dto.getEquipmentName();
        Integer status = dto.getStatus();
        //1. 当参数都是 null 的时候查询全部
        if (StringUtils.isBlank(equipmentName) && status == null){
            //创建一个集合用来存放IotsIoserverInfoDto
            List<IoserverDto> ioserverDtos = new ArrayList<>();
            ResultPage resultPage = new ResultPage();
            resultPage.setList(ioserverDtos);
            log.info("说明这两个请求参数都是null");
            // 3.先查询ioserver和变量,并分页
            List<IotsIoserverInfoEntity> entitys = this.baseMapper.selectIoserverAndEqipment(dto);
            IoserverDto ioserverDto = null;
            for (IotsIoserverInfoEntity entity : entitys) {
                ioserverDto = new IoserverDto();
                //封装ioserverDto
                ioserverDto.setId(entity.getId());
                ioserverDto.setName(entity.getName());
                ioserverDtos.add(ioserverDto);
                //创建一个集合用来保存设备
                // 获取变量
                List<IotsEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                //创建设备dto集合
                List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                ioserverDto.setEquipmentDtoList(equipmentDtoList);
                // 遍历
                for (IotsEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                    // 创建设备dto
                    EquipmentDto equipmentDto = new EquipmentDto();
                    equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                    equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                    //根据设备ID 查询所有绑定的变量信息
                    List<IotsVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectVariableByEquiId(iotsEquipmentInfoEntity.getId());
                    //创建封装变量dto的集合
                    List<VarizableDto> varizableDtoList = new ArrayList<>();
                    equipmentDto.setVarizableDtos(varizableDtoList);
                    //创建一个变量状态标记
                    boolean varStatusFlag = false;
                    //遍历变量
                    for (IotsVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                        //创建 变量dto
                        VarizableDto varizableDto = new VarizableDto();
                        varizableDto.setVarizableId(iotsVariableInfoEntity.getId());
                        varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + iotsVariableInfoEntity.getId(), iotsVariableInfoEntity.getId().toString());
                        if (variable != null && !"".equals(variable)) {
                            varizableDto.setVarizableValue(variable);
                            varStatusFlag = true;
                        }
                        varizableDtoList.add(varizableDto);
                    }
                    //判断整体的状态值
                    equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                    equipmentDtoList.add(equipmentDto);
                }
            }
            List<IoserverDto> list = resultPage.getList();
            List<IoserverDto> newList = new ArrayList<>();
            for(IoserverDto dtos : list){
                if (dtos.getEquipmentDtoList() != null && dtos.getEquipmentDtoList().size() > 0){
                    List<EquipmentDto> equipmentDtoList = dtos.getEquipmentDtoList();
                    boolean temp = false;
                    for(EquipmentDto equipmentDto : dtos.getEquipmentDtoList()){
                        if (equipmentDto.getVarizableDtos() != null && equipmentDto.getVarizableDtos().size() > 0){
                            temp = true;
                        } else {
                            temp = false;
                        }
                    }
                    if (temp){
                        newList.add(dtos);
                    }
                }
            }
            resultPage.setList(newList);
            return R.ok().put("page", resultPage);
        }

        //2. 当设备名字是null 状态不是null
        if (StringUtils.isBlank(equipmentName) && status != null){
            //创建一个集合用来存放IotsIoserverInfoDto
            List<IoserverDto> ioserverDtos = new ArrayList<>();
            ResultPage resultPage = new ResultPage();
            resultPage.setList(ioserverDtos);
            log.info("当设备名字是null 状态不是null");
            // 3.先查询ioserver和变量,并分页
            List<IotsIoserverInfoEntity> entitys = this.baseMapper.selectIoserverAndEqipment(dto);
            IoserverDto ioserverDto = null;
            for (IotsIoserverInfoEntity entity : entitys) {
                ioserverDto = new IoserverDto();
                //封装ioserverDto
                ioserverDto.setId(entity.getId());
                ioserverDto.setName(entity.getName());
                ioserverDtos.add(ioserverDto);
                //创建一个集合用来保存设备
                // 获取变量
                List<IotsEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                //创建设备dto集合
                List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                ioserverDto.setEquipmentDtoList(equipmentDtoList);
                // 遍历
                for (IotsEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                    // 创建设备dto
                    EquipmentDto equipmentDto = new EquipmentDto();
                    equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                    equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                    //根据设备ID 查询所有绑定的变量信息
                    List<IotsVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectVariableByEquiId(iotsEquipmentInfoEntity.getId());
                    //创建封装变量dto的集合
                    List<VarizableDto> varizableDtoList = new ArrayList<>();
                    equipmentDto.setVarizableDtos(varizableDtoList);
                    //创建一个变量状态标记
                    boolean varStatusFlag = false;
                    //遍历变量
                    for (IotsVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                        //创建 变量dto
                        VarizableDto varizableDto = new VarizableDto();
                        varizableDto.setVarizableId(iotsVariableInfoEntity.getId());
                        varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + iotsVariableInfoEntity.getId(), iotsVariableInfoEntity.getId().toString());
                        if (variable != null && !"".equals(variable)) {
                            varizableDto.setVarizableValue(variable);
                            varStatusFlag = true;
                        }
                        varizableDtoList.add(varizableDto);
                    }
                    equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                    if (status == 0){
                        if (equipmentDto.getStatus() == 0){
                            equipmentDtoList.add(equipmentDto);
                        }
                    }
                    if (status == 1){
                        if (equipmentDto.getStatus() == 1){
                            equipmentDtoList.add(equipmentDto);
                        }
                    }
                }
            }
            List<IoserverDto> list = resultPage.getList();
            List<IoserverDto> newList = new ArrayList<>();
            for(IoserverDto dtos : list){
                if (dtos.getEquipmentDtoList() != null && dtos.getEquipmentDtoList().size() > 0){
                    List<EquipmentDto> equipmentDtoList = dtos.getEquipmentDtoList();
                    for(EquipmentDto equipmentDto : dtos.getEquipmentDtoList()){
                        if (equipmentDto.getVarizableDtos() != null && equipmentDto.getVarizableDtos().size() > 0){
                            newList.add(dtos);
                        }
                    }
                }
            }
            resultPage.setList(newList);
            return R.ok().put("page", resultPage);
        }

        //3. 当设备名字不是null 状态是null
        if (!StringUtils.isBlank(equipmentName) && status == null){
            //创建一个集合用来存放IotsIoserverInfoDto
            List<IoserverDto> ioserverDtos = new ArrayList<>();
            ResultPage resultPage = new ResultPage();
            resultPage.setList(ioserverDtos);
            log.info("当设备名字不是null 状态是null");
            // 3.先查询ioserver和变量,并分页
            List<IotsIoserverInfoEntity> entitys = this.baseMapper.selectIoserverAndEqipmentByEqipmentName(dto);
            IoserverDto ioserverDto = null;
            for (IotsIoserverInfoEntity entity : entitys) {
                ioserverDto = new IoserverDto();
                //封装ioserverDto
                ioserverDto.setId(entity.getId());
                ioserverDto.setName(entity.getName());
                ioserverDtos.add(ioserverDto);
                //创建一个集合用来保存设备
                // 获取变量
                List<IotsEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                //创建设备dto集合
                List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                ioserverDto.setEquipmentDtoList(equipmentDtoList);
                // 遍历
                for (IotsEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                    // 创建设备dto
                    EquipmentDto equipmentDto = new EquipmentDto();
                    equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                    equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                    //根据设备ID 查询所有绑定的变量信息
                    List<IotsVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectVariableByEquiId(iotsEquipmentInfoEntity.getId());
                    //创建封装变量dto的集合
                    List<VarizableDto> varizableDtoList = new ArrayList<>();
                    equipmentDto.setVarizableDtos(varizableDtoList);
                    //创建一个变量状态标记
                    boolean varStatusFlag = false;
                    //遍历变量
                    for (IotsVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                        //创建 变量dto
                        VarizableDto varizableDto = new VarizableDto();
                        varizableDto.setVarizableId(iotsVariableInfoEntity.getId());
                        varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + iotsVariableInfoEntity.getId(), iotsVariableInfoEntity.getId().toString());
                        if (variable != null && !"".equals(variable)) {
                            varizableDto.setVarizableValue(variable);
                            varStatusFlag = true;
                        }
                        varizableDtoList.add(varizableDto);
                    }
                    //判断整体的状态值
                    equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                    equipmentDtoList.add(equipmentDto);
                }
            }
            List<IoserverDto> list = resultPage.getList();
            List<IoserverDto> newList = new ArrayList<>();
            for(IoserverDto dtos : list){
                if (dtos.getEquipmentDtoList() != null && dtos.getEquipmentDtoList().size() > 0){
                    List<EquipmentDto> equipmentDtoList = dtos.getEquipmentDtoList();
                    boolean temp = false;
                    for(EquipmentDto equipmentDto : dtos.getEquipmentDtoList()){
                        if (equipmentDto.getVarizableDtos() != null && equipmentDto.getVarizableDtos().size() > 0){
                            temp = true;
                        } else {
                            temp = false;
                        }
                    }
                    if (temp){
                        newList.add(dtos);
                    }
                }
            }
            resultPage.setList(newList);
            return R.ok().put("page", resultPage);
        }


        //4. 当设备名字不是null 状态不是null
        if (!StringUtils.isBlank(equipmentName) && status != null){
            //创建一个集合用来存放IotsIoserverInfoDto
            List<IoserverDto> ioserverDtos = new ArrayList<>();
            ResultPage resultPage = new ResultPage();
            resultPage.setList(ioserverDtos);
            log.info("当设备名字不是null 状态不是null");
            // 3.先查询ioserver和变量,并分页
            List<IotsIoserverInfoEntity> entitys = this.baseMapper.selectIoserverAndEqipmentByEqipmentName(dto);
            IoserverDto ioserverDto = null;
            for (IotsIoserverInfoEntity entity : entitys) {
                ioserverDto = new IoserverDto();
                //封装ioserverDto
                ioserverDto.setId(entity.getId());
                ioserverDto.setName(entity.getName());
                ioserverDtos.add(ioserverDto);
                //创建一个集合用来保存设备
                // 获取变量
                List<IotsEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                //创建设备dto集合
                List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                ioserverDto.setEquipmentDtoList(equipmentDtoList);
                // 遍历
                for (IotsEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                    // 创建设备dto
                    EquipmentDto equipmentDto = new EquipmentDto();
                    equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                    equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                    //根据设备ID 查询所有绑定的变量信息
                    List<IotsVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectVariableByEquiId(iotsEquipmentInfoEntity.getId());
                    //创建封装变量dto的集合
                    List<VarizableDto> varizableDtoList = new ArrayList<>();
                    equipmentDto.setVarizableDtos(varizableDtoList);
                    //创建一个变量状态标记
                    boolean varStatusFlag = false;
                    //遍历变量
                    for (IotsVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                        //创建 变量dto
                        VarizableDto varizableDto = new VarizableDto();
                        varizableDto.setVarizableId(iotsVariableInfoEntity.getId());
                        varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + iotsVariableInfoEntity.getId(), iotsVariableInfoEntity.getId().toString());
                        if (variable != null && !"".equals(variable)) {
                            varizableDto.setVarizableValue(variable);
                            varStatusFlag = true;
                        }
                        varizableDtoList.add(varizableDto);
                    }
                    //判断整体的状态值
                    equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                    if (status == 0){
                        if (equipmentDto.getStatus() == 0){
                            equipmentDtoList.add(equipmentDto);
                        }
                    }
                    if (status == 1){
                        if (equipmentDto.getStatus() == 1){
                            equipmentDtoList.add(equipmentDto);
                        }
                    }
                }
            }
            List<IoserverDto> list = resultPage.getList();
            List<IoserverDto> newList = new ArrayList<>();
            for(IoserverDto dtos : list){
                if (dtos.getEquipmentDtoList() != null && dtos.getEquipmentDtoList().size() > 0){
                    List<EquipmentDto> equipmentDtoList = dtos.getEquipmentDtoList();
                    boolean temp = false;
                    for(EquipmentDto equipmentDto : dtos.getEquipmentDtoList()){
                        if (equipmentDto.getVarizableDtos() != null && equipmentDto.getVarizableDtos().size() > 0){
                            temp = true;
                        } else {
                            temp = false;
                        }
                    }
                    if (temp){
                        newList.add(dtos);
                    }
                }
            }
            resultPage.setList(newList);
            return R.ok().put("page", resultPage);
        }
        return R.error("请输入请求参数");
    }

    @Override
    public R queryAllEquipmentName() {
        List<String> strings = this.baseMapper.queryAllEquipmentName();
        return R.ok().put("names",strings);
    }

    /**
     * 查询模型的的设备和变量, 并分页
     * @param dto
     * @return
     */
    @Override
    public R modelEquipmentMonitoringNoPage(QueryDto dto) {
        // 1. 先判断有没有分页参数, 没有分页参数查询的就是ioserver设备监控的数据, 反之是服务监控中的ioserver设备监控
        if (dto == null){
            log.info("这里什么参数都没有, 不知道是请求的ioserver设备的哪个接口");
            return R.error("请输入分页参数!");
        }
        // 2.说明参数封装实体类中的参数不为空，判断有没有分页参数,有的话是ioserver设备监控的数据, 反之是服务监控中的ioserver设备监控
        if (dto.getCurrentPage() != null && dto.getPageSize() != null){
            log.info("开始查询ioserver设备监控的数据");
            // 获取其他参数 TODO 这里的请求暂时不考虑 变量名字查询条件
            String ioserverName = dto.getIoserverName();
            log.info("ioserverName是: " + ioserverName);
            String equipmentName = dto.getEquipmentName();
            log.info("equipmentName是: " + equipmentName);
            if (!StringUtils.isBlank(ioserverName) && !StringUtils.isBlank(equipmentName)){
                //创建一个集合用来存放IotsIoserverInfoDto
                List<IoserverDto> ioserverDtos = new ArrayList<>();
                OurPage page = new OurPage();
                page.setCurrentPage(dto.getCurrentPage());
                page.setPageSize(dto.getPageSize());
                ResultPage resultPage = new ResultPage();
                resultPage.setPage(page);
                resultPage.setList(ioserverDtos);
                log.info("说明这两个请求参数都不是null");
                // 3.先查询ioserver和变量,并分页
                dto.setCurrentPage(dto.getCurrentPage() - 1);
                List<ModelGroupInfoEntity> entitys = this.baseMapper.selectModelAndEqipmentByPageAndName(dto);
                // 查询 总数
                Integer count = this.baseMapper.selectCountModelAndEqipmentByPageAndName(dto);
                // 总页数  (totalCount + pageSize - 1) / pageSize
                Integer totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
                page.setTotalSize(count);
                page.setTotalPage(totalPage);
                IoserverDto ioserverDto = null;
                for (ModelGroupInfoEntity entity : entitys) {
                    ioserverDto = new IoserverDto();
                    //封装ioserverDto
                    ioserverDto.setId(entity.getId());
                    ioserverDto.setName(entity.getName());
                    ioserverDtos.add(ioserverDto);
                    //创建一个集合用来保存设备
                    // 获取变量
                    List<ModelEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                    //创建设备dto集合
                    List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                    ioserverDto.setEquipmentDtoList(equipmentDtoList);
                    // 遍历
                    for (ModelEquipmentInfoEntity modelEquipmentInfoEntity: iotsEquipmentList) {
                        // 创建设备dto
                        EquipmentDto equipmentDto = new EquipmentDto();
                        equipmentDto.setEquipmentId(modelEquipmentInfoEntity.getId());
                        equipmentDto.setEquipmentName(modelEquipmentInfoEntity.getName());
                        //根据设备ID 查询所有绑定的变量信息
                        List<ModelVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectModelVariableByEquiId(modelEquipmentInfoEntity.getId());
                        //创建封装变量dto的集合
                        List<VarizableDto> varizableDtoList = new ArrayList<>();
                        equipmentDto.setVarizableDtos(varizableDtoList);
                        //创建一个变量状态标记
                        boolean varStatusFlag = false;
                        //遍历变量
                        for (ModelVariableInfoEntity modelVariableInfoEntity : IotsVariableInfoEntitys) {
                            //创建 变量dto
                            VarizableDto varizableDto = new VarizableDto();
                            varizableDto.setVarizableId(modelVariableInfoEntity.getDyncId());
                            varizableDto.setVarizableName(modelVariableInfoEntity.getName());
                            // TODO 拿到model的变量的ID 查询绑定的IOSERVER下的变量的ID
                            Long varId = this.baseMapper.selectBindVarIdByMidelVarId(modelVariableInfoEntity.getDyncId());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + varId, varId.toString());
                            if (variable != null && !"".equals(variable)){
                                varizableDto.setVarizableValue(variable);
                                varStatusFlag = true;
                            }
                            varizableDtoList.add(varizableDto);
                        }
                        //判断整体的状态值
                        equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                        equipmentDtoList.add(equipmentDto);
                    }
                }
                List<IoserverDto> list = resultPage.getList();
                List<IoserverDto> newList = new ArrayList<>();
                for(IoserverDto dtos : list){
                    List<String> names = new ArrayList<>();
                    List<String> newNames = this.get(names, dtos.getId());
                    String nameLine = this.getNameLine(newNames);
                    dtos.setName(nameLine);
                    newList.add(dtos);
                }
                resultPage.setList(newList);
                return R.ok().put("page", resultPage);
            }

            //3. 这里执行没有查询参数的
            if (StringUtils.isBlank(ioserverName) && StringUtils.isBlank(equipmentName)) {
                //创建一个集合用来存放IotsIoserverInfoDto
                List<IoserverDto> ioserverDtos = new ArrayList<>();
                OurPage page = new OurPage();
                page.setCurrentPage(dto.getCurrentPage());
                page.setPageSize(dto.getPageSize());
                ResultPage resultPage = new ResultPage();
                resultPage.setPage(page);
                resultPage.setList(ioserverDtos);
                log.info("说明这两个请求参数都是null");
                // 3.先查询ioserver和变量,并分页
                dto.setCurrentPage(dto.getCurrentPage() - 1);
                List<ModelGroupInfoEntity> entitys = this.baseMapper.selectModelAndEqipment(dto);
                // 查询 总数
                Integer count = this.baseMapper.selectCountModelAndEqipment(dto);
                // 总页数  (totalCount + pageSize - 1) / pageSize
                Integer totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
                page.setTotalSize(count);
                page.setTotalPage(totalPage);
                IoserverDto ioserverDto = null;
                for (ModelGroupInfoEntity entity : entitys) {
                    ioserverDto = new IoserverDto();
                    //封装ioserverDto
                    ioserverDto.setId(entity.getId());
                    ioserverDto.setName(entity.getName());
                    ioserverDtos.add(ioserverDto);
                    //创建一个集合用来保存设备
                    // 获取变量
                    List<ModelEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                    //创建设备dto集合
                    List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                    ioserverDto.setEquipmentDtoList(equipmentDtoList);
                    // 遍历
                    for (ModelEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                        // 创建设备dto
                        EquipmentDto equipmentDto = new EquipmentDto();
                        equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                        equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                        //根据设备ID 查询所有绑定的变量信息
                        List<ModelVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectModelVariableByEquiId(iotsEquipmentInfoEntity.getId());
                        //创建封装变量dto的集合
                        List<VarizableDto> varizableDtoList = new ArrayList<>();
                        equipmentDto.setVarizableDtos(varizableDtoList);
                        //创建一个变量状态标记
                        boolean varStatusFlag = false;
                        //遍历变量
                        for (ModelVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                            //创建 变量dto
                            VarizableDto varizableDto = new VarizableDto();
                            varizableDto.setVarizableId(iotsVariableInfoEntity.getDyncId());
                            varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                            Long varId = this.baseMapper.selectBindVarIdByMidelVarId(iotsVariableInfoEntity.getDyncId());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + varId, varId.toString());
                            if (variable != null && !"".equals(variable)) {
                                varizableDto.setVarizableValue(variable);
                                varStatusFlag = true;
                            }
                            varizableDtoList.add(varizableDto);
                        }
                        //判断整体的状态值
                        equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                        equipmentDtoList.add(equipmentDto);
                    }
                }
                List<IoserverDto> list = resultPage.getList();
                List<IoserverDto> newList = new ArrayList<>();
                for(IoserverDto dtos : list){
                    List<String> names = new ArrayList<>();
                    List<String> newNames = this.get(names, dtos.getId());
                    String nameLine = this.getNameLine(newNames);
                    dtos.setName(nameLine);
                    newList.add(dtos);
                }
                resultPage.setList(newList);
                return R.ok().put("page", resultPage);
            }
            //4. model 分组有参数 设备没有参数
            if (!StringUtils.isBlank(ioserverName) && StringUtils.isBlank(equipmentName)) {
                //创建一个集合用来存放IotsIoserverInfoDto
                List<IoserverDto> ioserverDtos = new ArrayList<>();
                OurPage page = new OurPage();
                page.setCurrentPage(dto.getCurrentPage());
                page.setPageSize(dto.getPageSize());
                ResultPage resultPage = new ResultPage();
                resultPage.setPage(page);
                resultPage.setList(ioserverDtos);
                log.info(" model 分组有参数 设备没有参数");
                // 3.先查询ioserver和变量,并分页
                dto.setCurrentPage(dto.getCurrentPage() - 1);
                List<ModelGroupInfoEntity> entitys = this.baseMapper.selectModelAndEqipmentByIoserverName(dto);
                // 查询 总数
                Integer count = this.baseMapper.selectCountModelAndEqipmentByIoserverName(dto);
                // 总页数  (totalCount + pageSize - 1) / pageSize
                Integer totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
                page.setTotalSize(count);
                page.setTotalPage(totalPage);
                IoserverDto ioserverDto = null;
                for (ModelGroupInfoEntity entity : entitys) {
                    ioserverDto = new IoserverDto();
                    //封装ioserverDto
                    ioserverDto.setId(entity.getId());
                    ioserverDto.setName(entity.getName());
                    ioserverDtos.add(ioserverDto);
                    //创建一个集合用来保存设备
                    // 获取变量
                    List<ModelEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                    //创建设备dto集合
                    List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                    ioserverDto.setEquipmentDtoList(equipmentDtoList);
                    // 遍历
                    for (ModelEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                        // 创建设备dto
                        EquipmentDto equipmentDto = new EquipmentDto();
                        equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                        equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                        //根据设备ID 查询所有绑定的变量信息
                        List<ModelVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectModelVariableByEquiId(iotsEquipmentInfoEntity.getId());
                        //创建封装变量dto的集合
                        List<VarizableDto> varizableDtoList = new ArrayList<>();
                        equipmentDto.setVarizableDtos(varizableDtoList);
                        //创建一个变量状态标记
                        boolean varStatusFlag = false;
                        //遍历变量
                        for (ModelVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                            //创建 变量dto
                            VarizableDto varizableDto = new VarizableDto();
                            varizableDto.setVarizableId(iotsVariableInfoEntity.getDyncId());
                            varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            Long varId = this.baseMapper.selectBindVarIdByMidelVarId(iotsVariableInfoEntity.getDyncId());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + varId, varId.toString());
                            if (variable != null && !"".equals(variable)) {
                                varizableDto.setVarizableValue(variable);
                                varStatusFlag = true;
                            }
                            varizableDtoList.add(varizableDto);
                        }
                        //判断整体的状态值
                        equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                        equipmentDtoList.add(equipmentDto);
                    }
                }
                List<IoserverDto> list = resultPage.getList();
                List<IoserverDto> newList = new ArrayList<>();
                for(IoserverDto dtos : list){
                    List<String> names = new ArrayList<>();
                    List<String> newNames = this.get(names, dtos.getId());
                    String nameLine = this.getNameLine(newNames);
                    dtos.setName(nameLine);
                    newList.add(dtos);
                }
                resultPage.setList(newList);
                return R.ok().put("page", resultPage);
            }

            //5. model 没有参数 设备有参数
            if (StringUtils.isBlank(ioserverName) && !StringUtils.isBlank(equipmentName)) {
                //创建一个集合用来存放IotsIoserverInfoDto
                List<IoserverDto> ioserverDtos = new ArrayList<>();
                OurPage page = new OurPage();
                page.setCurrentPage(dto.getCurrentPage());
                page.setPageSize(dto.getPageSize());
                ResultPage resultPage = new ResultPage();
                resultPage.setPage(page);
                resultPage.setList(ioserverDtos);
                log.info("model 没有参数 设备有参数");
                // 3.先查询ioserver和变量,并分页
                dto.setCurrentPage(dto.getCurrentPage() - 1);
                List<ModelGroupInfoEntity> entitys = this.baseMapper.selectModelAndEqipmentByEqipmentName(dto);
                // 查询 总数
                Integer count = this.baseMapper.selectCountModelAndEqipmentByEqipmentName(dto);
                // 总页数  (totalCount + pageSize - 1) / pageSize
                Integer totalPage = (count + page.getPageSize() - 1) / page.getPageSize();
                page.setTotalSize(count);
                page.setTotalPage(totalPage);
                IoserverDto ioserverDto = null;
                for (ModelGroupInfoEntity entity : entitys) {
                    ioserverDto = new IoserverDto();
                    //封装ioserverDto
                    ioserverDto.setId(entity.getId());
                    ioserverDto.setName(entity.getName());
                    ioserverDtos.add(ioserverDto);
                    //创建一个集合用来保存设备
                    // 获取变量
                    List<ModelEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                    //创建设备dto集合
                    List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                    ioserverDto.setEquipmentDtoList(equipmentDtoList);
                    // 遍历
                    for (ModelEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                        // 创建设备dto
                        EquipmentDto equipmentDto = new EquipmentDto();
                        equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                        equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                        //根据设备ID 查询所有绑定的变量信息
                        List<ModelVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectModelVariableByEquiId(iotsEquipmentInfoEntity.getId());
                        //创建封装变量dto的集合
                        List<VarizableDto> varizableDtoList = new ArrayList<>();
                        equipmentDto.setVarizableDtos(varizableDtoList);
                        //创建一个变量状态标记
                        boolean varStatusFlag = false;
                        //遍历变量
                        for (ModelVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                            //创建 变量dto
                            VarizableDto varizableDto = new VarizableDto();
                            varizableDto.setVarizableId(iotsVariableInfoEntity.getDyncId());
                            varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            Long varId = this.baseMapper.selectBindVarIdByMidelVarId(iotsVariableInfoEntity.getDyncId());
                            //查询 redis 根据 key  variable 变量ID 变量值
                            String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + varId, varId.toString());
                            if (variable != null && !"".equals(variable)) {
                                varizableDto.setVarizableValue(variable);
                                varStatusFlag = true;
                            }
                            varizableDtoList.add(varizableDto);
                        }
                        //判断整体的状态值
                        equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                        equipmentDtoList.add(equipmentDto);
                    }
                }
                List<IoserverDto> list = resultPage.getList();
                List<IoserverDto> newList = new ArrayList<>();
                for(IoserverDto dtos : list){
                    List<String> names = new ArrayList<>();
                    List<String> newNames = this.get(names, dtos.getId());
                    String nameLine = this.getNameLine(newNames);
                    dtos.setName(nameLine);
                    newList.add(dtos);
                }
                resultPage.setList(newList);
                return R.ok().put("page", resultPage);
            }
        }
        return R.error("请输入分页参数!");
    }

    @Override
    public R modelEquipmentMonitoring(QueryDto dto) {
        // 1. 先判断有没有分页参数, 没有分页参数查询的就是ioserver设备监控的数据, 反之是服务监控中的ioserver设备监控
        if (dto == null){
            log.info("这里什么参数都没有, 不知道是请求的ioserver设备的哪个接口");
            return R.error("这里什么参数都没有, 不知道是请求的ioserver设备的哪个接口!");
        }
        String equipmentName = dto.getEquipmentName();
        Integer status = dto.getStatus();
        //1. 当参数都是 null 的时候查询全部
        if (StringUtils.isBlank(equipmentName) && status == null){
            //创建一个集合用来存放IotsIoserverInfoDto
            List<IoserverDto> ioserverDtos = new ArrayList<>();
            ResultPage resultPage = new ResultPage();
            resultPage.setList(ioserverDtos);
            log.info("说明这两个请求参数都是null");
            // 3.先查询ioserver和变量,并分页
            List<ModelGroupInfoEntity> entitys = this.baseMapper.selectModelAndEqipment(dto);
            IoserverDto ioserverDto = null;
            for (ModelGroupInfoEntity entity : entitys) {
                ioserverDto = new IoserverDto();
                //封装ioserverDto
                ioserverDto.setId(entity.getId());
                ioserverDto.setName(entity.getName());
                ioserverDtos.add(ioserverDto);
                //创建一个集合用来保存设备
                // 获取变量
                List<ModelEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                //创建设备dto集合
                List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                ioserverDto.setEquipmentDtoList(equipmentDtoList);
                // 遍历
                for (ModelEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                    // 创建设备dto
                    EquipmentDto equipmentDto = new EquipmentDto();
                    equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                    equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                    //根据设备ID 查询所有绑定的变量信息
                    List<ModelVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectModelVariableByEquiId(iotsEquipmentInfoEntity.getId());
                    //创建封装变量dto的集合
                    List<VarizableDto> varizableDtoList = new ArrayList<>();
                    equipmentDto.setVarizableDtos(varizableDtoList);
                    //创建一个变量状态标记
                    boolean varStatusFlag = false;
                    //遍历变量
                    for (ModelVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                        //创建 变量dto
                        VarizableDto varizableDto = new VarizableDto();
                        varizableDto.setVarizableId(iotsVariableInfoEntity.getDyncId());
                        varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        Long varId = this.baseMapper.selectBindVarIdByMidelVarId(iotsVariableInfoEntity.getDyncId());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + varId, varId.toString());
                        if (variable != null && !"".equals(variable)) {
                            varizableDto.setVarizableValue(variable);
                            varStatusFlag = true;
                        }
                        varizableDtoList.add(varizableDto);
                    }
                    //判断整体的状态值
                    equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                    equipmentDtoList.add(equipmentDto);
                }
            }
            List<IoserverDto> list = resultPage.getList();
            List<IoserverDto> newList = new ArrayList<>();
            for(IoserverDto dtos : list){
                List<String> names = new ArrayList<>();
                List<String> newNames = this.get(names, dtos.getId());
                String nameLine = this.getNameLine(newNames);
                dtos.setName(nameLine);
                if (dtos.getEquipmentDtoList() != null && dtos.getEquipmentDtoList().size() > 0){
                    List<EquipmentDto> equipmentDtoList = dtos.getEquipmentDtoList();
                    boolean temp = false;
                    for(EquipmentDto equipmentDto : dtos.getEquipmentDtoList()){
                        if (equipmentDto.getVarizableDtos() != null && equipmentDto.getVarizableDtos().size() > 0){
                            temp = true;
                        } else {
                            temp = false;
                        }
                    }
                    if (temp){
                        newList.add(dtos);
                    }
                }
            }
            resultPage.setList(newList);
            return R.ok().put("page", resultPage);
        }
        //2. 当设备名字是null 状态不是null
        if (StringUtils.isBlank(equipmentName) && status != null){
            //创建一个集合用来存放IotsIoserverInfoDto
            List<IoserverDto> ioserverDtos = new ArrayList<>();
            ResultPage resultPage = new ResultPage();
            resultPage.setList(ioserverDtos);
            log.info("当设备名字是null 状态不是null");
            // 3.先查询ioserver和变量,并分页
            List<ModelGroupInfoEntity> entitys = this.baseMapper.selectModelAndEqipment(dto);
            IoserverDto ioserverDto = null;
            for (ModelGroupInfoEntity entity : entitys) {
                ioserverDto = new IoserverDto();
                //封装ioserverDto
                ioserverDto.setId(entity.getId());
                ioserverDto.setName(entity.getName());
                ioserverDtos.add(ioserverDto);
                //创建一个集合用来保存设备
                // 获取变量
                List<ModelEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                //创建设备dto集合
                List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                ioserverDto.setEquipmentDtoList(equipmentDtoList);
                // 遍历
                for (ModelEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                    // 创建设备dto
                    EquipmentDto equipmentDto = new EquipmentDto();
                    equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                    equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                    //根据设备ID 查询所有绑定的变量信息
                    List<ModelVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectModelVariableByEquiId(iotsEquipmentInfoEntity.getId());
                    //创建封装变量dto的集合
                    List<VarizableDto> varizableDtoList = new ArrayList<>();
                    equipmentDto.setVarizableDtos(varizableDtoList);
                    //创建一个变量状态标记
                    boolean varStatusFlag = false;
                    //遍历变量
                    for (ModelVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                        //创建 变量dto
                        VarizableDto varizableDto = new VarizableDto();
                        varizableDto.setVarizableId(iotsVariableInfoEntity.getDyncId());
                        varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        Long varId = this.baseMapper.selectBindVarIdByMidelVarId(iotsVariableInfoEntity.getDyncId());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + varId, varId.toString());
                        if (variable != null && !"".equals(variable)) {
                            varizableDto.setVarizableValue(variable);
                            varStatusFlag = true;
                        }
                        varizableDtoList.add(varizableDto);
                    }
                    equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                    if (status == 0){
                        if (equipmentDto.getStatus() == 0){
                            equipmentDtoList.add(equipmentDto);
                        }
                    }
                    if (status == 1){
                        if (equipmentDto.getStatus() == 1){
                            equipmentDtoList.add(equipmentDto);
                        }
                    }
                }
            }
            List<IoserverDto> list = resultPage.getList();
            List<IoserverDto> newList = new ArrayList<>();
            for(IoserverDto dtos : list){
                List<String> names = new ArrayList<>();
                List<String> newNames = this.get(names, dtos.getId());
                String nameLine = this.getNameLine(newNames);
                dtos.setName(nameLine);
                if (dtos.getEquipmentDtoList() != null && dtos.getEquipmentDtoList().size() > 0){
                    List<EquipmentDto> equipmentDtoList = dtos.getEquipmentDtoList();
                    boolean temp = false;
                    for(EquipmentDto equipmentDto : dtos.getEquipmentDtoList()){
                        if (equipmentDto.getVarizableDtos() != null && equipmentDto.getVarizableDtos().size() > 0){
                            temp = true;
                        } else {
                            temp = false;
                        }
                    }
                    if (temp){
                        newList.add(dtos);
                    }
                }
            }
            resultPage.setList(newList);
            return R.ok().put("page", resultPage);
        }

        //3. 当设备名字不是null 状态是null
        if (!StringUtils.isBlank(equipmentName) && status == null){
            //创建一个集合用来存放IotsIoserverInfoDto
            List<IoserverDto> ioserverDtos = new ArrayList<>();
            ResultPage resultPage = new ResultPage();
            resultPage.setList(ioserverDtos);
            log.info("当设备名字不是null 状态是null");
            // 3.先查询ioserver和变量,并分页
            List<ModelGroupInfoEntity> entitys = this.baseMapper.selectModelAndEqipmentByEqipmentName(dto);
            IoserverDto ioserverDto = null;
            for (ModelGroupInfoEntity entity : entitys) {
                ioserverDto = new IoserverDto();
                //封装ioserverDto
                ioserverDto.setId(entity.getId());
                ioserverDto.setName(entity.getName());
                ioserverDtos.add(ioserverDto);
                //创建一个集合用来保存设备
                // 获取变量
                List<ModelEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                //创建设备dto集合
                List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                ioserverDto.setEquipmentDtoList(equipmentDtoList);
                // 遍历
                for (ModelEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                    // 创建设备dto
                    EquipmentDto equipmentDto = new EquipmentDto();
                    equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                    equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                    //根据设备ID 查询所有绑定的变量信息
                    List<ModelVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectModelVariableByEquiId(iotsEquipmentInfoEntity.getId());
                    //创建封装变量dto的集合
                    List<VarizableDto> varizableDtoList = new ArrayList<>();
                    equipmentDto.setVarizableDtos(varizableDtoList);
                    //创建一个变量状态标记
                    boolean varStatusFlag = false;
                    //遍历变量
                    for (ModelVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                        //创建 变量dto
                        VarizableDto varizableDto = new VarizableDto();
                        varizableDto.setVarizableId(iotsVariableInfoEntity.getDyncId());
                        varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        Long varId = this.baseMapper.selectBindVarIdByMidelVarId(iotsVariableInfoEntity.getDyncId());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + varId, varId.toString());
                        if (variable != null && !"".equals(variable)) {
                            varizableDto.setVarizableValue(variable);
                            varStatusFlag = true;
                        }
                        varizableDtoList.add(varizableDto);
                    }
                    //判断整体的状态值
                    equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                    equipmentDtoList.add(equipmentDto);
                }
            }
            List<IoserverDto> list = resultPage.getList();
            List<IoserverDto> newList = new ArrayList<>();
            for(IoserverDto dtos : list){
                List<String> names = new ArrayList<>();
                List<String> newNames = this.get(names, dtos.getId());
                String nameLine = this.getNameLine(newNames);
                dtos.setName(nameLine);
                if (dtos.getEquipmentDtoList() != null && dtos.getEquipmentDtoList().size() > 0){
                    List<EquipmentDto> equipmentDtoList = dtos.getEquipmentDtoList();
                    boolean temp = false;
                    for(EquipmentDto equipmentDto : dtos.getEquipmentDtoList()){
                        if (equipmentDto.getVarizableDtos() != null && equipmentDto.getVarizableDtos().size() > 0){
                            temp = true;
                        } else {
                            temp = false;
                        }
                    }
                    if (temp){
                        newList.add(dtos);
                    }
                }
            }
            resultPage.setList(newList);
            return R.ok().put("page", resultPage);
        }
        //4. 当设备名字不是null 状态不是null
        if (!StringUtils.isBlank(equipmentName) && status != null){
            //创建一个集合用来存放IotsIoserverInfoDto
            List<IoserverDto> ioserverDtos = new ArrayList<>();
            ResultPage resultPage = new ResultPage();
            resultPage.setList(ioserverDtos);
            log.info("当设备名字不是null 状态不是null");
            // 3.先查询ioserver和变量,并分页
            List<ModelGroupInfoEntity> entitys = this.baseMapper.selectModelAndEqipmentByEqipmentName(dto);
            IoserverDto ioserverDto = null;
            for (ModelGroupInfoEntity entity : entitys) {
                ioserverDto = new IoserverDto();
                //封装ioserverDto
                ioserverDto.setId(entity.getId());
                ioserverDto.setName(entity.getName());
                ioserverDtos.add(ioserverDto);
                //创建一个集合用来保存设备
                // 获取变量
                List<ModelEquipmentInfoEntity> iotsEquipmentList = entity.getList();
                //创建设备dto集合
                List<EquipmentDto> equipmentDtoList = new ArrayList<>();
                ioserverDto.setEquipmentDtoList(equipmentDtoList);
                // 遍历
                for (ModelEquipmentInfoEntity iotsEquipmentInfoEntity : iotsEquipmentList) {
                    // 创建设备dto
                    EquipmentDto equipmentDto = new EquipmentDto();
                    equipmentDto.setEquipmentId(iotsEquipmentInfoEntity.getId());
                    equipmentDto.setEquipmentName(iotsEquipmentInfoEntity.getName());
                    //根据设备ID 查询所有绑定的变量信息
                    List<ModelVariableInfoEntity> IotsVariableInfoEntitys = this.baseMapper.selectModelVariableByEquiId(iotsEquipmentInfoEntity.getId());
                    //创建封装变量dto的集合
                    List<VarizableDto> varizableDtoList = new ArrayList<>();
                    equipmentDto.setVarizableDtos(varizableDtoList);
                    //创建一个变量状态标记
                    boolean varStatusFlag = false;
                    //遍历变量
                    for (ModelVariableInfoEntity iotsVariableInfoEntity : IotsVariableInfoEntitys) {
                        //创建 变量dto
                        VarizableDto varizableDto = new VarizableDto();
                        varizableDto.setVarizableId(iotsVariableInfoEntity.getDyncId());
                        varizableDto.setVarizableName(iotsVariableInfoEntity.getName());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        Long varId = this.baseMapper.selectBindVarIdByMidelVarId(iotsVariableInfoEntity.getDyncId());
                        //查询 redis 根据 key  variable 变量ID 变量值
                        String variable = redisUtils.hget(IOSERVER_VARIABLE_KEY + varId, varId.toString());
                        if (variable != null && !"".equals(variable)) {
                            varizableDto.setVarizableValue(variable);
                            varStatusFlag = true;
                        }
                        varizableDtoList.add(varizableDto);
                    }
                    //判断整体的状态值
                    equipmentDto.setStatus(varStatusFlag == true ? 0 : 1);
                    if (status == 0){
                        if (equipmentDto.getStatus() == 0){
                            equipmentDtoList.add(equipmentDto);
                        }
                    }
                    if (status == 1){
                        if (equipmentDto.getStatus() == 1){
                            equipmentDtoList.add(equipmentDto);
                        }
                    }
                }
            }
            List<IoserverDto> list = resultPage.getList();
            List<IoserverDto> newList = new ArrayList<>();
            for(IoserverDto dtos : list){
                List<String> names = new ArrayList<>();
                List<String> newNames = this.get(names, dtos.getId());
                String nameLine = this.getNameLine(newNames);
                dtos.setName(nameLine);
                if (dtos.getEquipmentDtoList() != null && dtos.getEquipmentDtoList().size() > 0){
                    List<EquipmentDto> equipmentDtoList = dtos.getEquipmentDtoList();
                    boolean temp = false;
                    for(EquipmentDto equipmentDto : dtos.getEquipmentDtoList()){
                        if (equipmentDto.getVarizableDtos() != null && equipmentDto.getVarizableDtos().size() > 0){
                            temp = true;
                        } else {
                            temp = false;
                        }
                    }
                    if (temp){
                        newList.add(dtos);
                    }
                }
            }
            resultPage.setList(newList);
            return R.ok().put("page", resultPage);
        }
        return R.error("请求参数错误!");
    }

    private List<String> get(List<String> name, Long id){
        List<ChildAndParent> childAndParents = this.baseMapper.selectPNamesByChildId(id);
        if (childAndParents != null && childAndParents.size() > 0){
            // 有数据继续查询父节点
            name.add(childAndParents.get(0).getName());
            this.get(name,childAndParents.get(0).getId());
        }
        return name;
    }

    public String getNameLine(List<String> names){
        Collections.reverse(names);
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < names .size(); i++){
            if (i < names.size() - 1){
                sb.append(names.get(i) + "/");
            } else {
                sb.append(names.get(i));
            }

        }
        return sb.toString();
    }


    @Override
    public R queryModelAllEquipmentName() {
        return R.ok().put("names",this.baseMapper.queryModelAllEquipmentName());
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        //String nameLine = getNameLine(list);
        //System.out.println(nameLine);
    }
}
