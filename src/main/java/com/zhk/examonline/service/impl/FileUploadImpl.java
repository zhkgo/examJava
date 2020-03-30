package com.zhk.examonline.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qiniu.storage.Region;
import com.zhk.examonline.configuration.property.QCloudConfig;
import com.zhk.examonline.configuration.property.QnConfig;
import com.zhk.examonline.configuration.property.SystemConfig;
import com.zhk.examonline.service.FileUpload;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@AllArgsConstructor
public class FileUploadImpl implements FileUpload {
    private final Logger logger = LoggerFactory.getLogger(FileUpload.class);
    private final SystemConfig systemConfig;

    @Override
    public String uploadFile(InputStream inputStream, long size, String extName) {
        QnConfig qnConfig = systemConfig.getQn();
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qnConfig.getAccessKey(), qnConfig.getSecretKey());
        String upToken = auth.uploadToken(qnConfig.getBucket());
        System.out.print(upToken);
        try {
            Response response = uploadManager.put(inputStream, null, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return qnConfig.getUrl() + "/" + putRet.key;
        } catch (QiniuException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadFileByTencentCloud(InputStream inputStream, long size, String extName) {
        QCloudConfig qcloud=systemConfig.getQcloud();
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = qcloud.getSecretId();
        String secretKey = qcloud.getSecretKey();
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        com.qcloud.cos.region.Region region = new com.qcloud.cos.region.Region(qcloud.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosclient = new COSClient(cred, clientConfig);
        String baseurl=qcloud.getUrl();
        // 指定要上传到的存储桶
        String bucketName = qcloud.getBucketName();
        // 指定要上传到 COS 上对象键
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketName, extName, inputStream, objectMetadata);
        try {
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
            String etag = putObjectResult.getETag();
            String url=baseurl+"/"+extName;
            return url;
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }finally {
            cosclient.shutdown();
        }
        return null;
    }
}
