package com.sast.material.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sast.material.pojo.*;
import com.sast.material.service.MaterialExtraService;
import com.sast.material.service.MaterialService;
import com.sast.user.utils.DateUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class MaterialController {

    @Resource
    MaterialService materialService;
    @Resource
    MaterialExtraService materialExtraService;
    @Resource
    ObjectMapper mapper;

    @GetMapping("/materials")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String selectMaterial(@RequestBody HashMap<String, Object> map) {
        ArrayList<SysMaterial> materials = materialService.selectMaterial(map);
        try {
            return mapper.writeValueAsString(materials);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    @GetMapping("/materials/detail/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String selectMaterialDetail(@PathVariable("id") int id) {
        SysMaterial material = materialService.selectById(id);
        SysMaterialExtra materialExtra = materialExtraService.getMaterialExtra(id);
        ArrayList<String> imgList = new ArrayList<>();
        ArrayList<String> docList = new ArrayList<>();
        for (SysMaterialImg img : materialExtra.getImg()) {
            imgList.add(img.getImgUUID());
        }
        for (SysMaterialDoc doc : materialExtra.getDoc()) {
            docList.add(doc.getDocUUID());
        }
        try {
            ObjectNode root = mapper.valueToTree(material);
            ArrayNode imgListNode = mapper.valueToTree(imgList);
            ArrayNode docListNode = mapper.valueToTree(docList);
            root.putArray("imgList").addAll(imgListNode);
            root.putArray("docList").addAll(docListNode);
            root.put("warehousingDate", DateUtil.getStringTime(material.getWarehousingDate()));
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    @GetMapping("/materials/detail-extra/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String selectMaterialExtraInfo(@PathVariable("id") int id) {
        try {
            return mapper.writeValueAsString(materialExtraService.getMaterialExtra(id));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @PostMapping("/materials/delete")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String deleteMaterial(@RequestBody HashMap<String, String> map) {
        try {
            return mapper.writeValueAsString(materialService.deleteMaterialById(Integer.parseInt(map.get("materialId"))));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @PostMapping("/materials/add")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String addMaterial(@RequestBody SysMaterial material) {
        try {
            return mapper.writeValueAsString(materialService.addMaterial(material));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @PostMapping("/materials/update")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String updateMaterial(@RequestBody SysMaterial material) {
        try {
            return mapper.writeValueAsString(materialService.updateMaterial(material));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @PostMapping("/materials/add-tag")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String addTag(@RequestBody HashMap<String, String> map) {
        try {
            SysTag newTag = new SysTag();
            newTag.setTagName(map.get("tagName"));
            return mapper.writeValueAsString(materialService.addTag(newTag));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @PostMapping("/materials/delete-tag")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String deleteTag(@RequestBody HashMap<String, Object> map) {
        try {
            return mapper.writeValueAsString(materialService.deleteTag((int) map.get("id")));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @PostMapping("/materials/modify-tag")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String modifyTag(@RequestBody SysTag tag){
        try {
            return mapper.writeValueAsString(materialService.modifyTagName(tag));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @PostMapping("/materials/search-tag")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public String searchTags(@RequestBody String keyword){
        try {
            return mapper.writeValueAsString(materialService.searchTags(keyword));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }
    }
}
