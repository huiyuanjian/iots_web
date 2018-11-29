package io.renren.modules.iots.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * 日志文件信息实体类
 */
@Data
public class LogEntity {

    /**
     * id
     */
    @TableField("id")
    @JsonIgnore
    private long id;

    /**
     * 日志名称 格式为：服务类型_yyyy-MM-dd_顺序号.log
     * 顺序号就是时间戳
     */
    @TableField("file_name")
    @JsonProperty(value ="fileName")
    private String file_name;

    /**
     * 日期 格式为：yyyy-MM-dd
     */
    @TableField("day_string")
    @JsonProperty(value ="dayString")
    private String day_string;

    /**
     * 服务类型
     */
    @TableField("server_type")
    @JsonProperty(value ="serverType")
    private String server_type;

    /**
     * 服务的mac地址
     */
    @TableField("server_mac")
    @JsonProperty(value ="serverMac")
    private String server_mac;

    /**
     * 顺序号（就是生成日志文件时的时间戳）
     */
    @JsonIgnore
    @TableField("index")
    private long index;

    /**
     * 入库时间
     */
    @JsonIgnore
    @TableField("create_time")
    private Date create_time;

    /**
     * 是否删除,默认是未删除
     */
    @TableLogic
    @JsonIgnore
    @TableField("is_del")
    private boolean isdel;

    /**
     * 日志上传路径
     */
    @TableField("upload_url")
    @JsonProperty(value ="uploadUrl")
    private String upload_url;
}
