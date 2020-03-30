package com.zhk.examonline.controller.student;

import com.zhk.examonline.base.BaseApiController;
import com.zhk.examonline.base.RestResponse;
import com.zhk.examonline.domain.ExamPaper;
import com.zhk.examonline.service.ExamPaperAnswerService;
import com.zhk.examonline.service.ExamPaperService;
import com.zhk.examonline.utility.DateTimeUtil;
import com.zhk.examonline.utility.PageInfoHelper;
import com.zhk.examonline.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperPageResponseVM;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperPageVM;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("StudentExamPaperController")
@RequestMapping(value = "/api/student/exam/paper")
@AllArgsConstructor
public class ExamPaperController extends BaseApiController {

    private final ExamPaperService examPaperService;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final ApplicationEventPublisher eventPublisher;


    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<ExamPaperEditRequestVM> select(@PathVariable Integer id) {
        ExamPaperEditRequestVM vm = examPaperService.examPaperToVM(id);
        return RestResponse.ok(vm);
    }


    @RequestMapping(value = "/pageList", method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamPaperPageResponseVM>> pageList(@RequestBody @Valid ExamPaperPageVM model) {
        PageInfo<ExamPaper> pageInfo = examPaperService.studentPage(model);
        PageInfo<ExamPaperPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamPaperPageResponseVM vm = modelMapper.map(e, ExamPaperPageResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }
}
