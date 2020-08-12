package com.sast.material.mapper;

import com.sast.material.pojo.SysMaterialImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface SysMaterialImgMapper {

    public ArrayList<SysMaterialImg> selectInfoById(@Param("mid")int mid);

    public SysMaterialImg selectInfo(@Param("id") int id);

    public void addInfo(@Param("mid")int mid, @Param("imgUUID") String imgUUID);

    public void deleteInfo(@Param("id")int id);

    public void deleteInfoByMaterial(@Param("mid")int mid);

    public void updateImgInfo(@Param("id")int id, @Param("imgUUID") String imgUUID);

}
