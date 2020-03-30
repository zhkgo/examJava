package com.zhk.examonline;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QnTest {
    @Test
    public void testQn() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "peYeh8SN_kgNReWkBJW3oLcI9_2yvhPWoWGGIdC5";
        String secretKey = "P0eYJDzf99dOgZZZD1-dP59WoEIO35ekw40fxG3O";
        String bucket = "image";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);

        } catch (Exception ex) {
            System.out.print(ex.toString());
        }
    }
}
