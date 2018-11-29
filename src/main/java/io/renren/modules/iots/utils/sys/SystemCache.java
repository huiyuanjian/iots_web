package io.renren.modules.iots.utils.sys;

import io.renren.modules.iots.entity.status.StatusInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 系统缓存类
 */
@Slf4j
public class SystemCache {

    private SystemCache(){}

    /**
     * 暂存接收到的服务状态信息
     * Map<服务类型, Map<服务的mac地址,StatusInfo>>
     */
    private static Map<String, Map<String,StatusInfo>> SERVERINFO_MAP = new LinkedHashMap <>();

    /**
     * 获得所有的服务的状态信息
     */
    public static Map<String, Map<String,StatusInfo>> getAllServer(){
        Map<String, Map<String,StatusInfo>> map = new LinkedHashMap <>(SERVERINFO_MAP);
        return map;
    }

    /**
     * 根据服务类型获得所有的服务的状态信息
     */
    public static Map<String,StatusInfo> getServerByType(String servertype){
        return SERVERINFO_MAP.get(servertype);
    }

    /**
     * 根据mac地址获取服务的状态信息
     */
    public static StatusInfo getStatusInfoByMac(String mac){
        Map<String, Map<String,StatusInfo>> allmap = SERVERINFO_MAP;
        for (Map<String,StatusInfo> map : allmap.values()){
            StatusInfo statusInfo = map.get(mac);
            if (statusInfo != null){
                return statusInfo;
            }
        }
        return null;
    }

    /**
     * 根据服务类型和mac地址获取服务的状态信息
     */
    public static StatusInfo getStatusInfoByMac(String serverType,String mac){
        Map<String,StatusInfo> map = SERVERINFO_MAP.get(serverType);
        if (map.isEmpty()){
            return null;
        } else {
            return map.get(mac);
        }
    }

    /**
     * 存入服务的状态信息
     */
    public static void put(StatusInfo statusInfo){
        Map<String, StatusInfo> stringStatusInfoMap = SERVERINFO_MAP.get(statusInfo.getServerType());
        if (stringStatusInfoMap != null){
            stringStatusInfoMap.put(statusInfo.getMacAddress(),statusInfo);
        } else {
            stringStatusInfoMap = new LinkedHashMap<>();
            stringStatusInfoMap.put(statusInfo.getMacAddress(),statusInfo);
            SERVERINFO_MAP.put(statusInfo.getServerType(),stringStatusInfoMap);
        }
    }

    /**
     * 清理线程
     */
    private static void cleanThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Map<String,StatusInfo>> allmap;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("清理 SERVERINFO_MAP 容器 的线程 睡眠异常，异常信息是：{}",e.getMessage());
                }
                Map<String, Map<String,StatusInfo>> new_map = new LinkedHashMap <>();
                Map<String,StatusInfo> map_element;
                String type = null;
                while (true){
                    allmap = SERVERINFO_MAP;
                    for (Map<String,StatusInfo> map : allmap.values()){
                        map_element = map;
                        for (StatusInfo statusInfo : map.values()){
                            if (System.currentTimeMillis() - statusInfo.getCreatetime() > 180000){// 超过180秒（即三个周期）后，将元素移除
                                type = statusInfo.getServerType();
                                map_element.remove(statusInfo.getMacAddress());
                            }
                        }
                        new_map.put(type,map_element);
                    }
                    SERVERINFO_MAP = new_map;
                }
            }
        },"清理 SERVERINFO_MAP 线程").start();
    }

    /**
     * 清理 SERVERINFO_MAP 容器
     */
    static {
        //TODO 暂时不运行
        //cleanThread();
    }


}
