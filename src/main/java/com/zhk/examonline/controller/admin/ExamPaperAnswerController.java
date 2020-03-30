package com.zhk.examonline.controller.admin;

import com.github.pagehelper.PageInfo;
import com.zhk.examonline.base.BaseApiController;
import com.zhk.examonline.base.RestResponse;
import com.zhk.examonline.domain.*;
import com.zhk.examonline.domain.enums.ExamPaperAnswerStatusEnum;
import com.zhk.examonline.event.CalculateExamPaperAnswerCompleteEvent;
import com.zhk.examonline.event.UserEvent;
import com.zhk.examonline.service.ExamPaperAnswerService;
import com.zhk.examonline.service.ExamPaperService;
import com.zhk.examonline.service.SubjectService;
import com.zhk.examonline.service.UserService;
import com.zhk.examonline.utility.DateTimeUtil;
import com.zhk.examonline.utility.ExamUtil;
import com.zhk.examonline.utility.PageInfoHelper;
import com.zhk.examonline.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.zhk.examonline.viewmodel.admin.exampaper.ExamPaperAnswerAPageVM;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperReadVM;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperSubmitVM;
import com.zhk.examonline.viewmodel.admin.exampaper.ExamPaperAnswerPageResponseVM;
import com.zhk.examonline.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController("AdminExamPaperAnswerController")
@RequestMapping(value = "/api/admin/exampaper")
@AllArgsConstructor
public class ExamPaperAnswerController extends BaseApiController {

    private final ExamPaperAnswerService examPaperAnswerService;
    private final ExamPaperService examPaperService;
    private final SubjectService subjectService;
    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<ExamPaperAnswerPageResponseVM>> pageList(@RequestBody @Valid ExamPaperAnswerAPageVM model) {
        PageInfo<ExamPaperAnswer> pageInfo = examPaperAnswerService.adminPage(model);
        PageInfo<ExamPaperAnswerPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            ExamPaperAnswerPageResponseVM vm = modelMapper.map(e, ExamPaperAnswerPageResponseVM.class);
            Subject subject = subjectService.selectById(vm.getSubjectId());
            vm.setDoTime(ExamUtil.secondToVM(e.getDoTime()));
            vm.setSystemScore(ExamUtil.scoreToVM(e.getSystemScore()));
            vm.setUserScore(ExamUtil.scoreToVM(e.getUserScore()));
            vm.setPaperScore(ExamUtil.scoreToVM(e.getPaperScore()));
            vm.setCreateUser(userService.getUserById(e.getCreateUser()).getUserName());
            vm.setSubjectName(subject.getName());
            vm.setCreateTime(DateTimeUtil.dateFormat(e.getCreateTime()));
            return vm;
        });
        return RestResponse.ok(page);
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResponse<String> edit(@RequestBody @Valid ExamPaperSubmitVM examPaperSubmitVM) {
        boolean notJudge = examPaperSubmitVM.getAnswerItems().stream().anyMatch(i -> i.getDoRight() == null && i.getScore() == null);
        if (notJudge) {
            return RestResponse.fail(2, "有未批改题目");
        }

        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.selectById(examPaperSubmitVM.getId());
        ExamPaperAnswerStatusEnum examPaperAnswerStatusEnum = ExamPaperAnswerStatusEnum.fromCode(examPaperAnswer.getStatus());
        if (examPaperAnswerStatusEnum == ExamPaperAnswerStatusEnum.Complete) {
            return RestResponse.fail(3, "试卷已完成");
        }
        String score = examPaperAnswerService.judge(examPaperSubmitVM);
        User user = getCurrentUser();
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), user.getRealName(), new Date());
        String content = user.getUserName() + " 批改试卷：" + examPaperAnswer.getPaperName() + " 得分：" + score;
        userEventLog.setContent(content);
        eventPublisher.publishEvent(new UserEvent(userEventLog));
        return RestResponse.ok(score);
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.POST)
    public RestResponse<ExamPaperReadVM> read(@PathVariable Integer id) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.selectById(id);
        ExamPaperReadVM vm = new ExamPaperReadVM();
        ExamPaperEditRequestVM paper = examPaperService.examPaperToVM(examPaperAnswer.getExamPaperId());
        ExamPaperSubmitVM answer = examPaperAnswerService.examPaperAnswerToVM(examPaperAnswer.getId());
        vm.setPaper(paper);
        vm.setAnswer(answer);
        return RestResponse.ok(vm);
    }


}
