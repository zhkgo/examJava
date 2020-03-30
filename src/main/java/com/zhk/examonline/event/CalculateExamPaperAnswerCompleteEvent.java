package com.zhk.examonline.event;

import com.zhk.examonline.domain.ExamPaperAnswerInfo;
import org.springframework.context.ApplicationEvent;


public class CalculateExamPaperAnswerCompleteEvent extends ApplicationEvent {


    private final ExamPaperAnswerInfo examPaperAnswerInfo;


    public CalculateExamPaperAnswerCompleteEvent(final ExamPaperAnswerInfo examPaperAnswerInfo) {
        super(examPaperAnswerInfo);
        this.examPaperAnswerInfo = examPaperAnswerInfo;
    }

    public ExamPaperAnswerInfo getExamPaperAnswerInfo() {
        return examPaperAnswerInfo;
    }

}
