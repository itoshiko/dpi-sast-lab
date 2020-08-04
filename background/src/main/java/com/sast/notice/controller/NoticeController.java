package com.sast.notice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sast.notice.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class NoticeController {

    @Resource
    NoticeService noticeService;

    @PostMapping("/notice/name")
    @ResponseBody
    public String searchNoticeByName(String name){
        try {
            return noticeService.searchNotice(name);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @PostMapping("/notice/all")
    @ResponseBody
    public String showAllNotices(){
        try {
            return noticeService.searchNotice("");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "failed";
        }
    }
}
