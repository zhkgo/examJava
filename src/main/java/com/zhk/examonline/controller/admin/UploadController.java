package com.zhk.examonline.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.zhk.examonline.base.BaseApiController;
import com.zhk.examonline.base.RestResponse;
import com.zhk.examonline.configuration.property.SystemConfig;
import com.zhk.examonline.domain.excel.*;
import com.zhk.examonline.listener.*;
import com.zhk.examonline.repository.SubjectMapper;
import com.zhk.examonline.service.FileUpload;
import com.zhk.examonline.service.QuestionService;
import com.zhk.examonline.utility.QuestionParserUtil;
import com.zhk.examonline.viewmodel.admin.file.UeditorConfigVM;
import com.zhk.examonline.viewmodel.admin.file.UploadResultVM;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


@Slf4j
@AllArgsConstructor
@RequestMapping("/api/admin/upload")
@RestController("AdminUploadController")
public class UploadController extends BaseApiController {

    private final FileUpload fileUpload;
    private final SystemConfig systemConfig;
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final String IMAGE_UPLOAD = "imgUpload";
    private static final String IMAGE_UPLOAD_FILE = "upFile";
    private QuestionService questionService;
    private SubjectMapper subjectMapper;
    @ResponseBody
    @RequestMapping("/configAndUpload")
    public Object upload(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        if (action.equals(IMAGE_UPLOAD)) {
            try {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                MultipartFile multipartFile = multipartHttpServletRequest.getFile(IMAGE_UPLOAD_FILE);
                long attachSize = multipartFile.getSize();
                String imgName = multipartFile.getOriginalFilename();
                String filePath;
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    filePath = fileUpload.uploadFileByTencentCloud(inputStream, attachSize, imgName);
                }
                String imageType = imgName.substring(imgName.lastIndexOf("."));
                UploadResultVM uploadResultVM = new UploadResultVM();
                uploadResultVM.setOriginal(imgName);
                uploadResultVM.setName(imgName);
                uploadResultVM.setUrl(filePath);
                uploadResultVM.setSize(multipartFile.getSize());
                uploadResultVM.setType(imageType);
                uploadResultVM.setState("SUCCESS");
                return uploadResultVM;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            UeditorConfigVM ueditorConfigVM = new UeditorConfigVM();
            ueditorConfigVM.setScrawlActionName("scrawlUpload");
            ueditorConfigVM.setScrawlFieldName("scrawl");
            ueditorConfigVM.setScrawlUrlPrefix("");
            ueditorConfigVM.setImageActionName(IMAGE_UPLOAD);
            ueditorConfigVM.setImageFieldName(IMAGE_UPLOAD_FILE);
            ueditorConfigVM.setImageMaxSize(2048000L);
            ueditorConfigVM.setImageAllowFiles(Arrays.asList(".png", ".jpg", ".jpeg", ".gif", ".bmp"));
            ueditorConfigVM.setImageCompressEnable(true);
            ueditorConfigVM.setImageCompressBorder(1600);
            ueditorConfigVM.setImageInsertAlign("none");
            ueditorConfigVM.setImageUrlPrefix("");
            ueditorConfigVM.setImagePathFormat("");
            return ueditorConfigVM;
        }
        return null;
    }
    @ResponseBody
    @RequestMapping("/excel")
    public Object uploadExcel(MultipartFile file) {
        QuestionParserUtil questionParserUtil=new QuestionParserUtil(subjectMapper);
        try{
            EasyExcel.read(file.getInputStream(), SingleChoice.class, new SingleDataListener(questionService,questionParserUtil,getCurrentUser().getId())).sheet(0).doRead();
            EasyExcel.read(file.getInputStream(), MutiChoice.class, new MutiDataListener(questionService,questionParserUtil,getCurrentUser().getId())).sheet(1).doRead();
            EasyExcel.read(file.getInputStream(), TrueFalse.class, new TrueFalseDataListener(questionService,questionParserUtil,getCurrentUser().getId())).sheet(2).doRead();
            EasyExcel.read(file.getInputStream(), GapFilling.class, new GapFillingListener(questionService,questionParserUtil,getCurrentUser().getId())).sheet(3).doRead();
            EasyExcel.read(file.getInputStream(), ShortAnswer.class, new ShortAnswerListener(questionService,questionParserUtil,getCurrentUser().getId())).sheet(4).doRead();
        }catch (IOException e){
            e.getMessage();
            return RestResponse.fail(600,"Excel存在错误");
        }
        return null;
    }

}
