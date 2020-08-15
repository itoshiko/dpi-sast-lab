package com.sast.user.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.pojo.ResultVO;
import com.sast.user.pojo.SysUser;
import com.sast.user.service.AccountService;
import com.sast.user.service.SysUserService;
import com.sast.user.utils.ResultEnum;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class AccountController {

    @Resource
    ObjectMapper mapper;
    @Resource
    SysUserService userService;
    @Resource
    AccountService accountService;

    @Resource
    @Lazy
    private SessionRegistry sessionRegistry;

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
            return mapper.writeValueAsString(accountService.fuzzySearch(key, roles));
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
    // TODO: 2020/8/6 忘记密码找回/管理员是否能够修改别人的密码？
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
            returnInfo = accountService.updatePassword(map.get("username"), map.get("password"), map.get("oldPassword"));
            if(returnInfo.get("errCode").equals("202")){
                return mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(ResultVO.result(ResultEnum.USER_LOGIN_FAILED, false));
            }
            removeUserSessionByUsername(map.get("username"));
            return mapper.writeValueAsString(returnInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    @PostMapping("/accounts/update-role")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String updateRole(@RequestBody HashMap<String, Object> map){
        HashMap<String, String> returnInfo;
        try {
            returnInfo = accountService.updateRoles((String)map.get("username"), (ArrayList<String>)map.get("roles"));
            removeUserSessionByUsername((String)map.get("username"));
            return mapper.writeValueAsString(returnInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    private void removeUserSessionByUsername(String username) {
        List<Object> users = sessionRegistry.getAllPrincipals(); // 获取session中所有的用户信息
        for (Object principal : users) {
            if (principal instanceof UserDetails) {
                final UserDetails loggedUser = (UserDetails) principal;
                if (username.equals(loggedUser.getUsername())) {
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false); // false代表不包含过期session
                    if (null != sessionsInfo && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionInformation.expireNow();
                        }
                    }
                }
            }
        }
    }


}
