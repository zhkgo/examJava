package com.zhk.examonline.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.zhk.examonline.base.RestResponse;
import com.zhk.examonline.base.SystemCode;
import com.zhk.examonline.domain.Question;
import com.zhk.examonline.domain.enums.QuestionTypeEnum;
import com.zhk.examonline.domain.excel.MutiChoice;
import com.zhk.examonline.domain.excel.TrueFalse;
import com.zhk.examonline.service.QuestionService;
import com.zhk.examonline.utility.ErrorUtil;
import com.zhk.examonline.utility.ExamUtil;
import com.zhk.examonline.utility.QuestionParserUtil;
import com.zhk.examonline.viewmodel.admin.question.QuestionEditRequestVM;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TrueFalseDataListener extends AnalysisEventListener<TrueFalse> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<TrueFalse> list = new ArrayList<TrueFalse>();

    private QuestionService questionService;
    private QuestionParserUtil questionParserUtil;
    private Integer id;
    public TrueFalseDataListener(QuestionService questionService, QuestionParserUtil questionParserUtil, Integer id) {
        this.questionService=questionService;
        this.questionParserUtil=questionParserUtil;
        this.id=id;
    }


    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(TrueFalse data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        QuestionEditRequestVM model;
        Question question;
        for(TrueFalse q:list){
            model=questionParserUtil.transTo(q);
            RestResponse validQuestionEditRequestResult = validQuestionEditRequestVM(model);
            if (validQuestionEditRequestResult.getCode() != SystemCode.OK.getCode()) {
                //do somethings
                continue;
            }
            question = questionService.insertFullQuestion(model, id);
        }
        log.info("存储数据库成功！");
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
