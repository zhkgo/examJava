package com.zhk.examonline.viewmodel.admin.question;

import com.zhk.examonline.viewmodel.BaseVM;
import lombok.Data;

@Data
public class QuestionResponseVM extends BaseVM {

    private Integer id;

    private Integer questionType;

    private Integer textContentId;

    private String createTime;

    private Integer subjectId;

    private Integer createUser;

    private String score;

    private Integer status;

    private String correct;

    private Integer analyzeTextContentId;

    private Integer difficult;

    private String shortTitle;

}
