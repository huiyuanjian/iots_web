package io.renren.modules.iots.entity.monitor;

import java.io.Serializable;

/**
 * 所有被绑定的变量
 */
public class VarizableDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 变量ID
     */
    private Long varizableId;

    /**
     * 变量名字
     */
    private String varizableName;

    /**
     * 变量值
     */
    private String varizableValue;

    public Long getVarizableId() {
        return varizableId;
    }

    public void setVarizableId(Long varizableId) {
        this.varizableId = varizableId;
    }

    public String getVarizableName() {
        return varizableName;
    }

    public void setVarizableName(String varizableName) {
        this.varizableName = varizableName;
    }

    public String getVarizableValue() {
        return varizableValue;
    }

    public void setVarizableValue(String varizableValue) {
        this.varizableValue = varizableValue;
    }
}
