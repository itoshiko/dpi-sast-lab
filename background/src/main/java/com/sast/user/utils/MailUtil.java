package com.sast.user.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MailUtil {

    static JavaMailSenderImpl mailSender;

    @Autowired
    public void setMailSender(JavaMailSenderImpl mailSender) {
        MailUtil.mailSender = mailSender;
    }

    public static void sendPassword(String target, String password) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("欢迎使用科协物资管理系统");
        helper.setText("这里来自精仪系科协的邮件。您已经成功注册，您的密码是: " + password);
        helper.setFrom("sastdpi@163.com");
        helper.setCc("sastdpi@163.com");
        helper.setTo(target);
        helper.addCc("sastdpi@163.com");
        mailSender.send(mimeMessage);
    }

    public static boolean isValidEmail(String email) {
        boolean isExist = false;
        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,4}");
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if (b) {
            isExist = true;
        }
        return isExist;
    }
}
