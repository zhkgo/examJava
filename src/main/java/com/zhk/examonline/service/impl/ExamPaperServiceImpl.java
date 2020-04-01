package com.zhk.examonline.service.impl;

import com.zhk.examonline.domain.ExamPaper;
import com.zhk.examonline.domain.Question;
import com.zhk.examonline.domain.TextContent;
import com.zhk.examonline.domain.User;
import com.zhk.examonline.domain.enums.ExamPaperTypeEnum;
import com.zhk.examonline.domain.enums.QuestionTypeEnum;
import com.zhk.examonline.domain.exam.ExamPaperQuestionItemObject;
import com.zhk.examonline.domain.exam.ExamPaperTitleItemObject;
import com.zhk.examonline.domain.other.KeyValue;
import com.zhk.examonline.domain.question.QuestionTypeClass;
import com.zhk.examonline.repository.ExamPaperMapper;
import com.zhk.examonline.repository.QuestionMapper;
import com.zhk.examonline.service.ExamPaperService;
import com.zhk.examonline.service.QuestionService;
import com.zhk.examonline.service.SubjectService;
import com.zhk.examonline.service.TextContentService;
import com.zhk.examonline.service.enums.ActionEnum;
import com.zhk.examonline.utility.*;
import com.zhk.examonline.viewmodel.admin.exam.ExamPaperEditRequestVM;
import com.zhk.examonline.viewmodel.admin.exam.ExamPaperPageRequestVM;
import com.zhk.examonline.viewmodel.admin.exam.ExamPaperTitleItemVM;
import com.zhk.examonline.viewmodel.admin.exampaper.ExamPaperRandomRequestVM;
import com.zhk.examonline.viewmodel.admin.question.QuestionEditRequestVM;
import com.zhk.examonline.viewmodel.admin.question.QuestionPageRequestVM;
import com.zhk.examonline.viewmodel.student.dashboard.PaperFilter;
import com.zhk.examonline.viewmodel.student.dashboard.PaperInfo;
import com.zhk.examonline.viewmodel.student.exam.ExamPaperPageVM;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExamPaperServiceImpl extends BaseServiceImpl<ExamPaper> implements ExamPaperService {

    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();
    private final ExamPaperMapper examPaperMapper;
    private final QuestionMapper questionMapper;
    private final TextContentService textContentService;
    private final QuestionService questionService;
    private final SubjectService subjectService;

    @Autowired
    public ExamPaperServiceImpl(ExamPaperMapper examPaperMapper, QuestionMapper questionMapper, TextContentService textContentService, QuestionService questionService, SubjectService subjectService) {
        super(examPaperMapper);
        this.examPaperMapper = examPaperMapper;
        this.questionMapper = questionMapper;
        this.textContentService = textContentService;
        this.questionService = questionService;
        this.subjectService = subjectService;
    }


    @Override
    public PageInfo<ExamPaper> page(ExamPaperPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperMapper.page(requestVM));
    }

    @Override
    public PageInfo<ExamPaper> taskExamPage(ExamPaperPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperMapper.taskExamPage(requestVM));
    }

    @Override
    public PageInfo<ExamPaper> studentPage(ExamPaperPageVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperMapper.studentPage(requestVM));
    }

    @Override
    @Transactional
    public ExamPaper savePaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, User user) {
        ActionEnum actionEnum = (examPaperEditRequestVM.getId() == null) ? ActionEnum.ADD : ActionEnum.UPDATE;
        Date now = new Date();
        List<ExamPaperTitleItemVM> titleItemsVM = examPaperEditRequestVM.getTitleItems();
        List<ExamPaperTitleItemObject> frameTextContentList = frameTextContentFromVM(titleItemsVM);
        String frameTextContentStr = JsonUtil.toJsonStr(frameTextContentList);

        ExamPaper examPaper;
        if (actionEnum == ActionEnum.ADD) {
            examPaper = modelMapper.map(examPaperEditRequestVM, ExamPaper.class);
            TextContent frameTextContent = new TextContent(frameTextContentStr, now);
            textContentService.insertByFilter(frameTextContent);
            examPaper.setFrameTextContentId(frameTextContent.getId());
            examPaper.setCreateTime(now);
            examPaper.setCreateUser(user.getId());
            examPaper.setDeleted(false);
            examPaperFromVM(examPaperEditRequestVM, examPaper, titleItemsVM);
            examPaperMapper.insertSelective(examPaper);
        } else {
            examPaper = examPaperMapper.selectByPrimaryKey(examPaperEditRequestVM.getId());
            TextContent frameTextContent = textContentService.selectById(examPaper.getFrameTextContentId());
            frameTextContent.setContent(frameTextContentStr);
            textContentService.updateByIdFilter(frameTextContent);
            modelMapper.map(examPaperEditRequestVM, examPaper);
            examPaperFromVM(examPaperEditRequestVM, examPaper, titleItemsVM);
            examPaperMapper.updateByPrimaryKeySelective(examPaper);
        }
        return examPaper;
    }

    @Override
    public ExamPaperEditRequestVM examPaperToVM(Integer id) {
        ExamPaper examPaper = examPaperMapper.selectByPrimaryKey(id);
        ExamPaperEditRequestVM vm = modelMapper.map(examPaper, ExamPaperEditRequestVM.class);
        vm.setLevel(examPaper.getGradeLevel());
        TextContent frameTextContent = textContentService.selectById(examPaper.getFrameTextContentId());
        List<ExamPaperTitleItemObject> examPaperTitleItemObjects = JsonUtil.toJsonListObject(frameTextContent.getContent(), ExamPaperTitleItemObject.class);
        List<Integer> questionIds = examPaperTitleItemObjects.stream()
                .flatMap(t -> t.getQuestionItems().stream()
                        .map(q -> q.getId()))
                .collect(Collectors.toList());
        List<Question> questions = questionMapper.selectByIds(questionIds);
        List<ExamPaperTitleItemVM> examPaperTitleItemVMS = examPaperTitleItemObjects.stream().map(t -> {
            ExamPaperTitleItemVM tTitleVM = modelMapper.map(t, ExamPaperTitleItemVM.class);
            List<QuestionEditRequestVM> questionItemsVM = t.getQuestionItems().stream().map(i -> {
                Question question = questions.stream().filter(q -> q.getId().equals(i.getId())).findFirst().get();
                QuestionEditRequestVM questionEditRequestVM = questionService.getQuestionEditRequestVM(question);
                questionEditRequestVM.setItemOrder(i.getItemOrder());
                return questionEditRequestVM;
            }).collect(Collectors.toList());
            tTitleVM.setQuestionItems(questionItemsVM);
            return tTitleVM;
        }).collect(Collectors.toList());
        vm.setTitleItems(examPaperTitleItemVMS);
        vm.setScore(ExamUtil.scoreToVM(examPaper.getScore()));
        if (ExamPaperTypeEnum.TimeLimit == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {
            List<String> limitDateTime = Arrays.asList(DateTimeUtil.dateFormat(examPaper.getLimitStartTime()), DateTimeUtil.dateFormat(examPaper.getLimitEndTime()));
            vm.setLimitDateTime(limitDateTime);
        }
        return vm;
    }

    @Override
    public List<PaperInfo> indexPaper(PaperFilter paperFilter) {
        return examPaperMapper.indexPaper(paperFilter);
    }


    @Override
    public Integer selectAllCount() {
        return examPaperMapper.selectAllCount();
    }

    @Override
    public ExamPaperEditRequestVM generateRandom(ExamPaperRandomRequestVM model) throws Exception{
        QuestionPageRequestVM questionPageRequestVM=new QuestionPageRequestVM();
        ExamPaperEditRequestVM examPaperEditRequestVM=new ExamPaperEditRequestVM();
        examPaperEditRequestVM.setSubjectId(model.getSubjectId());
        examPaperEditRequestVM.setLevel(model.getLevel());
        questionPageRequestVM.setSubjectId(model.getSubjectId());
        questionPageRequestVM.setLevel(model.getLevel());
        List<Question> questions=questionMapper.page(questionPageRequestVM);
        List<QuestionTypeClass> questionTypeClasses=new ArrayList<QuestionTypeClass>();
        questionTypeClasses.add(new QuestionTypeClass(1,questions));
        questionTypeClasses.add(new QuestionTypeClass(2,questions));
        questionTypeClasses.add(new QuestionTypeClass(3,questions));
        questionTypeClasses.add(new QuestionTypeClass(4,questions));
        questionTypeClasses.add(new QuestionTypeClass(5,questions));
        if(questionTypeClasses.get(0).size()<model.getSingleChoice()){
            throw new Exception("单选题要求数量超过库存");
        }
        if(questionTypeClasses.get(1).size()<model.getMultipleChoice()){
            throw new Exception("多选题要求数量超过库存");
        }
        if(questionTypeClasses.get(2).size()<model.getTrueFalse()){
            throw new Exception("判断题要求数量超过库存");
        }
        if(questionTypeClasses.get(3).size()<model.getGapFilling()){
            throw new Exception("填空题要求数量超过库存");
        }
        if(questionTypeClasses.get(4).size()<model.getShortAnswer()){
            throw new Exception("简答题要求数量超过库存");
        }
        questionTypeClasses.get(0).setQuestionList(RandomPick.<Question>randomPicker(questionTypeClasses.get(0).getQuestionList(),model.getSingleChoice()));
        questionTypeClasses.get(1).setQuestionList(RandomPick.<Question>randomPicker(questionTypeClasses.get(1).getQuestionList(),model.getMultipleChoice()));
        questionTypeClasses.get(2).setQuestionList(RandomPick.<Question>randomPicker(questionTypeClasses.get(2).getQuestionList(),model.getTrueFalse()));
        questionTypeClasses.get(3).setQuestionList(RandomPick.<Question>randomPicker(questionTypeClasses.get(3).getQuestionList(),model.getGapFilling()));
        questionTypeClasses.get(4).setQuestionList(RandomPick.<Question>randomPicker(questionTypeClasses.get(4).getQuestionList(),model.getShortAnswer()));
        List<ExamPaperTitleItemVM> titleItems=questionTypeClasses.stream().map(i->{
            if(i.size()==0)return null;
            ExamPaperTitleItemVM vm=new ExamPaperTitleItemVM();
            vm.setName(QuestionTypeEnum.fromCode(i.getQType()).getName());
            List<QuestionEditRequestVM> questionItems=i.getQuestionList().stream().map(question->{
                return questionService.getQuestionEditRequestVM(question);
            }).collect(Collectors.toList());
            vm.setQuestionItems(questionItems);
            return vm;
        }).collect(Collectors.toList());
        titleItems.removeIf(e->e==null);
        examPaperEditRequestVM.setTitleItems(titleItems);
        return examPaperEditRequestVM;
    }


    @Override
    public List<Integer> selectMothCount() {
        Date startTime = DateTimeUtil.getMonthStartDay();
        Date endTime = DateTimeUtil.getMonthEndDay();
        List<KeyValue> mouthCount = examPaperMapper.selectCountByDate(startTime, endTime);
        List<String> mothStartToNowFormat = DateTimeUtil.MothStartToNowFormat();
        return mothStartToNowFormat.stream().map(md -> {
            KeyValue keyValue = mouthCount.stream().filter(kv -> kv.getName().equals(md)).findAny().orElse(null);
            return null == keyValue ? 0 : keyValue.getValue();
        }).collect(Collectors.toList());
    }

    private void examPaperFromVM(ExamPaperEditRequestVM examPaperEditRequestVM, ExamPaper examPaper, List<ExamPaperTitleItemVM> titleItemsVM) {
        Integer gradeLevel = subjectService.levelBySubjectId(examPaperEditRequestVM.getSubjectId());
        Integer questionCount = titleItemsVM.stream()
                .mapToInt(t -> t.getQuestionItems().size()).sum();
        Integer score = titleItemsVM.stream().
                flatMapToInt(t -> t.getQuestionItems().stream()
                        .mapToInt(q -> ExamUtil.scoreFromVM(q.getScore()))
                ).sum();
        examPaper.setQuestionCount(questionCount);
        examPaper.setScore(score);
        examPaper.setGradeLevel(gradeLevel);
        List<String> dateTimes = examPaperEditRequestVM.getLimitDateTime();
        if (ExamPaperTypeEnum.TimeLimit == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {
            examPaper.setLimitStartTime(DateTimeUtil.parse(dateTimes.get(0), DateTimeUtil.STANDER_FORMAT));
            examPaper.setLimitEndTime(DateTimeUtil.parse(dateTimes.get(1), DateTimeUtil.STANDER_FORMAT));
        }
    }

    private List<ExamPaperTitleItemObject> frameTextContentFromVM(List<ExamPaperTitleItemVM> titleItems) {
        AtomicInteger index = new AtomicInteger(1);
        return titleItems.stream().map(t -> {
            ExamPaperTitleItemObject titleItem = modelMapper.map(t, ExamPaperTitleItemObject.class);
            List<ExamPaperQuestionItemObject> questionItems = t.getQuestionItems().stream()
                    .map(q -> {
                        ExamPaperQuestionItemObject examPaperQuestionItemObject = modelMapper.map(q, ExamPaperQuestionItemObject.class);
                        examPaperQuestionItemObject.setItemOrder(index.getAndIncrement());
                        return examPaperQuestionItemObject;
                    })
                    .collect(Collectors.toList());
            titleItem.setQuestionItems(questionItems);
            return titleItem;
        }).collect(Collectors.toList());
    }
}
