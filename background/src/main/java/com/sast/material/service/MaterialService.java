package com.sast.material.service;

import com.sast.material.mapper.SysMaterialMapper;
import com.sast.material.pojo.SysMaterial;
import com.sast.material.pojo.SysTag;
import com.sast.user.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

@Service
public class MaterialService {

    @Resource
    MaterialExtraService materialExtraService;
    @Resource
    RentalService rentalService;

    private SysMaterialMapper materialMapper;

    public SysMaterialMapper getMaterialMapper() {
        return materialMapper;
    }

    public boolean isMaterialIdExists(int id){
        return materialMapper.selectById(id) != null;
    }

    @Autowired
    public void setMaterialMapper(SysMaterialMapper materialMapper) {
        this.materialMapper = materialMapper;
    }

    public ArrayList<SysMaterial> selectMaterial(HashMap<String, Object> request) {
        HashMap<String, Object> conditions = new HashMap<>();
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
            conditions.put("warehousingDateHigh", DateUtil.formatSimpleDate((String) request.get("dateHigh")));
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

    public HashMap<String, String> updateMaterial(SysMaterial material) {
        HashMap<String, String> returnInfo = new HashMap<>();
        if(!isMaterialIdExists(material.getId())){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid id");
            return returnInfo;
        }
        try{
            materialMapper.updateMaterial(material);
            if(!material.getTags().isEmpty()){
                materialMapper.deleteTagsByMaterialId(material.getId());
                materialMapper.addTagsByMaterialId(material.getId(), material.getTags());
            }
        } catch(Exception e){
            e.printStackTrace();
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        return returnInfo;
    }

    public HashMap<String, String> deleteMaterialById(int id) {
        HashMap<String, String> returnInfo = new HashMap<>();
        if(!isMaterialIdExists(id)){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid id");
            return returnInfo;
        }
        try{
            materialExtraService.deleteDocInfoByMaterial(id);
            materialExtraService.deleteImgInfoByMaterial(id); //删除图片和文档信息
            materialMapper.deleteTagsByMaterialId(id); //虽然有级联删除，但是感觉还是这样子保险一点
            rentalService.deleteAllByMaterialId(id); //删除借还记录
            materialMapper.deleteMaterialById(id);
        } catch(Exception e){
            e.printStackTrace();
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        return returnInfo;
    }

    public HashMap<String, String> addMaterial(SysMaterial material) {
        HashMap<String, String> returnInfo = new HashMap<>();
        try{
            materialMapper.addMaterial(material);
            if(!material.getTags().isEmpty()){
                materialMapper.addTagsByMaterialId(material.getId(), material.getTags());
            }
        } catch(Exception e){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            returnInfo.put("id", "");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        returnInfo.put("id", String.valueOf(material.getId()));
        return returnInfo;
    }

    public int batchAddMaterial(ArrayList<SysMaterial> materialList){
        if (materialList.isEmpty()) return 0;
        ArrayList<SysMaterial> materialListFinal = new ArrayList<>();
        ArrayList<SysTag> materialTags = searchTags("");
        HashMap<String, Integer> tagMap = new HashMap<String, Integer>();
        for(SysTag tag : materialTags){
            tagMap.put(tag.getTagName(), tag.getTagId());
        }
        for(SysMaterial material : materialList){
            if(material.getName().isEmpty()||material.getName()==null) {
                continue;
            }
            if(material.getClassification().isEmpty()||material.getClassification()==null) {
                continue;
            }
            if(material.getPrice() == 0){
                material.setPrice(1);
            }
            if(material.getWarehousingDate() ==null) {
                material.setWarehousingDate(new Date());
            }
            // TODO: 17/09/2020 tag的处理：不存在的tag
            for(SysTag tag: material.getTags()){
                if(!tagMap.containsKey(tag.getTagName())){

                }
                tag.setTagId(tagMap.get(tag.getTagName()));
            }
            materialMapper.addMaterial(material);
            materialListFinal.add(material);
        }
        return materialListFinal.size();
    }

    public HashMap<String, String> addTag(SysTag tag){
        HashMap<String, String> returnInfo = new HashMap<>();
        if(!materialMapper.isTagExists(tag.getTagName()).isEmpty()){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "duplicated tag name");
            returnInfo.put("id", "");
            return returnInfo;
        }
        try{
            materialMapper.addTag(tag);
        } catch(Exception e){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            returnInfo.put("id", "");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        returnInfo.put("id", String.valueOf(tag.getTagId()));
        return returnInfo;
    }

    // TODO: 17/09/2020 删除关联的物品的tag
    public HashMap<String, String> deleteTag(int id){
        HashMap<String, String> returnInfo = new HashMap<String, String>();
        if(materialMapper.selectTagById(id) == null){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid id");
            return returnInfo;
        }
        try{
            materialMapper.deleteTag(id);
        } catch(Exception e){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        return returnInfo;
    }

    public HashMap<String, String> modifyTagName(SysTag tag){
        HashMap<String, String> returnInfo = new HashMap<String, String>();
        if(materialMapper.selectTagById(tag.getTagId()) == null){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid id");
            return returnInfo;
        }
        try{
            materialMapper.modifyTagName(tag);
        } catch(Exception e){
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "unexpected error");
            return returnInfo;
        }
        returnInfo.put("success", "true");
        returnInfo.put("errInfo", "");
        return returnInfo;
    }

    public ArrayList<SysTag> searchTags(String keyword){
        try{
            return materialMapper.searchTags(keyword);
        } catch(Exception e){
            return new ArrayList<SysTag>();
        }
    }

}
