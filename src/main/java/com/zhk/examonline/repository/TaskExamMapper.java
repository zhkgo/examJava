package com.zhk.examonline.repository;

import com.zhk.examonline.domain.TaskExam;
import com.zhk.examonline.viewmodel.admin.task.TaskPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskExamMapper extends BaseMapper<TaskExam> {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskExam record);

    int insertSelective(TaskExam record);

    TaskExam selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskExam record);

    int updateByPrimaryKey(TaskExam record);

    List<TaskExam> page(TaskPageRequestVM requestVM);

    List<TaskExam> getByGradeLevel(Integer gradeLevel);
}
