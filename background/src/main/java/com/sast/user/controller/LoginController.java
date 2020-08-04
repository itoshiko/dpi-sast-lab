package com.sast.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.ResultVO;
import com.sast.user.utils.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//如代码所示，获取当前登录用户：SecurityContextHolder.getContext().getAuthentication()
//@PreAuthorize 用于判断用户是否有指定权限，没有就不能访问

@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    ObjectMapper mapper;

    @GetMapping("/")
    public String showHome() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("当前登陆用户：" + name);
        return "home.html";
    }

    @RequestMapping("/login")
    public String showLogin() {
        return "login.html";
    }

    @GetMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @GetMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_NORMAL')")
    public String printUser() {
        return "如果你看见这句话，说明你有ROLE_NORMAL角色";
    }

    @GetMapping("/root")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String printRoot() {
        return "如果你看见这句话，说明你有ROLE_ROOT角色";
    }

    @GetMapping("/login/error")
    public void loginError(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        AuthenticationException exception =
                (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        try {
            response.getWriter().write(exception.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/login/invalid")
    public void loginInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(ResultVO.result(ResultEnum.LOGIN_IS_OVERDUE, false)));
    }
}
