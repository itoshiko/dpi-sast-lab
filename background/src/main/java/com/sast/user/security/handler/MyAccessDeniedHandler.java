package com.sast.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.ResultVO;
import com.sast.user.utils.ResultEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//无权访问
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Resource
    ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException, IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(ResultVO.result(ResultEnum.USER_NO_ACCESS, false)));
    }
}
