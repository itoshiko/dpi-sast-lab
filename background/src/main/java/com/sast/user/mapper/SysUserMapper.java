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

    ArrayList<SysUser> fuzzySearch(@Param("keyword") String keyword);

    ArrayList<SysUser> selectAll();

    void updateUserNameById(@Param("uid")int uid, @Param("userName")String userName);

    void updatePasswordById(@Param("uid")int uid, @Param("password")String EncryptedPassword);

    int addUser(SysUser sysUser);

    void addRoleByUserId(@Param("uid") int id, @Param("roles") ArrayList<SysRole> roles);

    void deleteUserById(@Param("uid")int uid);
}
