package com.sast.user.pojo;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "请求注册时应返回的数据，json格式")
public class RegisterUser {
    static final long serialVersionUID = 1L;

    private String userName;
    private String mail;
    private String realName;
    private String studentId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
