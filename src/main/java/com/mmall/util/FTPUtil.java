package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @program: mmall
 * @description: ftp服务器工具类
 * @author: xxxshi
 * @create: 2018-10-19 17:30
 * @Version: 1.0
 **/

public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPassword = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port,String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    /**
     * 开放的静态上传方法
     * @param fileList
     * @return
     * @throws IOException
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPassword);
        logger.info("开始上传文件");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("上传结束");
        return result;
    }



    /**
     * 内部逻辑，供公共的upload方法使用
     * @param remothPath
     * @param fileList
     * @return
     * @throws IOException
     */
    private boolean uploadFile(String remothPath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if (connectServer(this.getIp(), this.getPort(), this.getUser(), this.getPassword())) try {
            ftpClient.changeWorkingDirectory(remothPath);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("uft-8");
            //上传文件类型
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //本地被动模式
            ftpClient.enterLocalActiveMode();
            for (File file : fileList) {
                fis = new FileInputStream(file);
                ftpClient.storeFile(file.getName(), fis);
            }
        } catch (Exception e) {
            logger.error("上传文件异常" + e);
            uploaded = false;
        } finally {
            fis.close();
            ftpClient.disconnect();
        }
        return uploaded;

    }

    /**
     * 连接ftp服务器
     * @param ip
     * @param port
     * @param user
     * @param password
     * @return
     */
    private boolean connectServer(String ip, int port, String user, String password) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            ftpClient.login(user, password);
            isSuccess = true;
        } catch (IOException e) {
            logger.error("连接ftp服务器失败"+e);
        }
        return isSuccess;
    }



}

