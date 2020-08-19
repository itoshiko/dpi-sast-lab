package com.sast.user.mapper;

import com.sast.form.pojo.ExcelUser;
import com.sast.user.pojo.SysRole;
import com.sast.user.pojo.SysUser;
import com.sast.user.service.SysUserService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Mapper
@Repository
public interface SysUserMapper {

    SysUser selectById(@Param("id") Integer id);

    SysUser selectByName(@Param("username") String name);

    SysUser selectByMail(@Param("mail") String mail);

    SysUser selectByStudentId(@Param("id") String studentId);

    SysRole selectRoleByName(@Param("name") String name);

    ArrayList<SysRole> selectAllRoles();

    String queryPasswordByUsername(@Param("username") String username);

    ArrayList<SysUser> fuzzySearch(@Param("keyword") String keyword);

    ArrayList<SysUser> selectAll();

    void updateUserNameById(@Param("uid")int uid, @Param("userName")String userName);

    void updatePasswordById(@Param("uid")int uid, @Param("password")String EncryptedPassword);

    void updateMailById(@Param("uid")int uid, @Param("mail")String mail);

    void updateRealNameById(@Param("uid")int uid, @Param("realName")String realName);

    int addUser(SysUser sysUser);

    int batchAddUser(@Param("userList") ArrayList<ExcelUser> userList);

    int batchAddRole(@Param("roleList") ArrayList<SysUserService.IntPair> role);

    void addRoleByUserId(@Param("uid") int id, @Param("roles") ArrayList<SysRole> roles);

    void deleteUserById(@Param("uid")int uid);

    void deleteRoleById(@Param("uid") int uid);

    void disableUserById(@Param("uid") int uid);

    void enableUserById(@Param("uid") int uid);
}
