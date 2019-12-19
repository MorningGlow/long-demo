package com.luxshare.demo.mail;

import com.luxshare.demo.DemoApplication;
import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.SmbFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件测试
 *
 * @author lion hua
 * @since 2019-11-29
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class MailTest {

    @Autowired
    private EmailTest emailTest;

    @Test
    public void test() throws MessagingException, IOException {
//        emailTest.sendSimpleMail();
        emailTest.sendComplexMail();
    }

    @Test
    public void test2() throws IOException, MessagingException {

        StringBuilder sb = new StringBuilder();
        sb.append("<h3>For 客户</h3>")
                .append("<p>Dear all:</p>")
                .append("<p>  This X1454 Yield Report is automatically generated by IT_MES system.</p>")
                .append("<p>For data issue,please contact:Arther,Arther.xia@Luxshare-ict.com.</p>")
                .append("<p>For system issue,please contact:Timy,Timy.zhu@Luxshare-ict.com.</p>")
                .append("<p></p>")
                .append("<h3>For ICT：</h3>")
                .append("<p>  This LA0Gb/s Yield Report is automatically generated by IT_MES system.</p>")
                .append("For data issue,please contact:Arther,Arther.xia@Luxshare-ict.com.</p>")
                .append("<p>For system issue,please contact:Timy,Timy.zhu@Luxshare-ict.com.</p>");

        MailEntity mailEntity = new MailEntity();

        mailEntity.setFrom("BU22MES@Luxshare-ict.com")
                .setTo(new ArrayList<String>() {
                    {
                        add("Lion.Hua@luxshare-ict.com");
                        add("Kevin.lee@luxshare-ict.com");
                    }
                })
                .setSubject("标题：发送Html内容")
                .setSb(sb);
//        emailTest.sendMailTest(mailEntity, "smb://mes:luxshare@10.32.36.230/mes共享盘/SPARK/", "Dashboard.html");
        emailTest.sendMailTest(mailEntity, null, null, null);
    }

    @Test
    public void test3() throws IOException, MessagingException {

        StringBuilder sb = new StringBuilder();
        sb.append("<h3>For 客户</h3>")
                .append("<p>Dear all:</p>")
                .append("<p>  This X1454 Yield Report is automatically generated by IT_MES system.</p>")
                .append("<p>For data issue,please contact:Arther,Arther.xia@Luxshare-ict.com.</p>")
                .append("<p>For system issue,please contact:Timy,Timy.zhu@Luxshare-ict.com.</p>")
                .append("<p></p>")
                .append("<h3>For ICT：</h3>")
                .append("<p>  This LA0Gb/s Yield Report is automatically generated by IT_MES system.</p>")
                .append("For data issue,please contact:Arther,Arther.xia@Luxshare-ict.com.</p>")
                .append("<p>For system issue,please contact:Timy,Timy.zhu@Luxshare-ict.com.</p>");

        MailEntity mailEntity = new MailEntity();

        mailEntity.setFrom("BU22MES@Luxshare-ict.com")
                .setTo(new ArrayList<String>() {
                    {
                        add("Lion.Hua@luxshare-ict.com");
                        add("Kevin.lee@luxshare-ict.com");
                    }
                })
                .setSubject("标题：发送Html内容")
                .setSb(sb);

        SmbUtil.ShareProperties shareProperties = SmbUtil.defaultShareProperties();
        CIFSContext context = SmbUtil.withNTLMCredentials(SingletonContext.getInstance(), shareProperties);
        List<SmbFile> smbFileList = SmbUtil.getAllByDir("smb://" + shareProperties.getServerName() + "/" + shareProperties.getShareRoot() + "/", context);
        for (SmbFile smbFile : smbFileList) {
            if (smbFile.isFile()) {
                SmbUtil.ShareProperties share = SmbUtil.defaultShareProperties();
                emailTest.sendMailTest(mailEntity, share.setFilePath(smbFile.getName()), context, smbFile);
            }
        }
    }
}
