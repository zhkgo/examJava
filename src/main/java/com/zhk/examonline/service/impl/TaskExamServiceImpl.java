package com.zhk.examonline.service.impl;

import com.zhk.examonline.domain.ExamPaper;
import com.zhk.examonline.domain.TaskExam;
import com.zhk.examonline.domain.TextContent;
import com.zhk.examonline.domain.User;
import com.zhk.examonline.domain.task.TaskItemObject;
import com.zhk.examonline.repository.ExamPaperMapper;
import com.zhk.examonline.repository.TaskExamMapper;
import com.zhk.examonline.service.TaskExamService;
import com.zhk.examonline.service.TextContentService;
import com.zhk.examonline.service.enums.ActionEnum;
import com.zhk.examonline.utility.DateTimeUtil;
import com.zhk.examonline.utility.JsonUtil;
import com.zhk.examonline.utility.ModelMapperSingle;
import com.zhk.examonline.viewmodel.admin.exam.ExamResponseVM;
import com.zhk.examonline.viewmodel.admin.task.TaskPageRequestVM;
import com.zhk.examonline.viewmodel.admin.task.TaskRequestVM;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskExamServiceImpl extends BaseServiceImpl<TaskExam> implements TaskExamService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
    private final TaskExamMapper taskExamMapper;
    private final TextContentService textContentService;
    private final ExamPaperMapper examPaperMapper;

    @Autowired
    public TaskExamServiceImpl(TaskExamMapper taskExamMapper, TextContentService textContentService, ExamPaperMapper examPaperMapper) {
        super(taskExamMapper);
        this.taskExamMapper = taskExamMapper;
        this.textContentService = textContentService;
        this.examPaperMapper = examPaperMapper;
    }

    @Override
    public PageInfo<TaskExam> page(TaskPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                taskExamMapper.page(requestVM)
        );
    }

    @Override
    @Transactional
    public void edit(TaskRequestVM model, User user) {
        ActionEnum actionEnum = (model.getId() == null) ? ActionEnum.ADD : ActionEnum.UPDATE;
        TaskExam taskExam = null;
        if (actionEnum == ActionEnum.ADD) {
            Date now = new Date();
            taskExam = modelMapper.map(model, TaskExam.class);
            taskExam.setCreateUser(user.getId());
            taskExam.setCreateUserName(user.getUserName());
            taskExam.setCreateTime(now);
            taskExam.setDeleted(false);

            //保存任务结构
            TextContent textContent = textContentService.jsonConvertInsert(model.getPaperItems(), now, p -> {
                TaskItemObject taskItemObject = new TaskItemObject();
                taskItemObject.setExamPaperId(p.getId());
                taskItemObject.setExamPaperName(p.getName());
                return taskItemObject;
            });
            textContentService.insertByFilter(textContent);
            taskExam.setFrameTextContentId(textContent.getId());
            taskExamMapper.insertSelective(taskExam);

        } else {
            taskExam = taskExamMapper.selectByPrimaryKey(model.getId());
            modelMapper.map(model, taskExam);

            TextContent textContent = textContentService.selectById(taskExam.getFrameTextContentId());
            //清空试卷任务的试卷Id，后面会统一设置
            List<Integer> paperIds = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemObject.class)
                    .stream()
                    .map(d -> d.getExamPaperId())
                    .collect(Collectors.toList());
            examPaperMapper.clearTaskPaper(paperIds);

            //更新任务结构
            textContentService.jsonConvertUpdate(textContent, model.getPaperItems(), p -> {
                TaskItemObject taskItemObject = new TaskItemObject();
                taskItemObject.setExamPaperId(p.getId());
                taskItemObject.setExamPaperName(p.getName());
                return taskItemObject;
            });
            textContentService.updateByIdFilter(textContent);
            taskExamMapper.updateByPrimaryKeySelective(taskExam);
        }

        //更新试卷的taskId
        List<Integer> paperIds = model.getPaperItems().stream().map(d -> d.getId()).collect(Collectors.toList());
        examPaperMapper.updateTaskPaper(taskExam.getId(), paperIds);
        model.setId(taskExam.getId());
    }

    @Override
    public TaskRequestVM taskExamToVM(Integer id) {
        TaskExam taskExam = taskExamMapper.selectByPrimaryKey(id);
        TaskRequestVM vm = modelMapper.map(taskExam, TaskRequestVM.class);
        TextContent textContent = textContentService.selectById(taskExam.getFrameTextContentId());
        List<ExamResponseVM> examResponseVMS = JsonUtil.toJsonListObject(textContent.getContent(), TaskItemObject.class).stream().map(tk -> {
            ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(tk.getExamPaperId());
            ExamResponseVM examResponseVM = modelMapper.map(examPaper, ExamResponseVM.class);
            examResponseVM.setCreateTime(DateTimeUtil.dateFormat(examPaper.getCreateTime()));
            return examResponseVM;
        }).collect(Collectors.toList());
        vm.setPaperItems(examResponseVMS);
        return vm;
    }

    @Override
    public List<TaskExam> getByGradeLevel(Integer gradeLevel) {
        return taskExamMapper.getByGradeLevel(gradeLevel);
    }
}