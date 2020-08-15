package com.sast.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sast.user.mapper.SysUserMapper;
import com.sast.user.pojo.RegisterUser;
import com.sast.user.pojo.SysRole;
import com.sast.user.pojo.SysUser;
import com.sast.user.security.CustomUserDetailsService;
import com.sast.user.utils.MailUtil;
import com.sast.user.utils.RandomString;
import com.sast.user.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    SysUserService userService;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    public void setCustomUserDetailsService(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }


    public ArrayList<SysUser> fuzzySearch(String keyword, ArrayList<String> roles) throws JsonProcessingException {
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
            return rawResult;
        } else if (keyword != null && !keyword.equals("")) {
            ArrayList<SysUser> rawResult = userMapper.fuzzySearch(keyword);
            for (SysUser user : rawResult) {
                user.setPassword("");
            }
            return rawResult;
        } else {
            ArrayList<SysUser> rawResult = userMapper.selectAll();
            for (SysUser user : rawResult) {
                user.setPassword("");
            }
            return rawResult;
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
            user.setUsername(registerUser.getUserName());
            String rawPassword = RandomString.getRandomString();
            user.setPassword(bCryptPasswordEncoder.encode(rawPassword));
            user.setSysRoles(userService.selectRoles(registerUser.getRoles()));
            userService.addUser(user);
            // TODO: 2020/8/5 可以发邮件之后注册时发送密码 
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

    public HashMap<String, String> deleteAccount(String username) {
        SysUser user = userService.selectByName(username);
        int authentication;
        int highestRole = getHighestRole(user);
        boolean errFlag = false;
        HashMap<String, String> returnInfo = new HashMap<>();
        if (userHasAuthority("ROLE_ROOT")) authentication = 3;
        else authentication = 2;
        if (user == null) {
            returnInfo.put("errInfo", "user not found");
            returnInfo.put("errCode", "1");
            errFlag = true;
        } else if (highestRole >= authentication) {
            returnInfo.put("errInfo", "denied");
            returnInfo.put("errCode", "403");
            errFlag = true;
        }
        if (errFlag) {
            returnInfo.put("success", "false");
            return returnInfo;
        }
        try {
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

    public HashMap<String, Object> updateAccount(HashMap<String, String> request) {
        HashMap<String, Object> returnInfo = new HashMap<>();
        ArrayList<String> errInfoList = new ArrayList<>();
        ArrayList<String> errCodeList = new ArrayList<>();
        boolean errFlag = false;
        String studentId = request.get("studentId");
        if (studentId == null || studentId.isEmpty()) {
            errInfoList.add("invalid student id");
            errCodeList.add("20");
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", errInfoList);
            returnInfo.put("errCode", errCodeList);
            return returnInfo;
        }
        SysUser user = userService.selectByStudentId(studentId);
        if (user == null) {
            errInfoList.add("invalid student id");
            errCodeList.add("20");
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", errInfoList);
            returnInfo.put("errCode", errCodeList);
            return returnInfo;
        }
        //权限检查
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(loginUser.getUsername().equals(user.getUsername()))) {
            if (!userHasAuthority("ROLE_ROOT")) {
                errInfoList.add("denied");
                errCodeList.add("403");
                returnInfo.put("success", "false");
                returnInfo.put("errInfo", errInfoList);
                returnInfo.put("errCode", errCodeList);
                return returnInfo;
            }
        }
        //判断是否修改邮箱，邮箱是否重复以及邮箱地址是否有效
        String mail = request.get("mail");
        if (mail != null && !mail.equals("")) {
            if (userService.isMailExisted(mail)) {
                errFlag = true;
                errCodeList.add("1");
                errInfoList.add("mail existed");
            } else if (MailUtil.isValidEmail(mail)) {
                errFlag = true;
                errCodeList.add("10");
                errInfoList.add("invalid email");
            } else {
                user.setMail(mail);
            }
        }

        //判断是否修改用户名，用户名是否重复以及是否为合法的用户名
        String username = request.get("username");
        if (username != null && !username.equals("")) {
            if (userService.isUsernameExisted(username)) {
                errFlag = true;
                errCodeList.add("3");
                errInfoList.add("username existed");
            } else {
                Authentication nowAuth = SecurityContextHolder.getContext().getAuthentication();
                UserDetails newUser = new User(username, "", nowAuth.getAuthorities());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(newUser, nowAuth.getCredentials(), nowAuth.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                user.setUsername(username);
            }
        }
        //判断是否修改真实姓名
        String realName = request.get("realName");
        if (realName != null && !realName.equals("")) {
            user.setRealName(realName);
        }
        userService.updateUser(user);
        if (errFlag) {
            returnInfo.put("success", "false");
        } else {
            returnInfo.put("success", "true");
        }
        returnInfo.put("errInfo", errInfoList);
        returnInfo.put("errCode", errCodeList);
        return returnInfo;
    }

    @PreAuthorize("principal.username.equals(#username)")
    public HashMap<String, String> updatePassword(String username, String password, String oldPassword){
        HashMap<String, String> returnInfo = new HashMap<>();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        if(!bCryptPasswordEncoder.matches(oldPassword, userDetails.getPassword())) {
            returnInfo.put("errCode", "202");
            return returnInfo;
        }
        if (username == null || username.isEmpty()) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid username");
            returnInfo.put("errCode", "30");
            return returnInfo;
        }
        SysUser user = userService.selectByName(username);
        if(user == null){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid username");
            returnInfo.put("errCode", "30");
            return returnInfo;
        }
        //判断是否修改密码以及密码强度
        if (password != null && !password.equals("")) {
            if (!StringUtil.checkPassword(password)) {
                returnInfo.put("success", "false");
                returnInfo.put("errInfo", "password too weak");
                returnInfo.put("errCode", "40");
            }
            else{
                userService.updatePassword(user.getUid(), bCryptPasswordEncoder.encode(password));
                returnInfo.put("success", "true");
                returnInfo.put("errInfo", "");
                returnInfo.put("errCode", "");
            }
        }
        return returnInfo;
    }

    public HashMap<String, String> updateRoles(String username, ArrayList<String> roles){
        HashMap<String, String> returnInfo = new HashMap<>();
        SysUser user;
        if (username == null || username.isEmpty()) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid username");
            returnInfo.put("errCode", "30");
            return returnInfo;
        }
        user = userService.selectByName(username);
        if(user == null){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid username");
            returnInfo.put("errCode", "30");
            return returnInfo;
        }
        //如果为空，默认给予最低等级的权限
        if(roles.isEmpty()){
            roles.add("ROLE_NORMAL");
        }
        userService.updateRoles(user.getUid(), userService.selectRoles(roles));
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        returnInfo.put("errCode", "");
        return returnInfo;
    }


    private int getHighestRole(SysUser user) {
        int result = 0;
        for (SysRole role : user.getSysRoles()) {
            if (result < role.getRoleId()) result = role.getRoleId();
        }
        return result;
    }

    private boolean userHasAuthority(String authentication) {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (authentication.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }

        return false;
    }


}
