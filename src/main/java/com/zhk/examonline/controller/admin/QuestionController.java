package com.zhk.examonline.controller.admin;

import com.zhk.examonline.base.BaseApiController;
import com.zhk.examonline.base.RestResponse;
import com.zhk.examonline.base.SystemCode;
import com.zhk.examonline.domain.Question;
import com.zhk.examonline.domain.TextContent;
import com.zhk.examonline.domain.enums.QuestionTypeEnum;
import com.zhk.examonline.domain.question.QuestionObject;
import com.zhk.examonline.service.QuestionService;
import com.zhk.examonline.service.TextContentService;
import com.zhk.examonline.utility.*;
import com.zhk.examonline.viewmodel.admin.question.QuestionEditRequestVM;
import com.zhk.examonline.viewmodel.admin.question.QuestionPageRequestVM;
import com.zhk.examonline.viewmodel.admin.question.QuestionResponseVM;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("AdminQuestionController")
@RequestMapping(value = "/api/admin/question")
@AllArgsConstructor
public class QuestionController extends BaseApiController {

    private final QuestionService questionService;
    private final TextContentService textContentService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<QuestionResponseVM>> pageList(@RequestBody QuestionPageRequestVM model) {
        PageInfo<Question> pageInfo = questionService.page(model);
        PageInfo<QuestionResponseVM> page = PageInfoHelper.copyMap(pageInfo, q -> {
            QuestionResponseVM vm = modelMapper.map(q, QuestionResponseVM.class);
            vm.setCreateTime(DateTimeUtil.dateFormat(q.getCreateTime()));
            vm.setScore(ExamUtil.scoreToVM(q.getScore()));
            TextContent textContent = textContentService.selectById(q.getInfoTextContentId());
            QuestionObject questionObject = JsonUtil.toJsonObject(textContent.getContent(), QuestionObject.class);
            String clearHtml = HtmlUtil.clear(questionObject.getTitleContent());
            vm.setShortTitle(clearHtml);
            return vm;
        });
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public RestResponse<QuestionEditRequestVM> edit(@RequestBody @Valid QuestionEditRequestVM model) {
        RestResponse validQuestionEditRequestResult = validQuestionEditRequestVM(model);
        if (validQuestionEditRequestResult.getCode() != SystemCode.OK.getCode()) {
            return validQuestionEditRequestResult;
        }

        Question question;
        if (null == model.getId()) {
            question = questionService.insertFullQuestion(model, getCurrentUser().getId());
        } else {
            question = questionService.updateFullQuestion(model);
        }
        QuestionEditRequestVM newVM = questionService.getQuestionEditRequestVM(question.getId());
        return RestResponse.ok(newVM);
    }

    @RequestMapping(value = "/select/{id}", method = RequestMethod.POST)
    public RestResponse<QuestionEditRequestVM> select(@PathVariable Integer id) {
        QuestionEditRequestVM newVM = questionService.getQuestionEditRequestVM(id);
        return RestResponse.ok(newVM);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id) {
        Question question = questionService.selectById(id);
        question.setDeleted(true);
        questionService.updateByIdFilter(question);
        return RestResponse.ok();
    }

    private RestResponse validQuestionEditRequestVM(QuestionEditRequestVM model) {
        int qType = model.getQuestionType().intValue();
        boolean requireCorrect = qType == QuestionTypeEnum.SingleChoice.getCode() || qType == QuestionTypeEnum.TrueFalse.getCode();
        if (requireCorrect) {
            if (StringUtils.isBlank(model.getCorrect())) {
                String errorMsg = ErrorUtil.parameterErrorFormat("correct", "不能为空");
                return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
            }
        }

        if (qType == QuestionTypeEnum.GapFilling.getCode()) {
            Integer fillSumScore = model.getItems().stream().mapToInt(d -> ExamUtil.scoreFromVM(d.getScore())).sum();
            Integer questionScore = ExamUtil.scoreFromVM(model.getScore());
            if (!fillSumScore.equals(questionScore)) {
                String errorMsg = ErrorUtil.parameterErrorFormat("score", "空分数和与题目总分不相等");
                return new RestResponse<>(SystemCode.ParameterValidError.getCode(), errorMsg);
            }
        }
        return RestResponse.ok();
    }
}
