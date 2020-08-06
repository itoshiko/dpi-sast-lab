package com.sast.user.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.RegisterUser;
import com.sast.user.pojo.ResultVO;
import com.sast.user.pojo.SysUser;
import com.sast.user.service.AccountService;
import com.sast.user.service.SysUserService;
import com.sast.user.utils.ResultEnum;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

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
    @PreAuthorize("isAuthenticated() and principal.username.equals(#username)")
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
            if (roleJSON != null) {
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

    @PostMapping("/accounts/delete")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String deleteAccount(@RequestBody String request) {
        HashMap<String, String> returnInfo;
        try {
            String username = (String) mapper.readValue(request, HashMap.class).get("username");
            returnInfo = accountService.deleteAccount(username);
            if (returnInfo.get("errCode").equals("403")) {
                return mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(ResultVO.result(ResultEnum.USER_NO_ACCESS, false));
            } else return mapper.writeValueAsString(returnInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    @PostMapping("/accounts/update")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String updateAccount(@RequestBody HashMap<String, String> map) {
        HashMap<String, Object> returnInfo;
        try {
            returnInfo = accountService.updateAccount(map);
            ArrayList<String> errCodeList = (ArrayList<String>) returnInfo.get("errCode");
            if (errCodeList.contains("403")) {
                return mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(ResultVO.result(ResultEnum.USER_NO_ACCESS, false));
            } else return mapper.writeValueAsString(returnInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    @PostMapping("/accounts/update-password")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String updatePassword(@RequestBody HashMap<String, String> map) {
        HashMap<String, String> returnInfo;
        try {
            returnInfo = accountService.updatePassword(map.get("username"), map.get("password"));
            return mapper.writeValueAsString(returnInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "failed";
    }
}
