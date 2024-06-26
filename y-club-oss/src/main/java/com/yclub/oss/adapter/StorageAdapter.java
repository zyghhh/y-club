package com.yclub.oss.adapter;

import com.yclub.oss.entity.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-11  19:02
 */
@Service
public interface StorageAdapter {
    /**
     * 创建bucket桶
     */
    public void createBucket(String bucket) ;

    /**
     * 上传文件
     */
    public void uploadFile(MultipartFile uploadFile, String bucket, String objectName) ;
    /**
     * 列出所有桶
     */
    public List<String> getAllBucket() ;

    /**
     * 列出当前桶及文件
     */
    public List<FileInfo> getAllFile(String bucket) ;

    /**
     * 下载文件
     */
    public InputStream downLoad(String bucket, String objectName) ;

    /**
     * 删除桶
     */
    public void deleteBucket(String bucket) ;

    /**
     * 删除文件
     */
    public void deleteObject(String bucket, String objectName) ;

    /**
     * 获取文件url
     */
    String getUrl(String bucket, String objectName);
}