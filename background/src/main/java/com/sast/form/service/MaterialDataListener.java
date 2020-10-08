package com.sast.form.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisStopException;
import com.sast.form.exception.ColumnException;
import com.sast.form.pojo.ExcelUser;
import com.sast.material.pojo.SysMaterial;
import com.sast.material.service.MaterialService;
import com.sast.user.service.SysUserService;
import com.sast.user.utils.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Map;


public class MaterialDataListener extends AnalysisEventListener<SysMaterial> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataListener.class);

    MaterialService materialService;

    public MaterialDataListener(MaterialService materialService) {
        this.materialService = materialService;
    }

    ArrayList<SysMaterial> list = new ArrayList<SysMaterial>();

    @Override
    public void invoke(SysMaterial material, AnalysisContext analysisContext) {
        LOGGER.info("Data read: " + material.toString());
        list.add(material);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        LOGGER.info("Read finished");
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
        if(!headMap.containsValue("名称")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("ID")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("分类号")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("单价")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("是否可外借")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("外借是否需要审核")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("入库日期")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("总量")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("在库数量")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("存放位置")) throw new ColumnException("Invalid header!");
        if(!headMap.containsValue("标签")) throw new ColumnException("Invalid header!");
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        LOGGER.info("解析出错："+exception.getMessage());
        if(exception instanceof ColumnException) throw new ExcelAnalysisStopException("Invalid header!");
    }

    private void saveData() {
        LOGGER.info("{} data, begin to insert to DB", list.size());
        int userAdded = materialService.batchAddMaterial(list);
        LOGGER.info("Insertion success! {} added", userAdded);
    }

    public ArrayList<SysMaterial> getList() {
        return list;
    }
}

