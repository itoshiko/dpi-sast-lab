package com.sast.notice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.notice.mapper.SysNoticeMapper;
import com.sast.notice.pojo.SysNotice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class NoticeService {

    @Resource
    SysNoticeMapper sysNoticeMapper;

    @Resource
    ObjectMapper mapper;


    public boolean addNotice(String noticeJSON) throws JsonProcessingException {
        SysNotice notice = mapper.readValue(noticeJSON, SysNotice.class);
        int result = sysNoticeMapper.addNotice(notice);
        if (result == 0) return false;
        else return true;
    }

    public boolean deleteNotice(int id){
        int result = sysNoticeMapper.deleteById(id);
        if (result == 0) return false;
        else return true;
    }

    public String searchNotice(String nameReg) throws JsonProcessingException {
        ArrayList<SysNotice> sysNoticeList = sysNoticeMapper.selectByName(nameReg);
        return mapper.writeValueAsString(sysNoticeList);
    }

    public String showPublicNotice() throws JsonProcessingException {
        ArrayList<SysNotice> sysNoticeList = sysNoticeMapper.selectByIsPublic(true);
        return mapper.writeValueAsString(sysNoticeList);
    }

    public String showPrivateNotice() throws JsonProcessingException {
        ArrayList<SysNotice> sysNoticeList = sysNoticeMapper.selectByIsPublic(false);
        return mapper.writeValueAsString(sysNoticeList);
    }


}
