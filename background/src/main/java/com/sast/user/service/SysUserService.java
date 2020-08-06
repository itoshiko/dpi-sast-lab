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

    public SysUser selectByStudentId(String id) {
        return userMapper.selectByStudentId(id);
    }

    public String queryPasswordByUsername(String username){
        return userMapper.queryPasswordByUsername(username);
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

    public void updateUser(SysUser user) {
        userMapper.updateUserNameById(user.getUid(), user.getUsername());
        userMapper.updateMailById(user.getUid(), user.getMail());
        userMapper.updateRealNameById(user.getUid(), user.getRealName());
    }

    public void updatePassword(int id, String password){
        userMapper.updatePasswordById(id, password);
    }

    public void updateRoles(int id, ArrayList<SysRole> roles){
        userMapper.deleteRoleById(id);
        userMapper.addRoleByUserId(id, roles);

    }

    public void addUser(SysUser sysUser) {
        userMapper.addUser(sysUser);
        userMapper.addRoleByUserId(sysUser.getUid(), sysUser.getSysRoles());
    }

    public void deleteUserById(int id) {
        userMapper.deleteUserById(id);
    }
}
