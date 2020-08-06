package com.sast.user.mapper;

import com.sast.user.pojo.SysRole;
import com.sast.user.pojo.SysUser;
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

    String queryPasswordByUsername(@Param("username") String username);

    ArrayList<SysUser> fuzzySearch(@Param("keyword") String keyword);

    ArrayList<SysUser> selectAll();

    void updateUserNameById(@Param("uid")int uid, @Param("userName")String userName);

    void updatePasswordById(@Param("uid")int uid, @Param("password")String EncryptedPassword);

    void updateMailById(@Param("uid")int uid, @Param("mail")String mail);

    void updateRealNameById(@Param("uid")int uid, @Param("realName")String realName);

    int addUser(SysUser sysUser);

    void addRoleByUserId(@Param("uid") int id, @Param("roles") ArrayList<SysRole> roles);

    void deleteUserById(@Param("uid")int uid);

    void deleteRoleById(@Param("uid") int uid);
}
