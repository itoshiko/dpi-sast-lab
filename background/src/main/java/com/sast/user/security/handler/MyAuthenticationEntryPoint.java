package com.sast.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.ResultVO;
import com.sast.user.utils.ResultEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;


//未登录
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = 1L;

    @Resource
    ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException authException) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(ResultVO.result(ResultEnum.USER_NEED_AUTHORITIES, false)));
    }
}
