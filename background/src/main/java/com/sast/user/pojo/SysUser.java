package com.sast.user.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.sast.form.service.RoleListConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class SysUser implements Serializable, UserDetails {

    @ExcelIgnore
    static final long serialVersionUID = 7171722954972237961L;
    @ExcelProperty(value = "权限", index = 5, converter = RoleListConverter.class)
    private ArrayList<SysRole> sysRoles;
    @ExcelIgnore
    private Set<? extends GrantedAuthority> authorities;
    @ExcelProperty(value = "用户名", index = 0)
    private String username;
    @ExcelIgnore
    private String password;
    @ExcelProperty(value = "邮箱", index = 1)
    private String mail;
    @ExcelProperty(value = "真实姓名", index = 2)
    private String realName;
    @ExcelProperty(value = "学号", index = 3)
    private String studentId;
    @ExcelProperty(value = "UID", index = 4)
    private int uid;
    @ExcelProperty(value = "有效", index = 6)
    private boolean enabled = true;

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }


    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public ArrayList<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(ArrayList<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SysUser() {
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "sysRoles=" + sysRoles +
                ", authorities=" + authorities +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", realName='" + realName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", uid=" + uid +
                ", enabled=" + enabled +
                '}';
    }
}
