package com.mmall.service.impl;
/**
 *
 * Created by Administrator on 2018/10/19 0019.
 */

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @program: mmall
 * @description: 文件上传
 * @author: xxxshi
 * @create: 2018-10-19 16:55
 * @Version: 1.0
 **/
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "."+fileExtensionName;
        logger.info("开始上传文件，上传文件名{}，上传的路径{}，上传后的文件名{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            //文件上传tomcat服务器成功
            file.transferTo(targetFile);
            //文件上传ftp服务器成功
            //FTPUtil.uploadFile(Lists.newArrayList(targetFile));

        } catch (IOException e) {
            logger.error("文件上传失败"+e);
            return null;
        }
        return targetFile.getName();
    }

}
