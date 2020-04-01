package com.zhk.examonline.viewmodel.admin.exampaper;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
public class ExamPaperRandomRequestVM {
    @NotNull
    private Integer level;
    @NotNull
    private Integer subjectId;
    @NotNull
    private Integer singleChoice;
    @NotNull
    private Integer multipleChoice;
    @NotNull
    private Integer trueFalse;
    @NotNull
    private Integer gapFilling;
    @NotNull
    private Integer shortAnswer;
}
