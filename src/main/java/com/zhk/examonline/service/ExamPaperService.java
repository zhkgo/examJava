package com.zhk.examonline.service;

import com.zhk.examonline.domain.ExamPaper;
import com.zhk.examonline.domain.User;
import com.zhk.examonline.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.zhk.examonline.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.zhk.examonline.viewmodel.admin.exampaper.ExamPaperRandomRequestVM;
import com.zhk.examonline.viewmodel.student.dashboard.PaperFilter;
import com.zhk.examonline.viewmodel.student.dashboard.PaperInfo;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperPageVM;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ExamPaperService extends BaseService<ExamPaper> {

    PageInfo<ExamPaper> page(ExamPaperPageRequestVM requestVM);

    PageInfo<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM);

    PageInfo<ExamPaper> studentPage(ExamPaperPageVM requestVM);

    ExamPaper savePaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, User user);

    ExamPaperEditRequestVM examPaperToVM(Integer id);

    List<PaperInfo> indexPaper(PaperFilter paperFilter);

    Integer selectAllCount();
    ExamPaperEditRequestVM generateRandom(ExamPaperRandomRequestVM model) throws Exception;
    List<Integer> selectMothCount();
}
