package com.sast.user.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.ResultVO;
import com.sast.user.pojo.SysUser;
import com.sast.user.utils.ResultEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    ObjectMapper mapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {


        // 跨域处理
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        // 允许的请求方法
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        // 允许的请求头
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 设置响应头
        httpServletResponse.setContentType("application/json;charset=utf-8");

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        httpServletResponse.getWriter().write(mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(ResultVO.success(name)));
    }
}
