package com.sast.user.controller;

import com.sast.user.pojo.RegisterUser;
import com.sast.user.pojo.SysUser;
import com.sast.user.service.SysUserService;
import com.sast.user.utils.MailUtil;
import com.sast.user.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RegisterController {

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    SysUserService userService;

    @Autowired
    public void setUserService(SysUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(){
        return ("register.html");
    }

    @ResponseBody
    @PostMapping("/register/doRegister")
    public String doRegister(RegisterUser registerUser) {
        try {
            SysUser sysUser = new SysUser(registerUser);
            String rawPassword = RandomString.getRandomString();
            sysUser.setPassword(bCryptPasswordEncoder.encode(rawPassword));
            userService.addUser(sysUser);
            MailUtil.sendPassword(registerUser.getMail(), rawPassword);
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }
}
