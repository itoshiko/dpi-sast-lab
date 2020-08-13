package com.sast.material.service;

import com.sast.material.mapper.SysMaterialDocMapper;
import com.sast.material.mapper.SysMaterialImgMapper;
import com.sast.material.pojo.SysMaterialDoc;
import com.sast.material.pojo.SysMaterialExtra;
import com.sast.material.pojo.SysMaterialImg;
import com.sast.material.pojo.enums.FileType;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Service
public class MaterialExtraService {

    @Resource
    SysMaterialImgMapper sysMaterialImgMapper;
    @Resource
    SysMaterialDocMapper sysMaterialDocMapper;
    @Resource
    MaterialService materialService;

    public ArrayList<SysMaterialImg> selectImgInfoById(int mid) {
        return sysMaterialImgMapper.selectInfoById(mid);
    }

    public void addImgInfo(SysMaterialImg img) {
        sysMaterialImgMapper.addInfo(img.getId(), img.getImgUUID());
    }

    public void deleteImgInfo(int id) {
        SysMaterialImg target = sysMaterialImgMapper.selectInfo(id);
        String path = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath();
        path = path + "static/img/materials/" + target.getImgUUID();
        File targetFile = new File(path);
        if(targetFile.delete()){
            sysMaterialImgMapper.deleteInfo(id);
        }
    }

    public void deleteImgInfoByMaterial(int mid) {
        ArrayList<SysMaterialImg> target = sysMaterialImgMapper.selectInfoById(mid);
        for(SysMaterialImg img : target){
            String path = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath();
            path = path + "static/img/materials/" + img.getImgUUID();
            File targetFile = new File(path);
            if(targetFile.delete()){
                sysMaterialImgMapper.deleteInfo(img.getId());
            }
        }
    }

    public void updateImgInfo(String imgUUID, int id) {
        if (sysMaterialImgMapper.selectInfo(id) != null) {
            sysMaterialImgMapper.updateImgInfo(id, imgUUID);
        }
    }

    public ArrayList<SysMaterialDoc> selectDocInfoById(int mid) {
        return sysMaterialDocMapper.selectInfoById(mid);
    }

    public SysMaterialDoc selectDocByUUID(String uuid) {
        return sysMaterialDocMapper.selectInfoByUUID(uuid);
    }

    public void addDocInfo(int mid, String docUUID, String docType, String docName) {
        sysMaterialDocMapper.addInfo(mid, docUUID, docType, docName);
    }

    public void deleteDocInfo(int id) {
        SysMaterialDoc target = sysMaterialDocMapper.selectInfo(id);
        String path = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath();
        path = path + "static/doc/materials/" + target.getDocUUID();
        File targetFile = new File(path);
        if(targetFile.delete()){
            sysMaterialDocMapper.deleteInfo(id);
        }
    }

    public void deleteDocInfoByMaterial(int mid) {
        ArrayList<SysMaterialDoc> target = sysMaterialDocMapper.selectInfoById(mid);
        for(SysMaterialDoc doc : target){
            String path = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath();
            path = path + "static/doc/materials/" + doc.getDocUUID();
            File targetFile = new File(path);
            if(targetFile.delete()){
                sysMaterialDocMapper.deleteInfo(doc.getId());
            }
        }
    }

    public void updateDocInfo(String imgUUID, String docType, int id) {
        if (sysMaterialDocMapper.selectInfo(id) != null) {
            sysMaterialDocMapper.updateDocInfo(id, imgUUID, docType);
        }
    }

    public SysMaterialExtra getMaterialExtra(int mid) {
        SysMaterialExtra sysMaterialExtra = new SysMaterialExtra();
        sysMaterialExtra.setMid(mid);
        sysMaterialExtra.setImg(sysMaterialImgMapper.selectInfoById(mid));
        sysMaterialExtra.setDoc(sysMaterialDocMapper.selectInfoById(mid));
        return sysMaterialExtra;
    }

    public HashMap<String, String> uploadMaterialFile(CommonsMultipartFile file, int materialId, FileType type) {
        HashMap<String, String> returnInfo = new HashMap<>();
        if (file.isEmpty()) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "empty file");
            return returnInfo;
        }
        if (!materialService.isMaterialIdExists(materialId)) {
            returnInfo.put("success", "false");
            returnInfo.put("errInfo", "invalid material id");
            return returnInfo;
        }
        String path = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        File realPath = null;
        if (type.equals(FileType.IMAGE)) {
            realPath = new File(path + "static/img/materials");
            if (!realPath.exists()) {
                realPath.mkdir();
                try {
                    file.transferTo(new File(realPath + "/" + uuid));
                    SysMaterialImg info = new SysMaterialImg();
                    info.setMid(materialId);
                    info.setImgUUID(uuid);
                    addImgInfo(info);
                    returnInfo.put("success", "true");
                    returnInfo.put("errInfo", "");
                    return returnInfo;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (type.equals(FileType.DOC)) {
            realPath = new File(path + "static/doc/materials");
            if (!realPath.exists()) {
                realPath.mkdir();
            }
            try {
                file.transferTo(new File(realPath + "/" + uuid));
                addDocInfo(materialId, uuid, file.getContentType(), file.getOriginalFilename());
                returnInfo.put("success", "true");
                returnInfo.put("uuid", uuid);
                returnInfo.put("errInfo", "");
                return returnInfo;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        returnInfo.put("success", "false");
        returnInfo.put("errInfo", "unexpected error");
        return returnInfo;
    }
}
