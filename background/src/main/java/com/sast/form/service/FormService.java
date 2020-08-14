package com.sast.form.service;

import com.alibaba.excel.EasyExcel;
import com.sast.form.pojo.ExcelUser;
import com.sast.user.pojo.SysUser;
import com.sast.user.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;

@Service
public class FormService {

    @Resource
    SysUserService userService;

    public ArrayList<ExcelUser> readForm() {
        String fileName = "D:\\Java_dev\\dpi-sast-lab\\background\\target\\classes\\static\\doc" + File.separator + "demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        UserDataListener userDataListener = new UserDataListener(userService);
        EasyExcel.read(fileName, ExcelUser.class, userDataListener).sheet().doRead();
        return userDataListener.getList();
    }

    public void simpleWrite(ArrayList<ExcelUser> data) {
        String fileName = "D:\\Java_dev\\dpi-sast-lab\\background\\target\\classes\\static\\doc" + File.separator + "demo2.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, ExcelUser.class).sheet("模板").doWrite(data);
    }
}
