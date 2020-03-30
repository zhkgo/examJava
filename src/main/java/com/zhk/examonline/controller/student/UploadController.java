package com.zhk.examonline.controller.student;



import com.zhk.examonline.base.BaseApiController;
import com.zhk.examonline.base.RestResponse;
import com.zhk.examonline.service.FileUpload;
import com.zhk.examonline.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


@Slf4j
@AllArgsConstructor
@RequestMapping("/api/student/upload")
@RestController("StudentUploadController")
public class UploadController extends BaseApiController {

    private final FileUpload fileUpload;
    private final UserService userService;


    @RequestMapping("/image")
    @ResponseBody
    public RestResponse questionUploadAndReadExcel(HttpServletRequest request) {
        System.out.println("here");
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        long attachSize = multipartFile.getSize();
        log.info("size is {}",attachSize);
        String imgName = multipartFile.hashCode()+multipartFile.getOriginalFilename();
        log.info(imgName);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String filePath = fileUpload.uploadFileByTencentCloud(inputStream, attachSize, imgName);
            userService.changePicture(getCurrentUser(), filePath);
            return RestResponse.ok(filePath);
        } catch (IOException e) {
            return RestResponse.fail(2, e.getMessage());
        }
    }


}
