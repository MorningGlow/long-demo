package com.luxshare.demo.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 发送邮件测试
 *
 * @author lion hua
 * @since 2019-11-29
 */
@Component
public class EmailTest {

    @Autowired
    private JavaMailSender javaMailSender;


    public void sendSimpleMail() throws MessagingException {
        MimeMessage message;

        message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("BU22MES@Luxshare-ict.com");
        helper.setTo(new String[]{"Lion.Hua@luxshare-ict.com","Abbott.Hao@luxshare-ict.com"});
        helper.setSubject("标题：发送Html内容");

        StringBuffer sb = new StringBuffer();
        sb.append("<h1>大标题-h1</h1>")
                .append("<p style='color:#F00'>红色字</p>")
                .append("<p style='text-align:right'>右对齐</p>");
        helper.setText(sb.toString(), true);
        FileSystemResource fileSystemResource = new FileSystemResource(new File("C:\\Users\\12755167\\Desktop\\RPT_CR_ODST_LOT_TRACE.sql"));
        helper.addAttachment("电子发票", fileSystemResource);
        javaMailSender.send(message);

    }
}
