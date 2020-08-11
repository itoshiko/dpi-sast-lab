package com.sast.material.service;

import com.sast.material.mapper.SysMaterialMapper;
import com.sast.material.pojo.SysMaterial;
import com.sast.material.pojo.SysTag;
import com.sast.user.pojo.SysRole;
import com.sast.user.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

    public ArrayList<SysMaterial> selectMaterial(HashMap<String, Object> request) {
        HashMap<String, Object> conditions = new HashMap<String, Object>();
        if (request.get("name") != null && ((String)request.get("name")).length() > 0) {
            conditions.put("name", request.get("name"));
        }
        if (request.get("classification") != null && ((String)request.get("classification")).length() > 0) {
            conditions.put("classification", request.get("classification"));
        }
        if (request.containsKey("priceHigh")) {
            conditions.put("priceHigh", request.get("priceHigh"));
        }
        if (request.containsKey("priceLow")) {
            conditions.put("priceLow", request.get("priceLow"));
        }
        if (request.containsKey("isLoanable")) {
            conditions.put("isLoanable", request.get("isLoanable"));
        }
        if (request.containsKey("needReview")) {
            conditions.put("needReview", request.get("needReview"));
        }
        if (request.containsKey("dateHigh")) {
            conditions.put("warehousingDateHigh", request.get("dateHigh"));
        }
        if (request.containsKey("dateLow")) {
            conditions.put("warehousingDateLow", request.get("dateLow"));
        }
        if (request.containsKey("remained")) {
            conditions.put("remained", request.get("remained"));
        }
        ArrayList<SysMaterial> result = materialMapper.selectByConditions(conditions);
        if(request.containsKey("tags")){
            ArrayList<String> tags = (ArrayList<String>)request.get("tags");
            SysMaterial tempMaterial;
            for (Iterator<SysMaterial> iterator = result.iterator(); iterator.hasNext(); ) {
                tempMaterial = iterator.next();
                if (!hasTag(tempMaterial, tags)) {
                    iterator.remove();
                }
            }
        }
        return result;
    }

    public SysMaterial selectById(int id){
        return materialMapper.selectById(id);
    }

    private boolean hasTag(SysMaterial material, ArrayList<String> tags) {
        for (SysTag tag : material.getTags()) {
            for (String tagName : tags) {
                if (tagName.equalsIgnoreCase(tag.getTagName())) return true;
            }
        }
        return false;
    }

    public void updateMaterial(SysMaterial material) {
        materialMapper.updateMaterial(material);
        materialMapper.deleteTagsByMaterialId(material.getId());
        materialMapper.addTagsByMaterialId(material.getId(), material.getTags());
    }

    public void deleteMaterialById(int id) {
        materialMapper.deleteMaterialById(id);
        materialMapper.deleteTagsByMaterialId(id); //虽然有级联删除，但是感觉还是这样子保险一点
    }

    public void addMaterial(SysMaterial material) {
        materialMapper.addMaterial(material);
        materialMapper.addTagsByMaterialId(material.getId(), material.getTags());
    }


}
