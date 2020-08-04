package com.sast.notice.mapper;

import com.sast.notice.pojo.SysNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Mapper
@Repository
public interface SysNoticeMapper {

    ArrayList<SysNotice> selectByName(@Param("name") String nameReg);

    ArrayList<SysNotice> selectByConditions(HashMap<String, Object> conditions);

    ArrayList<SysNotice> selectPinned();

    ArrayList<SysNotice> selectByIsPublic(@Param("isPublic") boolean isPublic);

    int addNotice(SysNotice notice);

    int deleteById(@Param("id") int id);

    int update(SysNotice notice);


}
