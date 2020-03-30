package com.zhk.examonline.service;



import java.io.InputStream;

public interface FileUpload {

    String uploadFile(InputStream inputStream, long size, String extName);
    String uploadFileByTencentCloud(InputStream inputStream,long size,String extName);
}
