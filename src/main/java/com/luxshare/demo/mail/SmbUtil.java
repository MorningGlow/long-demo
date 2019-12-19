package com.luxshare.demo.mail;

import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class SmbUtil {

    public static void main(String[] args) throws IOException {

        ShareProperties shareProperties = new ShareProperties();

        CIFSContext context = withNTLMCredentials(SingletonContext.getInstance(), shareProperties);
//        read(context, shareProperties);
    }


    public static void read(CIFSContext context, ShareProperties shareProperties, Path targetPath) throws IOException {
        long start = System.currentTimeMillis();
        SmbFileReader reader = new SmbFileReader();
        InputStream in = reader.readSmbFile(getShareRootURL(shareProperties) + shareProperties.getFilePath(), context);

        Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        // 流关闭
        in.close();
        long end = System.currentTimeMillis();
        log.info("Read File success,use time:" + (end - start) + "ms");
    }


    public static void write(CIFSContext context, ShareProperties shareProperties) throws IOException {
        long start = System.currentTimeMillis();
        String dir = getShareRootURL(shareProperties) + shareProperties.getBackup();
        LocalDateTime now = LocalDateTime.now();
        String format = "" + now.getYear() + now.getMonthValue() + now.getDayOfYear() + now.getHour() + now.getMinute() + now.getSecond();
        String targetPath = dir + File.separator + format + shareProperties.getFilePath();
        log.info("目标目录targetPath:" + targetPath);
//        SmbUtil.SmbFileWriter.createDirectory(dir, context);
        String source = getShareRootURL(shareProperties) + shareProperties.getFilePath();
        log.info("源文件source:" + source);
        boolean result = SmbUtil.SmbFileWriter.writeSmbFile(source, targetPath, context);
        log.info("Write File success:{}", result);
        long end = System.currentTimeMillis();
        log.info("Write File success,use time:{} {}", (end - start), "ms");
    }

    public static CIFSContext withNTLMCredentials(CIFSContext ctx, ShareProperties shareProperties) {
        return ctx.withCredentials(new NtlmPasswordAuthenticator(shareProperties.getDomain(),
                shareProperties.getUserName(), shareProperties.getPassword()));
    }


    public static void moveSmbFileToSmb(SmbFile source, String target, ShareProperties shareProperties) throws IOException {
        try (SmbFile targetFile = newSmbFile(target, shareProperties)) {
            if (targetFile.exists()) {
                targetFile.delete();
            }
            targetFile.createNewFile();
            writeSmbFile(source.getInputStream(), target, shareProperties);
            source.delete();
        }
    }

    public static SmbFile newSmbFile(String path, ShareProperties shareProperties) throws MalformedURLException {
        return new SmbFile(getShareRootURL(shareProperties) + shareProperties.getBackup() + "/" + path, build(shareProperties.getUserName(), shareProperties.getPassword()));
    }

    public static void writeSmbFile(InputStream in, String target, ShareProperties shareProperties) throws IOException {
        if (null != in && StringUtils.isNotEmpty(target)) {
            try (SmbFile file = newSmbFile(target, shareProperties)) {
                if (!file.exists()) {
                    try (SmbFile parent = newSmbFile(file.getParent(), shareProperties)) {
                        if (!parent.exists()) {
                            parent.mkdirs();
                        }
                        file.createNewFile();
                    }
                }
                write(in, file);
            }
        }
    }

    private static void write(InputStream in, SmbFile file) throws IOException {
        try (OutputStream os = file.getOutputStream()) {
            byte[] bytes = new byte[1024];
            while (in.read(bytes) != -1) {
                os.write(bytes);
            }
            in.close();
        }
    }

    private static CIFSContext build(String username, String password) {
        return SingletonContext.getInstance().withCredentials(new NtlmPasswordAuthenticator(username, password));
    }

    private static String getShareRootURL(ShareProperties shareProperties) {
        return "smb://" + shareProperties.getServerName() + "/" + shareProperties.getShareRoot() + "/";
    }

    static class SmbFileReader {
        public InputStream readSmbFile(String path, CIFSContext context) throws IOException {
            try (SmbFile file = new SmbFile(path, context)) {
                if (Objects.isNull(file) || !file.exists()) {
                    throw new FileNotFoundException(path);
                }
                return file.getInputStream();
            }
        }
    }


    static class SmbFileWriter {
        public static boolean writeSmbFile(String source, String target, CIFSContext context) throws IOException {
            if (StringUtils.isEmpty(source) || StringUtils.isEmpty(target)) {
                return false;
            }

            return writeSmbFile(Files.newInputStream(Paths.get(source)),
                    target, context);
        }

        static boolean writeSmbFile(InputStream in, String target, CIFSContext context) throws IOException {
            if (Objects.nonNull(in) && StringUtils.isNotEmpty(target)) {
                try (SmbFile file = new SmbFile(target, context)) {
                    try (SmbFile parent = new SmbFile(file.getParent(), context)) {
                        if (!parent.exists()) {
                            createDirectory(file.getParent(), context);
                        }
                        if (!file.exists()) {
                            file.createNewFile();
                            // 删除
                            file.delete();
                        }
                    }
                    try (OutputStream os = file.getOutputStream()) {
                        byte[] bytes = new byte[1024];
                        while (in.read(bytes) != -1) {
                            os.write(bytes);
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        static SmbFile createDirectory(String targetDir, CIFSContext context) throws MalformedURLException,
                CIFSException {
            try (SmbFile dir = new SmbFile(targetDir, context)) {
                dir.mkdir();
                return dir;
            }
        }
    }

    public static List<SmbFile> getAllByDir(String path, CIFSContext context) throws MalformedURLException, SmbException {
        SmbFile file = new SmbFile(path, context);
        List<SmbFile> list = new ArrayList<>();
        SmbFile[] smbFiles = file.listFiles();
        for (SmbFile smbFile : smbFiles) {
            list.add(smbFile);
        }
        return list;
    }

    public static List<String> getFileName(String path, CIFSContext context) throws MalformedURLException, SmbException {
        List<SmbFile> smbFiles = getAllByDir(path, context);
        List<String> list = new ArrayList<>();
        for (SmbFile file : smbFiles) {
            if (file.isFile()) {
                list.add(file.getName());
            }
        }
        return list;
    }

    public static ShareProperties defaultShareProperties() {
        return new ShareProperties();
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    static class ShareProperties {

//        public static final String DOMAIN = "";
//        public static final String USERNAME = "TableauQ";
//        public static final String PASSWORD = "Luxshare2020";
//        public static final String SHARE_ROOT = "ReportShare/TableauQ";
//        public static final String FILE_PATH = "test.xlsx";
//        public static final String SERVER_NAME = "10.32.36.41";

        private String domain = "";

        private String userName = "TableauQ";

        private String password = "Luxshare2020";

        private String shareRoot = "ReportShare/TableauQ";

        private String filePath = "test.xlsx";

        private String serverName = "10.32.36.41";

        /**
         * 备份地址
         */
        private String backup = "backup";

    }

}
