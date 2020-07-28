package com.sast.user.service;

import com.sast.user.mapper.SysUserMapper;
import com.sast.user.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserService {

    private SysUserMapper userMapper;

    public SysUserMapper getUserMapper() {
        return userMapper;
    }

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
        if (userMapper.selectByMail(mail) != null) return false;
        else return true;
    }

    public boolean isStudentIdExisted(String studentId) {
        if (userMapper.selectByStudentId(studentId) != null) return false;
        else return true;
    }

    public boolean isUsernameExisted(String username) {
        if (userMapper.selectByName(username) != null) return false;
        else return true;
    }

    public void updateUsernameById(int id, String username){
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
