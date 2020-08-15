package com.sast.form.controller;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.SysUser;
import com.sast.user.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FormController {

    @Resource
    ObjectMapper mapper;
    @Resource
    AccountService accountService;

    @GetMapping("accounts/extract")
    public void download(HttpServletResponse response) throws IOException {
        ArrayList<SysUser> result = accountService.fuzzySearch("", new ArrayList<String>());
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("user_data", StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), SysUser.class).autoCloseStream(Boolean.FALSE).sheet("user")
                    .doWrite(result);
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


    @PostMapping("upload")
    @ResponseBody
    public String importUserDataFromExcel(MultipartFile file) throws IOException {
        HashMap<String, String> returnInfo = new HashMap<>();
        if(!checkExcel(file)){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid file");
            return mapper.writeValueAsString(returnInfo);
        }
        //EasyExcel.read(file.getInputStream(), DemoData.class, new DemoDataListener()).sheet().doRead();
        return "success";
    }

    private boolean checkExcel(MultipartFile file) {
        if (file == null) return false;
        else {
            String type = file.getContentType();
            assert type != null;
            return type.equals("application/vnd.ms-excel") ||
                    type.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                    type.equals("text/csv");
        }
    }
}
