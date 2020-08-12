package com.sast.material.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.material.pojo.SysMaterialImg;
import com.sast.material.pojo.enums.FileType;
import com.sast.material.service.MaterialExtraService;
import com.sast.material.service.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Controller
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Resource
    MaterialService materialService;
    @Resource
    MaterialExtraService materialExtraService;
    @Resource
    ObjectMapper mapper;

    //@PreAuthorize("isAuthenticated()")
    @PostMapping("/materials/mat-doc-up")
    @ResponseBody
    public String uploadMaterialImage(@RequestParam("doc") CommonsMultipartFile file, @RequestParam("mat_id") int materialId) throws JsonProcessingException {
        HashMap<String, String> returnInfo = materialExtraService.uploadMaterialFile(file, materialId, FileType.DOC);
        if (returnInfo.get("success").equals("true")) {
            logger.info("Documentation of material with id = " + materialId + " upload successfully, uuid = " + returnInfo.get("uuid"));
        } else {
            logger.info("Documentation upload failed: " + returnInfo.get("errInfo"));
        }
        return mapper.writeValueAsString(returnInfo);
    }

    @RequestMapping("/materials/doc")
    @ResponseBody
    public void getMaterialDoc(@RequestBody HashMap<String, String> map) throws IOException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletResponse response = requestAttributes.getResponse();
        //需要下载的图片的地址
        String uuid = map.get("uuid");
        String path = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath();
        path = path + "static/doc/materials/";
        //设置response响应头
        // TODO: 2020/8/13 文件类型：下载，数据库储存
        assert response != null;
        response.reset(); //设置页面不缓存，清空buffer
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data"); //二进制传输数据
        File file = new File(path, uuid);
        String type = new MimetypesFileTypeMap().getContentType(file);
        response.setHeader("Content-type", type);
        response.setHeader("Content-Disposition", "attachment;filename=" + uuid);
        //读取文件：输入流
        InputStream inputStream = new FileInputStream(file);
        //写出文件：输出流
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int index = 0;
        //执行写出操作
        while ((index = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, index);
            outputStream.flush();
        }
        outputStream.close();
        inputStream.close();
    }

}

