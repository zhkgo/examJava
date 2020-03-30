package com.zhk.examonline.viewmodel.student.question.answer;

import com.zhk.examonline.viewmodel.admin.question.QuestionEditRequestVM;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperSubmitItemVM;
import lombok.Data;

@Data
public class QuestionAnswerVM {
    private QuestionEditRequestVM questionVM;
    private ExamPaperSubmitItemVM questionAnswerVM;
}
