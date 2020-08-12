package com.sast.material.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.material.pojo.enums.FileType;
import com.sast.material.service.MaterialExtraService;
import com.sast.material.service.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

@Controller
public class ImageController {

    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Resource
    MaterialExtraService materialExtraService;
    @Resource
    MaterialService materialService;
    @Resource
    ObjectMapper mapper;

    @Bean
    public BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getCommonsMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(1048576);
        return multipartResolver;
    }

    @GetMapping(value = "/materials/img", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public BufferedImage getMaterialImage(@RequestBody HashMap<String, String> map) throws IOException {
        String uuid = map.get("uuid");
        String path = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath();
        try (InputStream is = new FileInputStream(path + "static/img/materials/" + uuid)) {
            return ImageIO.read(is);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/materials/mat-img-up")
    @ResponseBody
    public String uploadMaterialImage(@RequestParam("img") CommonsMultipartFile file, @RequestParam("mat_id") int materialId) throws JsonProcessingException {
        HashMap<String, String> returnInfo = materialExtraService.uploadMaterialFile(file, materialId, FileType.IMAGE);
        if (returnInfo.get("success").equals("true")) {
            logger.info("Image of material with id = " + materialId + " upload successfully, uuid = " + returnInfo.get("uuid"));
        } else {
            logger.info("Image upload failed: " + returnInfo.get("errInfo"));
        }
        return mapper.writeValueAsString(returnInfo);
    }
}
