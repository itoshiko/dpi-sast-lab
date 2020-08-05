package com.sast.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.mapper.SysUserMapper;
import com.sast.user.pojo.RegisterUser;
import com.sast.user.pojo.SysRole;
import com.sast.user.pojo.SysUser;
import com.sast.user.utils.MailUtil;
import com.sast.user.utils.RandomString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

@Service
public class AccountService {
    @Resource
    SysUserMapper userMapper;
    @Resource
    ObjectMapper mapper;
    @Resource
    SysUserService userService;


    public String fuzzySearch(String keyword, ArrayList<String> roles) throws JsonProcessingException {
        if (roles != null && roles.size() > 0) {
            ArrayList<SysUser> rawResult = null;
            if (keyword != null && !keyword.equals("")) {
                rawResult = userMapper.fuzzySearch(keyword);
            } else {
                rawResult = userMapper.selectAll();
            }
            SysUser tempUser = null;
            for (Iterator<SysUser> iterator = rawResult.iterator(); iterator.hasNext(); ) {
                tempUser = iterator.next();
                if (!hasRole(tempUser, roles)) {
                    iterator.remove();
                }
            }
            for (SysUser user : rawResult) {
                user.setPassword("");
            }
            return mapper.writeValueAsString(rawResult);
        } else if (keyword != null && !keyword.equals("")) {
            ArrayList<SysUser> rawResult = userMapper.fuzzySearch(keyword);
            for (SysUser user : rawResult) {
                user.setPassword("");
            }
            return mapper.writeValueAsString(rawResult);
        } else {
            ArrayList<SysUser> rawResult = userMapper.selectAll();
            for (SysUser user : rawResult) {
                user.setPassword("");
            }
            return mapper.writeValueAsString(rawResult);
        }
    }


    private boolean hasRole(SysUser user, ArrayList<String> roles) {
        for (SysRole role : user.getSysRoles()) {
            for (String roleName : roles) {
                if (roleName.equalsIgnoreCase(role.getRoleName())) return true;
            }
        }
        return false;
    }

    public HashMap<String, Object> addAccount(RegisterUser registerUser) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        HashMap<String, Object> returnInfo = new HashMap<>();
        ArrayList<String> errInfoList = new ArrayList<>();
        ArrayList<String> errCodeList = new ArrayList<>();
        boolean errFlag = false;
        if (userService.isMailExisted(registerUser.getMail())) {
            errFlag = true;
            errCodeList.add("1");
            errInfoList.add("mail existed");
        }
        if (userService.isStudentIdExisted(registerUser.getStudentId())) {
            errFlag = true;
            errCodeList.add("2");
            errInfoList.add("student ID existed");
        }
        if (userService.isUsernameExisted(registerUser.getUserName())) {
            errFlag = true;
            errCodeList.add("3");
            errInfoList.add("username existed");
        }
        if (errFlag) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", errInfoList);
            returnInfo.put("errCode", errCodeList);
            return returnInfo;
        }
        try {
            SysUser user = new SysUser();
            user.setRealName(registerUser.getRealName());
            user.setMail(registerUser.getMail());
            user.setStudentId(registerUser.getStudentId());
            user.setUserName(registerUser.getUserName());
            String rawPassword = RandomString.getRandomString();
            user.setPassword(bCryptPasswordEncoder.encode(rawPassword));
            user.setSysRoles(userService.selectRoles(registerUser.getRoles()));
            userService.addUser(user);
            //MailUtil.sendPassword(registerUser.getMail(), rawPassword);
        } catch (Exception e) {
            e.printStackTrace();
            returnInfo.put("success", "false");
            errCodeList.add("233");
            errInfoList.add("unexpected error");
            returnInfo.put("errInfo", errInfoList);
            returnInfo.put("errCode", errCodeList);
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", errInfoList);
        returnInfo.put("errCode", errCodeList);
        return returnInfo;
    }

    public HashMap<String, String> deleteAccount(String username){
        SysUser user = userService.selectByName(username);
        int authentication;
        int highestRole = getHighestRole(user);
        boolean errFlag = false;
        HashMap<String, String> returnInfo = new HashMap<>();
        if(userHasAuthority()) authentication = 3;
        else authentication = 2;
        if(user == null) {
            returnInfo.put("errInfo", "user not found");
            returnInfo.put("errCode", "1");
            errFlag = true;
        }
        else if(highestRole >= authentication) {
            returnInfo.put("errInfo", "denied");
            returnInfo.put("errCode", "2");
            errFlag = true;
        }
        if (errFlag) {
            returnInfo.put("success", "false");
            return returnInfo;
        }
        try{
            userService.deleteUserById(user.getUid());
        } catch (Exception e) {
            e.printStackTrace();
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            returnInfo.put("errCode", "233");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        returnInfo.put("errCode", "");
        return returnInfo;
    }

    private int getHighestRole(SysUser user) {
        int result = 0;
        for(SysRole role : user.getSysRoles()) {
            if(result < role.getRoleId()) result = role.getRoleId();
        }
        return result;
    }

    private boolean userHasAuthority()
    {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if ("ROLE_ROOT".equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }


}
