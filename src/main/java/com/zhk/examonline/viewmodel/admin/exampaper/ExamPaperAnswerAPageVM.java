package com.zhk.examonline.viewmodel.admin.exampaper;

import com.zhk.examonline.base.BasePage;
import lombok.Data;

@Data
public class ExamPaperAnswerAPageVM extends BasePage {

    private Integer subjectId;
    private Integer status;
    private Integer createUser;

}
