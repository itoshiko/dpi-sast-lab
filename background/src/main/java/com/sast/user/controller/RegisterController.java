package com.sast.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.RegisterUser;
import com.sast.user.service.AccountService;
import com.sast.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;


@Controller
public class RegisterController {

    SysUserService userService;
    AccountService accountService;
    @Resource
    ObjectMapper mapper;

    @Autowired
    public void setUserService(SysUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @ResponseBody
    @PostMapping("/accounts/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String register(@RequestBody RegisterUser registerUser) {
        HashMap<String,Object>resultInfo = accountService.addAccount(registerUser);
        try {
            return mapper.writeValueAsString(resultInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "failed";
    }
}
