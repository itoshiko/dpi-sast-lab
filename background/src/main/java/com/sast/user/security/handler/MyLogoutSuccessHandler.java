package com.sast.user.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.ResultVO;
import com.sast.user.utils.ResultEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    ObjectMapper mapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(ResultVO.result(ResultEnum.USER_LOGOUT_SUCCESS, true)));

    }
}
