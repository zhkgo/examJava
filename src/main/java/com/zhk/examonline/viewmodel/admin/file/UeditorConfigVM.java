package com.zhk.examonline.viewmodel.admin.file;

import lombok.Data;

import java.util.List;

@Data
public class UeditorConfigVM {
    private String scrawlActionName;
    private String scrawlFieldName;
    private String imageActionName;
    private  String imageFieldName;
    private String scrawlUrlPrefix;
    private Long imageMaxSize;
    private List<String> imageAllowFiles;
    private boolean imageCompressEnable;
    private Integer imageCompressBorder;
    private String imageInsertAlign;
    private String imageUrlPrefix;
    private String imagePathFormat;
}
