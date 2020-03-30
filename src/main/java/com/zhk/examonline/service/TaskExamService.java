package com.zhk.examonline.service;

import com.zhk.examonline.domain.TaskExam;
import com.zhk.examonline.domain.User;
import com.zhk.examonline.viewmodel.admin.task.TaskPageRequestVM;
import com.zhk.examonline.viewmodel.admin.task.TaskRequestVM;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TaskExamService extends BaseService<TaskExam> {

    PageInfo<TaskExam> page(TaskPageRequestVM requestVM);

    void edit(TaskRequestVM model, User user);

    TaskRequestVM taskExamToVM(Integer id);

    List<TaskExam> getByGradeLevel(Integer gradeLevel);
}
