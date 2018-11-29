package io.renren.modules.iots.entity.log;

/**
 * 日志tree 数据查询实体类
 */
public class LoggerDto {

    /**
     * 服务类型
     */
    private String type;

    /**
     * 服务名字(用于模糊查询)
     */
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isEmpty(LoggerDto loggerDto){
        return loggerDto == null ? true : loggerDto.type == null ? true : "".equals(loggerDto) ? true : false;
    }
}
