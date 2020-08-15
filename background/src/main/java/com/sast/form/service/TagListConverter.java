package com.sast.form.service;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.sast.material.pojo.SysTag;

import java.util.ArrayList;
import java.util.Arrays;

public class TagListConverter  implements Converter<ArrayList<SysTag>> {
    @Override
    public Class<ArrayList> supportJavaTypeKey() {
        return ArrayList.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public ArrayList<SysTag> convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        if(cellData.getStringValue().equals("")) return new ArrayList<SysTag>();
        ArrayList<String> tags = (ArrayList<String>) Arrays.asList(cellData.getStringValue().split(","));
        ArrayList<SysTag> tagList = new ArrayList<>();
        for(String tag : tags){
            SysTag tempTag = new SysTag();
            tempTag.setTagName(tag);
            tagList.add(tempTag);
        }
        return tagList;
    }

    @Override
    public CellData<String> convertToExcelData(ArrayList<SysTag> arrayList, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        if(arrayList.isEmpty()) return new CellData<>("");
        StringBuilder sb = new StringBuilder();
        for(SysTag tag : arrayList){
            sb.append(tag.getTagName());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return new CellData<>(sb.toString());
    }
}
