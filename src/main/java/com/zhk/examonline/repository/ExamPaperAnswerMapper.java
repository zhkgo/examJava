package com.zhk.examonline.repository;

import com.zhk.examonline.domain.ExamPaperAnswer;
import com.zhk.examonline.domain.other.KeyValue;
import com.zhk.examonline.viewmodel.admin.exampaper.ExamPaperAnswerAPageVM;
import com.zhk.examonline.viewmodel.student.exampaper.ExamPaperAnswerPageVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ExamPaperAnswerMapper extends BaseMapper<ExamPaperAnswer> {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamPaperAnswer record);

    int insertSelective(ExamPaperAnswer record);

    ExamPaperAnswer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamPaperAnswer record);

    int updateByPrimaryKey(ExamPaperAnswer record);
    List<ExamPaperAnswer> adminPage(ExamPaperAnswerAPageVM requestVM);
    List<ExamPaperAnswer> studentPage(ExamPaperAnswerPageVM requestVM);

    Integer selectAllCount();

    List<KeyValue> selectCountByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    ExamPaperAnswer getByPidUid(@Param("pid") Integer paperId, @Param("uid") Integer uid);

}
