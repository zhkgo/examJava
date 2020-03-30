package com.zhk.examonline.domain.excel;

import lombok.Data;

@Data
public class ShortAnswer {
    private String gradeLevel;
    private String subjectId;
    private String title;
    private String correct;
    private String analyze;
    private String score;
    private Integer difficult;
    private String result;
    private Integer id;
}
