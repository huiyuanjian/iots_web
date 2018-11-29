package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

@TableName("sys_role_ioserverInfo_scope")
public class SysIotsIoserverInfoScopeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    private Long roleId;

    private Long roleIoserverinfoIdScope;

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

    public Long getRoleIoserverinfoIdScope() {
        return roleIoserverinfoIdScope;
    }

    public void setRoleIoserverinfoIdScope(Long roleIoserverinfoIdScope) {
        this.roleIoserverinfoIdScope = roleIoserverinfoIdScope;
    }
}
