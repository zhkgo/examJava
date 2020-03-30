package com.zhk.examonline.repository;

import com.zhk.examonline.domain.ExamPaper;
import com.zhk.examonline.domain.other.KeyValue;
import com.zhk.examonline.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.zhk.examonline.viewmodel.student.dashboard.PaperFilter;
import com.zhk.examonline.viewmodel.student.dashboard.PaperInfo;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperPageVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {
    int deleteByPrimaryKey(Integer id);

    int insert(ExamPaper record);

    int insertSelective(ExamPaper record);

    ExamPaper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExamPaper record);

    int updateByPrimaryKey(ExamPaper record);

    List<ExamPaper> page(ExamPaperPageRequestVM requestVM);

    List<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM);

    List<ExamPaper> studentPage(ExamPaperPageVM requestVM);

    List<PaperInfo> indexPaper(PaperFilter paperFilter);

    Integer selectAllCount();

    List<KeyValue> selectCountByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int updateTaskPaper(@Param("taskId") Integer taskId, @Param("paperIds") List<Integer> paperIds);

    int clearTaskPaper(@Param("paperIds") List<Integer> paperIds);
}
