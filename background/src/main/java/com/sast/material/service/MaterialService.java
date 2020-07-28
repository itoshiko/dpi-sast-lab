package com.sast.material.service;

import com.sast.material.mapper.SysMaterialMapper;
import com.sast.material.pojo.SysMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    private SysMaterialMapper materialMapper;

    public SysMaterialMapper getMaterialMapper() {
        return materialMapper;
    }

    @Autowired
    public void setMaterialMapper(SysMaterialMapper materialMapper) {
        this.materialMapper = materialMapper;
    }

    public void updateMaterial(SysMaterial material){
        materialMapper.updateMaterial(material);
        materialMapper.deleteTagsByMaterialId(material.getId());
        materialMapper.addTagsByMaterialId(material.getId(), material.getTags());
    }

    public void deleteMaterialById(int id){
        materialMapper.deleteMaterialById(id);
        materialMapper.deleteTagsByMaterialId(id); //虽然有级联删除，但是感觉还是这样子保险一点
    }

    public void addMaterial(SysMaterial material){
        materialMapper.addMaterial(material);
        materialMapper.addTagsByMaterialId(material.getId(), material.getTags());
    }


}
