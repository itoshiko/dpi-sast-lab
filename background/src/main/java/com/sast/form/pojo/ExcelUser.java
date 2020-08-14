package com.sast.form.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

public class ExcelUser {
    @ExcelIgnore
    private int uid;
    @ExcelProperty("用户名")
    private String username;
    @ExcelProperty("真实姓名")
    private String realName;
    @ExcelProperty("邮箱")
    private String mail;
    @ExcelProperty("学号")
    private String studentId;
    @ExcelProperty(index = 4)
    private String password;
    @ExcelProperty(index = 5)
    private String errInfo;
    @ExcelIgnore
    private String encryptedPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public ExcelUser() {
    }

    @Override
    public String toString() {
        return "ExcelUser{" +
                ", username='" + username + '\'' +
                ", realName='" + realName + '\'' +
                ", mail='" + mail + '\'' +
                ", studentId='" + studentId + '\'' +
                ", errInfo='" + errInfo + '\'' +
                '}';
    }
}
