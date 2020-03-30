package com.zhk.examonline.viewmodel.admin.exam;

import com.zhk.examonline.viewmodel.admin.question.QuestionEditRequestVM;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ExamPaperTitleItemVM {

    @NotBlank(message = "标题内容不能为空")
    private String name;

    @Size(min = 1,message = "请添加题目")
    @Valid
    private List<QuestionEditRequestVM> questionItems;
}
