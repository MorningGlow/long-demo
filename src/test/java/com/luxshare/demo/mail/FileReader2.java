package com.luxshare.demo.mail;

import jcifs.smb.SmbFile;

public class FileReader2 {
    public static void main(String[] args) throws Exception {
        //smb://xxx:xxx@192.168.2.188/testIndex/
        //xxx:xxx是共享机器的用户名密码
        String url="smb://TableauQ:Luxshare2020@10.32.36.41/ReportShare/TableauQ/";
        SmbFile file = new SmbFile(url);
        if(file.exists()){
            SmbFile[] files = file.listFiles();
            for(SmbFile f : files){
                System.out.println(f.getName());
            }
        }
    }
}
