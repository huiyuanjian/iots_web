package io.renren.modules.iots.service;

import io.renren.modules.iots.entity.status.StatusInfo;

import java.util.Map;

/**
 * 首页业务类
 */
public interface FirstPageServerI {

    // 获得所有的服务的状态信息
    Object getAllServer();

    // 获得serverType类型的所有服务状态信息
    Object getServerByType(String serverType, Integer status, Integer timeSort, Integer currentPage, Integer pageSize);

    // 根据mac地址获得服务的状态信息
    StatusInfo getStatusInfoByMac(String mac);

    // 根据服务类型和mac地址获得服务的状态信息
    StatusInfo getStatusInfoByMac(String serverType,String mac);




}
