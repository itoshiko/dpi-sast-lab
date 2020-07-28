package com.sast.material.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
public class FileController {

    @RequestMapping("/upload")
    public String fileUpload(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {
        //获取文件名
        String uploadFileName = file.getOriginalFilename();
        //如果文件名为空，直接回到首页
        if (uploadFileName.equals("")) return "redirect:/index.jsp";
        //上传路径保存设置
        String path = request.getServletContext().getRealPath("/upload");
        //路径不存在则创建
        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdir();
        }
        InputStream inputStream = file.getInputStream(); //文件输入流
        OutputStream outputStream = new FileOutputStream(new File(realPath, uploadFileName)); //文件输出流

        //读取写出
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
            outputStream.flush();
        }
        outputStream.close();
        inputStream.close();
        return "redirect:/index.jsp";
    }

    //采用file.transferTo来保存上传文件
    @RequestMapping("/upload2")
    public String fileUpload2(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {
        String path = request.getServletContext().getRealPath("/upload");
        File realPath = new File(path);
        if (!realPath.exists()) {
            realPath.mkdir();
        }
        //通过CommonsMultipartFile的方法直接写文件
        file.transferTo(new File(realPath + "/" + file.getOriginalFilename()));
        return "redirect:/index.jsp";
    }

    @RequestMapping("/download")
    public String download(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //需要下载的图片的地址
        String path = request.getServletContext().getRealPath("/upload");
        String fileName = "test.jpg";

        //设置response响应头
        response.reset(); //设置页面不缓存，清空buffer
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data"); //二进制传输数据
        //设置响应头
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));

        File file = new File(path, fileName);
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
        return null;
    }

}

