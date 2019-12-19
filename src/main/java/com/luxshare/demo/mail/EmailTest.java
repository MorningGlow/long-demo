package com.luxshare.demo.mail;

import com.alibaba.fastjson.JSON;
import jcifs.CIFSContext;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 发送邮件测试
 * // D:\code\long-demo
 * log.info("user.dir:" + System.getProperty("user.dir"));
 *
 * @author lion hua
 * @since 2019-11-29
 */
@Component
@Slf4j
public class EmailTest {

    @Autowired
    private JavaMailSender javaMailSender;

    //    private static final String filePath = "\\\\10.32.36.230\\Tapworks\\TAPworks培训资料\\Report\\Tableau_教学文件.pdf";
    private static final String filePath = "\\\\10.32.36.230\\mes共享盘\\SPARK\\Dashboard.html";
    private static final String complexFilePath = "\\\\10.32.36.41\\ReportShare\\TableauQ\\test.xlsx";
//    private static final String filePath = "\\\\10.32.36.230\\Tapworks\\TAPworks培训资料\\Report\\LotTraceability DB Schema.xlsx";

    static {
        System.setProperty("mail.mime.splitlongparameters", "false");
    }


    public void sendSimpleMail() throws MessagingException {
        MimeMessage message;

        message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("BU22MES@Luxshare-ict.com");
        helper.setTo(new String[]{"Lion.Hua@luxshare-ict.com"});
        helper.setSubject("标题：发送Html内容");

        StringBuffer sb = new StringBuffer();
        sb.append("<h1>大标题-h1</h1>")
                .append("<p style='color:#F00'>红色字</p>")
                .append("<p style='text-align:right'>右对齐</p>");
        helper.setText(sb.toString(), true);
//        FileSystemResource fileSystemResource = new FileSystemResource(new File("C:\\Users\\12755167\\Desktop\\RPT_CR_ODST_LOT_TRACE.sql"));

//        File file = new File(filePath + "\\Tableau_教学文件.pdf");
        File file = new File(complexFilePath);
        File file2 = new File("\\\\10.32.36.41\\ReportShare\\TableauQ");
        String[] list = file2.list();
        log.info(JSON.toJSONString(list, true));
        log.info(file.getPath());
        if (file.exists()) {
            log.info("准备发送!" + " : " + filePath.substring(filePath.lastIndexOf(File.separator)));
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(file.getPath().substring(file.getPath().lastIndexOf(File.separator)), fileSystemResource);
            javaMailSender.send(message);
        } else {
            log.info("文件不存在!");
        }


    }

    public void sendComplexMail() throws MessagingException, IOException {
        MimeMessage message;

        message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("BU22MES@Luxshare-ict.com");
        helper.setTo(new String[]{"Lion.Hua@luxshare-ict.com"});
        helper.setSubject("标题：发送Html内容");

        StringBuffer sb = new StringBuffer();
        sb.append("<h1>大标题-h1</h1>")
                .append("<p style='color:#F00'>红色字</p>")
                .append("<p style='text-align:right'>右对齐</p>");
        helper.setText(sb.toString(), true);

        String filePath = "smb://mes:luxshare@10.32.36.230/mes共享盘/SPARK/";
        SmbFile file = new SmbFile(filePath);
        // D:\code\long-demo
        log.info("user.dir:" + System.getProperty("user.dir"));

        log.info(file.getPath());
        if (file.exists()) {
            SmbFile[] files = file.listFiles();
            SmbFile smbFile = Arrays.stream(files).filter(f -> "Dashboard.html".equals(f.getName())).findFirst().get();
            log.info("准备发送!" + " : " + smbFile.getName());

            SmbFileInputStream in = new SmbFileInputStream(smbFile);

            FileOutputStream out = new FileOutputStream(System.getProperty("user.dir") + "\\Dashboard.html");
            IOUtils.copy(in, out);
            FileSystemResource fileSystemResource = new FileSystemResource(System.getProperty("user.dir") + "\\Dashboard.html");
//            InputStreamResource inputStreamResource = new InputStreamResource(in);

            helper.addAttachment(smbFile.getName(), fileSystemResource);
            javaMailSender.send(message);
        } else {
            log.info("文件不存在!");
        }


    }

    public void sendMailTest(MailEntity mailEntity, SmbUtil.ShareProperties shareProperties, CIFSContext context, SmbFile smbFile) throws IOException, MessagingException {
        Path path = Paths.get(System.getProperty("user.dir"), shareProperties.getFilePath());
        if (path.toFile().exists()) {
            path.toFile().delete();
        }
        SmbUtil.read(context, shareProperties, path);
        // 直接删除
        SmbUtil.delete(smbFile);
        // 备份
//        SmbUtil.moveSmbFileToSmb(smbFile, smbFile.getName(), shareProperties);
        this.sendMail(mailEntity, new FileSystemResource(path));
    }

    public void sendMail(MailEntity mailEntity, FileSystemResource resource) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        // 发送人
        helper.setFrom(mailEntity.getFrom());
        // 发送给谁
        helper.setTo(mailEntity.getTo().toArray(new String[0]));
        // 邮件主题
        helper.setSubject(mailEntity.getSubject());
        // 邮件主体内容
        helper.setText(mailEntity.getSb().toString(), true);
        // 附件
        helper.addAttachment(Objects.requireNonNull(resource.getFilename()), resource);
        // 发送
        javaMailSender.send(message);
    }

    /**
     * 判断文件或文件夹是否存在
     *
     * @param path 文件或文件夹路径
     * @return boolean  true/false
     */
    public boolean isExist(String path) {
        return new File(path).exists();
    }

    /**
     * 判断需要登录认证的文件或文件夹是否存在
     *
     * @param smbPath smb 协议的路径
     * @return boolean true/false
     */
    public boolean isSmbExist(String smbPath) throws MalformedURLException, SmbException {
        return new SmbFile(smbPath).exists();
    }

    /**
     * 获取需要登录认证的文件的流
     *
     * @param smbPath smb 协议的路径
     * @return 路径存在, 则返回流, 否则返回null
     * @throws MalformedURLException exception
     * @throws SmbException          exception
     * @throws UnknownHostException  exception
     */
    public InputStream getSmbFile(String smbPath, String fileName) throws IOException {
        if (isSmbExist(smbPath)) {

//            SmbFileReader reader = new SmbFileReader();

            // 如果存在
            SmbFile file = new SmbFile(smbPath);
            Optional<SmbFile> optional = Arrays.stream(file.listFiles()).filter(f -> fileName.equals(f.getName())).findFirst();
            if (optional.isPresent()) {
                log.info("文件存在!" + optional.get().getPath());

                return optional.get().getInputStream();
            }
            return null;

        }
        // 不存在
        return null;
    }
}
