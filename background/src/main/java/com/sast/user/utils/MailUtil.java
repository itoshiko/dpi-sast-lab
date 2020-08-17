package com.sast.user.utils;

import com.sast.material.controller.FileController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MailUtil {

    JavaMailSenderImpl mailSender;
    TemplateEngine templateEngine;
    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Autowired
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPassword(String target, String password) throws MessagingException {
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

    @Async
    public void thymeleafEmail(String from, String[] to, String subject, HashMap<String, String> emailParam, String template) throws MessagingException {
        long start = System.currentTimeMillis();
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper mimeMessageHelper
                = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        // 利用 Thymeleaf 模板构建 html 文本
        Context ctx = new Context();
        ctx.setVariable("emailParam", emailParam);
        ctx.setVariable("logo_pic", "logo_pic");
        ctx.setVariable("green_check_pic", "green_check_pic");
        ctx.setVariable("ma_pic", "ma_pic");
        // 执行模板引擎，执行模板引擎需要传入模板名、上下文对象
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/email/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(0);
        templateEngine.setTemplateResolver(templateResolver);
        String emailText = templateEngine.process(template, ctx);
        mimeMessageHelper.setText(emailText, true);
        File image1 = new File(Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("templates/img/logo_transparent.png")).getPath());
        File image2 = new File(Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("templates/img/icons-green-check.png")).getPath());
        File image3 = new File(Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("templates/img/ma.jpg")).getPath());
        InputStreamSource imageSource = new ByteArrayResource(File2byte(image1));
        mimeMessageHelper.addInline("logo_pic", imageSource, "image/png");
        imageSource = new ByteArrayResource(File2byte(image2));
        mimeMessageHelper.addInline("green_check_pic", imageSource, "image/png");
        imageSource = new ByteArrayResource(File2byte(image3));
        mimeMessageHelper.addInline("ma_pic", imageSource, "image/jpeg");
        mailSender.send(mimeMessage);
        long end = System.currentTimeMillis();
        logger.info("Mail sent to {} successfully, cost {} ms", to, end - start);
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

    public byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }
}
