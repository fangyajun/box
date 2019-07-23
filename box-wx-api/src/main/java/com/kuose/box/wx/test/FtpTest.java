package com.kuose.box.wx.test;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author fangyajun
 * @description
 * @since 2019/7/14
 */
public class FtpTest {

    public static void testFtp() throws Exception {
        // 1、连接ftp服务器
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("192.168.5.176", 21);
        // 2、登录ftp服务器
        ftpClient.login("henduomaoa", "kuose123");
        // 3、读取本地文件（获取本地文件的地址后，用于之后的上传）
        FileInputStream inputStream = new FileInputStream(new File("D:/images/u=1718395925,3485808025&fm=26&gp=0.jpg"));
        // 4、上传文件
        // 1）指定上传目录
        ftpClient.changeWorkingDirectory("/home/eleuser/www/images/");
        // 2）指定文件类型
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        // 第一个参数：文件在远程服务器的名称
        // 第二个参数：文件流
        ftpClient.storeFile("hello.jpg", inputStream);
        // 5、退出登录
        ftpClient.logout();
    }

    public static void main(String[] args) throws Exception {
        testFtp();
    }
}
