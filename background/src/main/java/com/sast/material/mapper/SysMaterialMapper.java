package com.sast.material.mapper;

import com.sast.material.pojo.SysMaterial;
import com.sast.material.pojo.SysTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface SysMaterialMapper {

    ArrayList<SysMaterial> selectByNameReg(@Param("nameReg") String nameReg);

    ArrayList<SysMaterial> selectByClassification(@Param("classification") String classReg);

    ArrayList<SysMaterial> selectByTag(@Param("tag") int tag);

    ArrayList<SysTag> getTagByMaterialId(@Param("id") int id);

    SysMaterial selectById(@Param("id") int id);

    int addMaterial(SysMaterial material);

    void addTagsByMaterialId(@Param("id") int id, @Param("tags") ArrayList<SysTag> tags);

    void deleteMaterialById(@Param("id") int id);

    void deleteTagsByMaterialId(@Param("id") int id);

    void updateMaterial(SysMaterial material);


}
