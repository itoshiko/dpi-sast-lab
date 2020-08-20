package com.sast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sast.form.service.FormService;
import com.sast.material.mapper.RentalMapper;
import com.sast.material.pojo.SysMaterial;
import com.sast.material.service.MaterialService;
import com.sast.material.service.RentalService;
import com.sast.notice.pojo.PriorityEnum;
import com.sast.notice.pojo.SysNotice;
import com.sast.notice.service.NoticeService;
import com.sast.user.service.AccountService;
import com.sast.user.service.SysUserService;
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
    RentalService rentalService;
    @Resource
    ObjectMapper mapper;
    @Resource
    MailUtil mailUtil;
    @Resource
    RentalMapper rentalMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void mailTest() throws MessagingException {
        String[] to = new String[1];
        to[0] = "tuhanz@163.com";
        HashMap<String, String> info = new HashMap<String, String>();
        info.put("username", "itoshiko");
        info.put("password", "cnusiYBC8");
        info.put("mail", "tuhanz@163.com");
        info.put("studentId", "2010010653");
        mailUtil.thymeleafEmail("sastdpi@163.com", to, "password", info, "register");
    }

    @Test
    void loanTest() {
        HashMap<String, String> request = new HashMap<String, String>();
        request.put("materialId", "12");
        request.put("count", "2");
        request.put("borrowerId", "1");
        request.put("expectedReturnDate", "2020-10-20 10:00:00");
        request.put("remark", "review test 2");
        System.out.println(rentalService.loanMaterial(request, "itoshiko"));
        /*request.put("loanId", "8");
        request.put("approve", "true");
        request.put("reviewerId", "2");
        System.out.println(rentalService.loanReview(request));*/
    }

    @Test
    void returnTest(){
        HashMap<String, String> request = new HashMap<String, String>();
        /*request.put("loadId", "6");
        request.put("numberReturned", "2");
        request.put("numberDamaged", "0");
        request.put("status", "INTACT");
        request.put("remark", "还回来了");
        System.out.println(rentalService.returnMaterial(request));*/
        //System.out.println(rentalMapper.selectReturnPending(666));
        request.put("approve", "true");
        request.put("returnId", "4");
        request.put("returnReviewerId", "1");
        System.out.println(rentalService.returnReview(request));
    }

    @Test
    void materialTest2(){
        System.out.println(rentalService.selectAllNotReturned());
        System.out.println(rentalService.selectNotReturnedByMaterialId(6));
        System.out.println(rentalService.selectNotReturnedByUserId(1));
        System.out.println(rentalService.allPendingLoan());
    }

    @Test
    void materialList(){
        SysMaterial material = materialService.selectById(6);
        material.setRemaining(material.getRemaining() - 10);
        materialService.updateMaterial(material);
        System.out.println(materialService.selectById(6));
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
        accountService.deleteAccount("tianshuo", 1);
    }

    @Test
    void jsonTest() throws JsonProcessingException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        HashMap received = new HashMap<String, String>();
        data.put("test", new Date());
        String json = mapper.writeValueAsString(data);
        System.out.println(json);
        received = mapper.readValue(json, HashMap.class);
        System.out.println(received);
        System.out.println(received.get("test").getClass());
    }

    @Test
    public void excel() {
    }


}
