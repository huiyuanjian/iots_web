package io.renren.modules.iots.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("iots_regist_info")
public class IotsRegistInfoEntity {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * ip+port
     * 格式形如：192.192.192.192:8888
     */
    private String host;

    /**
     * mac地址
     */
    private String macAddress;

    /**
     * 服务的类型
     *
     * PROXY--采集代理
     * COLLECT--物联网接口
     * WEB--web管理端
     * CONTROL--控制接口服务
     * DISTRIBUTE--分发接口服务
     */
    private String serverType;

    /**
     * 服务名称(如果没有则赋一个""值)
     */
    private String serverName;

    /**
     * 说明
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime = new Date();
}
