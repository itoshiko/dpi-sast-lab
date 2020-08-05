package com.sast.user.service;

import com.sast.user.mapper.SysUserMapper;
import com.sast.user.pojo.SysRole;
import com.sast.user.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SysUserService {

    private SysUserMapper userMapper;

    @Autowired
    public void setUserMapper(SysUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SysUser selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public SysUser selectByName(String name) {
        return userMapper.selectByName(name);
    }

    public boolean isMailExisted(String mail) {
        return userMapper.selectByMail(mail) != null;
    }

    public ArrayList<SysRole> selectRoles(ArrayList<String> roles) {
        ArrayList<SysRole> rolesList = new ArrayList<>();
        if (!(roles == null || roles.isEmpty())) {
            for (String role : roles) {
                rolesList.add(userMapper.selectRoleByName(role));
            }
        }
        return rolesList;
    }

    public boolean isStudentIdExisted(String studentId) {
        return userMapper.selectByStudentId(studentId) != null;
    }

    public boolean isUsernameExisted(String username) {
        return userMapper.selectByName(username) != null;
    }

    public void updateUsernameById(int id, String username) {
        userMapper.updateUserNameById(id, username);
    }

    public void addUser(SysUser sysUser) {
        userMapper.addUser(sysUser);
        userMapper.addRoleByUserId(sysUser.getUid(), sysUser.getSysRoles());
    }

    public void deleteUserById(int id) {
        userMapper.deleteUserById(id);
    }
}
