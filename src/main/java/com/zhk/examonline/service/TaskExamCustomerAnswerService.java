package com.zhk.examonline.service;

import com.zhk.examonline.domain.ExamPaper;
import com.zhk.examonline.domain.ExamPaperAnswer;
import com.zhk.examonline.domain.TaskExamCustomerAnswer;

import java.util.Date;
import java.util.List;

public interface TaskExamCustomerAnswerService extends BaseService<TaskExamCustomerAnswer> {

    void insertOrUpdate(ExamPaper examPaper, ExamPaperAnswer examPaperAnswer, Date now);

    TaskExamCustomerAnswer selectByTUid(Integer tid, Integer uid);

    List<TaskExamCustomerAnswer> selectByTUid(List<Integer> taskIds, Integer uid);
}
