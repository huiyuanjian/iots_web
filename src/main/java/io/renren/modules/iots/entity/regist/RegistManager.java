package io.renren.modules.iots.entity.regist;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册信息管理类
 */
public class RegistManager {

    /**
     * 采集代理注册的消息
     * String 是mac地址
     */
    public static Map<String,RegistInfo> REGIST_MAP = new HashMap<>();

}
