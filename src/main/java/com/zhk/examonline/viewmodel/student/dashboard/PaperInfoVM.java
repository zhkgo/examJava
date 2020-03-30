package com.zhk.examonline.viewmodel.student.dashboard;

import lombok.Data;

@Data
public class PaperInfoVM extends PaperInfo {
    private String startTime;
    private String endTime;
    private Integer status;
}
