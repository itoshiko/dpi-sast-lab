package com.sast.material.mapper;

import com.sast.material.pojo.SysMaterialDoc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface SysMaterialDocMapper {

    public ArrayList<SysMaterialDoc> selectInfoById(@Param("mid") int mid);

    public SysMaterialDoc selectInfoByUUID(@Param("uuid") String uuid);

    public SysMaterialDoc selectInfo(@Param("id") int id);

    public void addInfo(@Param("mid") int mid, @Param("docUUID") String docUUID, @Param("docType") String docType, @Param("docName") String docName);

    public void deleteInfo(@Param("id") int id);

    public void deleteInfoByMaterial(@Param("mid") int mid);

    public void updateDocInfo(@Param("id") int id, @Param("docUUID") String docUUID, @Param("docType") String docType);
}
