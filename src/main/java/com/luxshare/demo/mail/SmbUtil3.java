package com.luxshare.demo.mail;

import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
public class SmbUtil3 {

    private static final Path TARGET_PATH = Paths.get(System.getProperty("user.dir"), "test.xlsx");
    private static final String SOURCE_PATH = "D:/SMB2-TEST/Spring.pdf";
    private static final String TARGET_DIR = "testDir";

    public static void main(String[] args) throws IOException {

        CIFSContext context = withNTLMCredentials(SingletonContext.getInstance());
        read(context);
        write(context);
    }

    private static void write(CIFSContext context) throws IOException {
        long start = System.currentTimeMillis();
        String dir = getShareRootURL() + TARGET_DIR;
        String targetPath = dir + File.separator + "Spring.pdf";
        SmbFileWriter.createDirectory(dir, context);
        boolean result = SmbFileWriter.writeSmbFile(SOURCE_PATH, targetPath, context);
        log.info("Write File success:{}", result);
        long end = System.currentTimeMillis();
        log.info("Write File success,use time:{} {}", (end - start), "ms");
    }

    public static void read(CIFSContext context) throws IOException {
        long start = System.currentTimeMillis();
        SmbFileReader reader = new SmbFileReader();
        InputStream in = reader.readSmbFile(getShareRootURL() + ShareProperties.FILE_PATH, context);

        Files.copy(in, TARGET_PATH, StandardCopyOption.REPLACE_EXISTING);
        long end = System.currentTimeMillis();
        log.info("Read File success,use time:" + (end - start) + "ms");
    }

    public static InputStream readR(CIFSContext context) throws IOException {
        SmbFileReader reader = new SmbFileReader();
        InputStream in = reader.readSmbFile(getShareRootURL() + ShareProperties.FILE_PATH, context);
        return in;
    }


    public static CIFSContext withNTLMCredentials(CIFSContext ctx) {
        return ctx.withCredentials(new NtlmPasswordAuthenticator(ShareProperties.DOMAIN,
                ShareProperties.USERNAME, ShareProperties.PASSWORD));
    }

    private static String getShareRootURL() {
        return "smb://" + ShareProperties.SERVER_NAME + "/" + ShareProperties.SHARE_ROOT + "/";
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
        static boolean writeSmbFile(String source, String target, CIFSContext context) throws IOException {
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

    class ShareProperties {

        public static final String DOMAIN = "";
        public static final String USERNAME = "TableauQ";
        public static final String PASSWORD = "Luxshare2020";
        public static final String SHARE_ROOT = "ReportShare/TableauQ";
        public static final String FILE_PATH = "test.xlsx";
        public static final String SERVER_NAME = "10.32.36.41";



//        public static final String USERNAME = "mes";
//        public static final String PASSWORD = "luxshare";
//
//        public static final String SHARE_ROOT = "mes共享盘/SPARK";
//        public static final String FILE_PATH = "副本新技術開發及精益生產處-09（MES）.xlsx";
//        public static final String SERVER_NAME = "10.32.36.230";
    }

}
