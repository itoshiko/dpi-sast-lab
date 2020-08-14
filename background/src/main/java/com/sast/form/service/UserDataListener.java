package com.sast.form.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.sast.form.exception.ColumnException;
import com.sast.form.pojo.ExcelUser;
import com.sast.user.service.SysUserService;
import com.sast.user.utils.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Map;


public class UserDataListener extends AnalysisEventListener<ExcelUser> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataListener.class);

    SysUserService userService;

    public UserDataListener(SysUserService userService) {
        this.userService = userService;
    }

    ArrayList<ExcelUser> list = new ArrayList<ExcelUser>();

    @Override
    public void invoke(ExcelUser user, AnalysisContext analysisContext) {
        // TODO: 2020/8/14 数据校验
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!userService.checkUser(user)){
            user.setErrInfo("duplicated");
        }
        else {
            user.setPassword(RandomString.getRandomString());
            user.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setErrInfo("");
        }
        LOGGER.info("Data read: " + user.toString() + " " + user.getErrInfo());
        list.add(user);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        LOGGER.info("Read finished");
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
        if(!headMap.containsValue("用户名")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("真实姓名")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("学号")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("邮箱")) throw new ColumnException("Invalid header!");
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        LOGGER.info("解析出错："+exception.getMessage());
        if(exception instanceof ColumnException) throw new ExcelAnalysisStopException("Invalid header!");
    }

    private void saveData() {
        LOGGER.info("{} data, begin to insert to DB", list.size());
        int userAdded = userService.batchAddUser(list);
        LOGGER.info("Insertion success! {} added", userAdded);
    }

    public ArrayList<ExcelUser> getList() {
        return list;
    }
}
