package com.zhk.examonline.service;

import com.zhk.examonline.domain.ExamPaperAnswer;
import com.zhk.examonline.domain.ExamPaperAnswerInfo;
import com.zhk.examonline.domain.User;
import com.zhk.examonline.viewmodel.admin.exampaper.ExamPaperAnswerAPageVM;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperSubmitVM;
import com.zhk.examonline.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExamPaperAnswerService extends BaseService<ExamPaperAnswer> {

    /**
     * 学生考试记录分页
     *
     * @param requestVM 过滤条件
     * @return PageInfo<ExamPaperAnswer>
     */
    PageInfo<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM);
    PageInfo<ExamPaperAnswer> adminPage(ExamPaperAnswerAPageVM requestVM);
    /**
     * 计算试卷提交结果(不入库)
     *
     * @param examPaperSubmitVM
     * @param user
     * @return
     */
    ExamPaperAnswerInfo calculateExamPaperAnswer(ExamPaperSubmitVM examPaperSubmitVM, User user);

    ExamPaperAnswer getByPidUid(Integer pid, Integer uid);
    /**
     * 试卷批改
     * @param examPaperSubmitVM  examPaperSubmitVM
     * @return String
     */
    String judge(ExamPaperSubmitVM examPaperSubmitVM);

    /**
     * 试卷答题信息转成ViewModel 传给前台
     *
     * @param id 试卷id
     * @return ExamPaperSubmitVM
     */
    ExamPaperSubmitVM examPaperAnswerToVM(Integer id);


    Integer selectAllCount();

    List<Integer> selectMothCount();
}
