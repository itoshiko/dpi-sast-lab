package com.sast.user.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.SysRole;
import com.sast.user.pojo.SysUser;
import com.sast.user.service.AccountService;
import com.sast.user.service.SysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;

@Controller
public class AccountController {

    @Resource
    ObjectMapper mapper;
    @Resource
    SysUserService userService;
    @Resource
    AccountService accountService;

    @GetMapping("/account")
    @ResponseBody
    @PreAuthorize("principal.username.equals(#username)")
    public String accountInfo(String username) {
        try {
            SysUser user = userService.selectByName(username);
            user.setPassword("");
            return mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @PostMapping("/accounts")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String showAccounts(@RequestBody String request) {
        JsonNode node = null;
        try {
            node = mapper.readTree(request);
            String roleJSON = node.get("role").toString();
            ArrayList<String> roles = null;
            if(roleJSON != null){
                roles = mapper.readValue(roleJSON, ArrayList.class);
            }
            String key = node.get("search").toString();
            key = key.substring(1, key.length() - 1);
            return accountService.fuzzySearch(key, roles);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "failed";
    }
}
