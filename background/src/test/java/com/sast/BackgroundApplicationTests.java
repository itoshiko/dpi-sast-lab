package com.sast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.material.pojo.SysMaterial;
import com.sast.material.pojo.SysTag;
import com.sast.material.service.MaterialService;
import com.sast.notice.pojo.PriorityEnum;
import com.sast.notice.pojo.SysNotice;
import com.sast.notice.service.NoticeService;
import com.sast.user.service.SysUserService;
import com.sast.user.service.AccountService;
import com.sast.user.utils.DateUtil;
import com.sast.user.utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@SpringBootTest
class BackgroundApplicationTests {

    @Resource
    SysUserService userService;
    @Resource
    MaterialService materialService;
    @Resource
    NoticeService noticeService;
    @Resource
    AccountService accountService;
    @Resource
    ObjectMapper mapper;

    @Test
    void contextLoads() {
    }

    // TODO: 2020/8/5 解决发送邮件问题
    @Test
    void mailTest() throws MessagingException {
        MailUtil.sendPassword("tuhanz@163.com", "shshudhweudhwe");
    }

    @Test
    void materialTest() {
        /*SysMaterial material = new SysMaterial();
        material.setId(6);
        material.setLoanable(true);
        material.setName("HC-05蓝牙模块");
        material.setClassification("00000XXX");
        material.setNeedReview(false);
        material.setPrice(20);
        material.setWarehousingDate(new Date());
        material.setTotal(100);
        material.setRemaining(70);
        material.setStorageLocation("NULL");
        ArrayList<SysTag> tags = new ArrayList<SysTag>();
        tags.add(new SysTag(3, "Wireless"));
        tags.add(new SysTag(4, "Bluetooth"));
        material.setTags(tags);
        System.out.println(material);
        materialService.updateMaterial(material);*/
        HashMap<String, Object> map = new HashMap<String, Object>();
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("Wireless");
        map.put("tags", tags);
        for(SysMaterial material : materialService.selectMaterial(map)){
            System.out.println(material);
        }
    }

    @Test
    void noticeTest() throws JsonProcessingException {
        SysNotice notice = new SysNotice();
        notice.setTitle("zznb4");
        notice.setContent("这是一个测试用的通知");
        notice.setAuthor("itoshiko");
        notice.setSubject("test");
        notice.setPublishTime(new Date());
        notice.setPublic(false);
        //notice.setPinned(true);
        notice.setDeleted(true);
        notice.setUpdateTime(new Date());
        notice.setPriority(PriorityEnum.UPMOST);
        noticeService.addNotice(mapper.writeValueAsString(notice));
    }

    @Test
    void noticeTest2() throws JsonProcessingException {
        System.out.println(noticeService.searchNotice("zz"));
    }

    @Test
    void accountsTest() throws JsonProcessingException {
        accountService.deleteAccount("tianshuo");
    }

    @Test
    void jsonTest() throws JsonProcessingException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        HashMap<String, String> received = new HashMap<String, String>();
        data.put("test", true);
        String json = mapper.writeValueAsString(data);
        received = mapper.readValue(json, HashMap.class);
        System.out.println(received);
    }


}
