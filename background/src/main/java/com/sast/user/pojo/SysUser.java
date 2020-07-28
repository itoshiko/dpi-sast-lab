package com.sast.user.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class SysUser implements Serializable {

    static final long serialVersionUID = 1L;

    private ArrayList<SysRole> sysRoles;

    private String userName;
    private String password;
    private String mail;
    private String realName;
    private String studentId;
    private int uid;

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
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

    public SysUser(RegisterUser registerUser) {
        this.mail = registerUser.getMail();
        this.realName = registerUser.getRealName();
        this.studentId = registerUser.getStudentId();
        this.userName = registerUser.getUserName();
        ArrayList<SysRole> roles = new ArrayList<SysRole>();
        roles.add(new SysRole(1, "ROLE_NORMAL"));
        this.sysRoles = roles;
        this.password = null;
    }

    public SysUser() {
    }

    @Override
    public String toString() {
        return "User{" +
                "roles=" + sysRoles +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", realName='" + realName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", uid=" + uid +
                '}';
    }
}
