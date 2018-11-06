package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * Created by Administrator on 2018/10/19 0019.
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
