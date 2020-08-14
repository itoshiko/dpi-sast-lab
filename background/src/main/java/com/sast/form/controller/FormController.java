package com.sast.form.controller;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Controller
public class FormController {

    @Resource
    ObjectMapper mapper;

    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=demo.xlsx");
        //EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());

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
