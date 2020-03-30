package com.zhk.examonline.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@ConfigurationProperties(prefix = "system")
@Data
public class SystemConfig {
    private PasswordKeyConfig pwdKey;
    private List<String> securityIgnoreUrls;
    private WxConfig wx;
    private QnConfig qn;
    private QCloudConfig qcloud;
}
