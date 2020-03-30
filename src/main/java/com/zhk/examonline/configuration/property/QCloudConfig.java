package com.zhk.examonline.configuration.property;

import lombok.Data;

@Data
public class QCloudConfig {
    private String secretId;
    private String secretKey;
    private String url;
    private String bucketName;
    private String region;
}
