package com.sast.user.pojo;

import java.io.Serializable;

public class SysRole implements Serializable {

    static final long serialVersionUID = 1L;

    private int roleId;
    private String roleName;

    public SysRole(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public SysRole(){}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
