package com.sast.form.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.form.service.FormService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Resource
    FormService formService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    @GetMapping("accounts/extract")
    @ResponseBody
    public void extractUserData(HttpServletResponse response) throws IOException {
        formService.writeDataToExcel(response, 1);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    @GetMapping("materials/extract")
    @ResponseBody
    public void extractMaterialData(HttpServletResponse response) throws IOException {
        formService.writeDataToExcel(response, 2);
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
                    type.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }
    }
}
