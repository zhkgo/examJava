package com.zhk.examonline.viewmodel.admin.exampaper;

import lombok.Data;

@Data
public class ExamPaperRandomRequestVM {
    private Integer level;
    private Integer subjectId;
    private Integer singleChoice;
    private Integer multipleChoice;
    private Integer trueFalse;
    private Integer gapFilling;
    private Integer shortAnswer;
}
