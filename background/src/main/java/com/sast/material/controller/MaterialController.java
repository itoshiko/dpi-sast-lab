package com.sast.material.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.material.pojo.SysMaterial;
import com.sast.material.service.MaterialService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class MaterialController {

    @Resource
    MaterialService materialService;
    @Resource
    ObjectMapper mapper;

    @GetMapping("/materials")
    @ResponseBody
    //@PreAuthorize("isAuthenticated()")
    public String selectMaterial(@RequestBody HashMap<String, Object> map){
        ArrayList<SysMaterial> materials =materialService.selectMaterial(map);
        try {
            return mapper.writeValueAsString(materials);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }
}
