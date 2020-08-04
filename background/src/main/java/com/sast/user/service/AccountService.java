package com.sast.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.user.mapper.SysUserMapper;
import com.sast.user.pojo.SysRole;
import com.sast.user.pojo.SysUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class AccountService {
    @Resource
    SysUserMapper userMapper;
    @Resource
    ObjectMapper mapper;


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


}
