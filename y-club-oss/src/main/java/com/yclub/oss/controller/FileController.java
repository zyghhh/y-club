package com.yclub.oss.controller;

import com.yclub.oss.entity.Result;
import com.yclub.oss.service.FileService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-11  19:01
 */
@RestController
public class FileController {

    @Resource
    private FileService fileService;

//    @NacosValue(value = "${storage.service.type}",autoRefreshed = true)
//    private String storageType;


    @RequestMapping("/testGetAllBuckets")
    public String testGetAllBuckets() throws Exception {
//        List<String> allBucket = fileService.getAllBucket();
        List<String> allBucket = fileService.getAllBucket();
        return allBucket.get(0);
    }


    @RequestMapping("/getUrl")
    public String getUrl(String bucketName, String objectName) throws Exception {
        return fileService.getUrl(bucketName, objectName);
    }

    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile uploadFile, String bucket, String objectName) throws Exception {
        String url = fileService.uploadFile(uploadFile, bucket, objectName);
        return Result.ok(url);
    }

//    @RequestMapping("/download")
//    public Result download(String bucket, String objectName) throws Exception {
//        InputStream file = fileService.download(bucket,objectName);
//        return Result.ok(file);
//    }
}