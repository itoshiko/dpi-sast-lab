package com.sast.form.service;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.sast.user.pojo.SysRole;

import java.util.ArrayList;
import java.util.Arrays;

public class RoleListConverter implements Converter<ArrayList<SysRole>> {
    @Override
    public Class<ArrayList> supportJavaTypeKey() {
        return ArrayList.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public ArrayList<SysRole> convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        ArrayList<String> roles = (ArrayList<String>)Arrays.asList(cellData.getStringValue().split(","));
        ArrayList<SysRole> userRoles = new ArrayList<SysRole>();
        for(String role : roles){
            SysRole tempRole = new SysRole();
            tempRole.setRoleName(role);
            userRoles.add(tempRole);
        }
        return userRoles;
    }

    @Override
    public CellData<String> convertToExcelData(ArrayList<SysRole> arrayList, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        StringBuilder sb = new StringBuilder();
        for(SysRole role : arrayList){
            sb.append(role.getRoleName());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return new CellData<>(sb.toString());
    }
}
