package io.renren.modules.iots.service.impl;

import io.renren.modules.iots.entity.status.AnomalyInfo;
import io.renren.modules.iots.entity.status.ServerStatus;
import io.renren.modules.iots.entity.status.StatusInfo;
import io.renren.modules.iots.service.FirstPageServerI;
import io.renren.modules.iots.service.IotsIotserverService;
import io.renren.modules.iots.utils.sys.SystemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 首页 业务实现类
 */
@Component("firstPageServer")
public class FirstPageServerImpl implements FirstPageServerI {

    @Autowired
    private IotsIotserverService server;

    /**
     * 最近一次上报时间判断周期 毫秒 ms
     */
    @Value("${recently.reported.time.judgment.cycle}")
    private String RECENTLY_REPORTED_TIME_JUDGMENT_CYCLE;

    @Override
    public Object getAllServer() {
        Map<String, Map<String, StatusInfo>> allServer = SystemCache.getAllServer();
        List<Object> list = new ArrayList<>();
        if (allServer != null && allServer.size() > 0){
            ServerStatus serverStatus = null;
            for (Map.Entry<String, Map<String, StatusInfo>> entry : allServer.entrySet()){
                serverStatus = new ServerStatus();
                String name = entry.getKey();
                Map<String, StatusInfo> value = entry.getValue();
                // 异常数量
                int statusNum = 0;
                // 设置总数  名称
                serverStatus.setName(name);
                serverStatus.setTotalNum(value.size());
                //最外层时间写当前时间
                serverStatus.setLatestReportingTime(new Date());
                if (value != null && value.size() > 0){
                    for(Map.Entry<String, StatusInfo> statusInfo : value.entrySet()){
                        // 判断上报时间
                        StatusInfo sinfo = statusInfo.getValue();
                        String mac = statusInfo.getKey();
                        String serverName = sinfo.getServerName();
                        //异常
                        if (System.currentTimeMillis() - sinfo.getCreatetime() >= Integer.valueOf(RECENTLY_REPORTED_TIME_JUDGMENT_CYCLE.trim()) * 6000){
                            statusNum += 1;
                            // 根据名字和mac地址 设置在线和离线 0 是在线, 1是离线
                            server.setStatus(mac,serverName,1);
                        }
                    }
                }
                //设置异常数量
                serverStatus.setAnomalyTotalNum(statusNum);
                if (statusNum == value.size()) serverStatus.setStatus(1); else serverStatus.setStatus(0);
                list.add(serverStatus);
            }
        }
        return list;
    }

    /**
     * 根据类型查询服务
     * @param serverType
     * @param status
     * @param timeSort
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Object getServerByType(String serverType, Integer status, Integer timeSort, Integer currentPage, Integer pageSize) {
        Map<String, StatusInfo> serverByType = SystemCache.getServerByType(serverType);
        List<AnomalyInfo> list = new ArrayList();
        if (serverByType != null && serverByType.size() > 0){
            AnomalyInfo anomalyInfo = null;
            for(Map.Entry<String, StatusInfo> statusInfos : serverByType.entrySet()){
                anomalyInfo = new AnomalyInfo();
                // 判断上报时间
                StatusInfo sinfo = statusInfos.getValue();
                //异常
                if (System.currentTimeMillis() - sinfo.getCreatetime() >= Integer.valueOf(RECENTLY_REPORTED_TIME_JUDGMENT_CYCLE.trim()) * 6000){
                    anomalyInfo.setStatus(1);
                } else {
                    anomalyInfo.setStatus(0);
                }
                anomalyInfo.setName(sinfo.getServerName());
                Date date = new Date();
                date.setTime(sinfo.getCreatetime());
                anomalyInfo.setLatestReportingTime(date);
                list.add(anomalyInfo);
            }
        }
        List<AnomalyInfo> newList = null;
        // 创建一个新的集合
        if (status != null){
            // 说明根据状态筛选了
            newList = new ArrayList();
            if (list != null){
                for(AnomalyInfo entity : list){
                    if(status == 0){
                        if (entity.getStatus() == status){
                            newList.add(entity);
                        }
                    } else if (status == 1){
                        if (entity.getStatus() == status){
                            newList.add(entity);
                        }
                    } else {
                        newList.add(entity);
                    }
                }
            }
        } else {
            if (list != null && list.size() > 0){
                newList = new ArrayList();
                newList.addAll(list);
            }
        }
        List<AnomalyInfo> anomalyInfos = timeSort(timeSort, newList);
        Map<String, Object> page = page(anomalyInfos, currentPage, pageSize);
        return page;
    }

    /**
     * 分页
     * @param list
     * @param currentPage
     * @param pageSize
     * @return
     */
    private Map<String, Object> page(List<AnomalyInfo> list,Integer currentPage, Integer pageSize){
        Map<String, Object> page = null;
        if (list == null || list.size() == 0){
            page = new HashMap<>();
            // 总数
            page.put("totalCount", 0);
            // 当前页
            if (currentPage == null || currentPage == 0 || currentPage == 1){
                currentPage = 1;
            }
            page.put("currentPage", currentPage);
            // 每页显示条数
            if (pageSize == null){
                // 默认10条
                pageSize = 10;
            }
            page.put("pageSize",pageSize);
            // 总页数
            page.put("totalPage", 0);
            // 分页数据
            page.put("page", list);
        } else {
            page = new HashMap<>();
            // 总数
            page.put("totalCount", list.size());
            // 当前页
            if (currentPage == null || currentPage == 0 || currentPage == 1){
                currentPage = 1;
            }
            page.put("currentPage", currentPage);
            // 每页显示条数
            if (pageSize == null || pageSize == 0){
                // 默认10条
                pageSize = 10;
            }
            page.put("pageSize",pageSize);
            // 总页数
            page.put("totalPage", (list.size() + pageSize - 1) / pageSize);
            List<AnomalyInfo> anomalyInfos = new ArrayList<>();
            Integer index = ((currentPage - 1) * pageSize);
            Integer scope = pageSize < list.size() ? pageSize : list.size();
            for(int i = index; i < (index + scope < list.size() ? index + scope : list.size()); i++){
                anomalyInfos.add(list.get(i));
            }
            page.put("page", anomalyInfos);
        }
        return page;
    }

    /**
     * 排序
     * @param timeSort
     * @param list
     * @return
     */
    private List<AnomalyInfo> timeSort(Integer timeSort, List<AnomalyInfo> list){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list != null && list.size() > 0){
            if (timeSort != null && timeSort == 0){
                // 升序
                Collections.sort(list, new Comparator<AnomalyInfo>(){
                    @Override
                    public int compare(AnomalyInfo o1, AnomalyInfo o2) {
                        try {
                            Date dt1 = o1.getLatestReportingTime();
                            Date dt2 = o2.getLatestReportingTime();
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
            } else if(timeSort != null && timeSort == 1) {
                // 降序
                Collections.sort(list, new Comparator<AnomalyInfo>(){
                    @Override
                    public int compare(AnomalyInfo o1, AnomalyInfo o2) {
                        try {
                            Date dt1 = o1.getLatestReportingTime();
                            Date dt2 = o2.getLatestReportingTime();
                            if (dt1.getTime() < dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() > dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
            } else {
                // 默认降序
                Collections.sort(list, new Comparator<AnomalyInfo>(){
                    @Override
                    public int compare(AnomalyInfo o1, AnomalyInfo o2) {
                        try {
                            Date dt1 = o1.getLatestReportingTime();
                            Date dt2 = o2.getLatestReportingTime();
                            if (dt1.getTime() < dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() > dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
            }
        }
        return list;
    }

    @Override
    public StatusInfo getStatusInfoByMac( String mac ) {
        return SystemCache.getStatusInfoByMac(mac);
    }

    @Override
    public StatusInfo getStatusInfoByMac( String serverType, String mac ) {
        return SystemCache.getStatusInfoByMac(serverType,mac);
    }

}
