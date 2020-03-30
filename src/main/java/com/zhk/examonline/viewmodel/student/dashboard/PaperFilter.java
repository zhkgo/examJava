package com.zhk.examonline.viewmodel.student.dashboard;

import lombok.Data;

import java.util.Date;

@Data
public class PaperFilter {
    private Integer userId;
    private Date dateTime;
    private Integer examPaperType;
    private Integer gradeLevel;
}
