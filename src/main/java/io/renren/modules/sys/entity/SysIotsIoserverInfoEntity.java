package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

@TableName("sys_role_ioserverInfo")
public class SysIotsIoserverInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    private Long roleId;

    private Long ioserverInfoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getIoserverInfoId() {
        return ioserverInfoId;
    }

    public void setIoserverInfoId(Long ioserverInfoId) {
        this.ioserverInfoId = ioserverInfoId;
    }
}
