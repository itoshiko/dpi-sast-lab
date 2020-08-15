package com.sast.form.service;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.form.pojo.ExcelUser;
import com.sast.material.pojo.SysMaterial;
import com.sast.material.service.MaterialService;
import com.sast.user.pojo.SysUser;
import com.sast.user.service.AccountService;
import com.sast.user.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class FormService {

    @Resource
    SysUserService userService;
    @Resource
    AccountService accountService;
    @Resource
    MaterialService materialService;
    @Resource
    ObjectMapper mapper;

    private static String CONTENT = null;

    public ArrayList<ExcelUser> importUserDataFromExcel(MultipartFile file) throws IOException {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        UserDataListener userDataListener = new UserDataListener(userService);
        EasyExcel.read(file.getInputStream(), ExcelUser.class, userDataListener).sheet().doRead();
        return userDataListener.getList();
    }

    public void writeDataToExcel(HttpServletResponse response, int type) throws IOException {
        //导出用户数据
        ArrayList result = null;
        if(type == 1){
            result = accountService.fuzzySearch("", new ArrayList<String>());
            CONTENT = "user";
        }
        else if(type == 2){
            result = materialService.selectMaterial(new HashMap<>());
            CONTENT = "material";
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(CONTENT + "_data", StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            if(type == 1){
                EasyExcel.write(response.getOutputStream(), SysUser.class).autoCloseStream(Boolean.FALSE).sheet("user")
                        .doWrite(result);
            }
            else if (type == 2){
                // 这里需要设置不关闭流
                EasyExcel.write(response.getOutputStream(), SysMaterial.class).autoCloseStream(Boolean.FALSE).sheet("user")
                        .doWrite(result);
            }

        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("success", "fail");
            map.put("message", "extract failed " + e.getMessage());
            response.getWriter().println(mapper.writeValueAsString(map));
        }
    }
}
