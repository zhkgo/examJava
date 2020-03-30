package com.zhk.examonline.controller.student;

import com.zhk.examonline.base.BaseApiController;
import com.zhk.examonline.base.RestResponse;
import com.zhk.examonline.domain.ExamPaperQuestionCustomerAnswer;
import com.zhk.examonline.domain.Subject;
import com.zhk.examonline.domain.TextContent;
import com.zhk.examonline.domain.question.QuestionObject;
import com.zhk.examonline.service.ExamPaperQuestionCustomerAnswerService;
import com.zhk.examonline.service.QuestionService;
import com.zhk.examonline.service.SubjectService;
import com.zhk.examonline.service.TextContentService;
import com.zhk.examonline.utility.DateTimeUtil;
import com.zhk.examonline.utility.HtmlUtil;
import com.zhk.examonline.utility.JsonUtil;
import com.zhk.examonline.utility.PageInfoHelper;
import com.zhk.examonline.viewmodel.admin.question.QuestionEditRequestVM;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperSubmitItemVM;
import com.zhk.examonline.viewmodel.student.question.answer.QuestionAnswerVM;
import com.zhk.examonline.viewmodel.student.question.answer.QuestionPageStudentRequestVM;
import com.zhk.examonline.viewmodel.student.question.answer.QuestionPageStudentResponseVM;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("StudentQuestionAnswerController")
@RequestMapping(value = "/api/student/question/answer")
@AllArgsConstructor
public class QuestionAnswerController extends BaseApiController {

    private final ExamPaperQuestionCustomerAnswerService examPaperQuestionCustomerAnswerService;
    private final QuestionService questionService;
    private final TextContentService textContentService;
    private final SubjectService subjectService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<QuestionPageStudentResponseVM>> pageList(@RequestBody QuestionPageStudentRequestVM model) {
        model.setCreateUser(getCurrentUser().getId());
        PageInfo<ExamPaperQuestionCustomerAnswer> pageInfo = examPaperQuestionCustomerAnswerService.studentPage(model);
        PageInfo<QuestionPageStudentResponseVM> page = PageInfoHelper.copyMap(pageInfo, q -> {
            Subject subject = subjectService.selectById(q.getSubjectId());
            QuestionPageStudentResponseVM vm = modelMapper.map(q, QuestionPageStudentResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(q.getCreateTime()));
            TextContent textContent = textContentService.selectById(q.getQuestionTextContentId());
            QuestionObject questionObject = JsonUtil.toJsonObject(textContent.getContent(), QuestionObject.class);
            String clearHtml = HtmlUtil.clear(questionObject.getTitleContent());
            vm.setShortTitle(clearHtml);
            vm.setSubjectName(subject.getName());
            return vm;
        });
        return RestResponse.ok(page);
    }


    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<QuestionAnswerVM> select(@PathVariable Integer id) {
        QuestionAnswerVM vm = new QuestionAnswerVM();
        ExamPaperQuestionCustomerAnswer examPaperQuestionCustomerAnswer = examPaperQuestionCustomerAnswerService.selectById(id);
        ExamPaperSubmitItemVM questionAnswerVM = examPaperQuestionCustomerAnswerService.examPaperQuestionCustomerAnswerToVM(examPaperQuestionCustomerAnswer);
        QuestionEditRequestVM questionVM = questionService.getQuestionEditRequestVM(examPaperQuestionCustomerAnswer.getQuestionId());
        vm.setQuestionVM(questionVM);
        vm.setQuestionAnswerVM(questionAnswerVM);
        return RestResponse.ok(vm);
    }

}
