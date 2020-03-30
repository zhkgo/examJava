package com.zhk.examonline.viewmodel.student.dashboard;

import lombok.Data;

@Data
public class TaskItemPaperVm {
    private Integer examPaperId;
    private String examPaperName;
    private Integer examPaperAnswerId;
    private Integer status;
}
