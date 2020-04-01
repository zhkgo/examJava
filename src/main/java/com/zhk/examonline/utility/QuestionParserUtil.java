package com.zhk.examonline.utility;

import com.alibaba.excel.EasyExcel;
import com.zhk.examonline.domain.Question;
import com.zhk.examonline.domain.Subject;
import com.zhk.examonline.domain.excel.*;
import com.zhk.examonline.repository.SubjectMapper;
import com.zhk.examonline.viewmodel.admin.question.QuestionEditItemVM;
import com.zhk.examonline.viewmodel.admin.question.QuestionEditRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TransferQueue;


public class QuestionParserUtil {
    List<QuestionEditRequestVM> questions;
    SubjectMapper subjectMapper;
    public QuestionParserUtil(SubjectMapper subjectMapper) {
        questions = new ArrayList<QuestionEditRequestVM>();
        this.subjectMapper = subjectMapper;
    }
    public String gapReplace(String text){
        String left="<span class=\"gapfilling-span f581607a-671b-4288-9940-3170d393f6ee\">";
        String right="</span>";
        int flag=0;
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<text.length();i++){
            if(text.charAt(i)!='$'){
                sb.append(text.charAt(i));
            }else{
                if(flag==0){
                    sb.append(left);
                }else{
                    sb.append(right);
                }
                flag=1-flag;
            }
        }
        return sb.toString();
    }

    public QuestionEditRequestVM transTo(SingleChoice singleChoice) {
        QuestionEditRequestVM model = new QuestionEditRequestVM();
        Subject subject = new Subject();
        subject.setLevelName(singleChoice.getGradeLevel());
        subject.setName(singleChoice.getSubjectId());
        subject = subjectMapper.getBySubject(subject);//补全subject
        model.setQuestionType(1);
        model.setGradeLevel(subject.getLevel());
        model.setSubjectId(subject.getId());
        model.setTitle(singleChoice.getTitle());
        model.setAnalyze(singleChoice.getAnalyze());
        model.setCorrect(singleChoice.getCorrect());
        model.setScore(singleChoice.getScore());
        model.setDifficult(singleChoice.getDifficult());
        List<QuestionEditItemVM> items = new ArrayList<QuestionEditItemVM>();
        QuestionEditItemVM vm;
        try {
            if (singleChoice.getCa().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("A");
                vm.setContent(singleChoice.getCa());
                items.add(vm);
            }
            if (singleChoice.getCb().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("B");
                vm.setContent(singleChoice.getCb());
                items.add(vm);
            }
            if (singleChoice.getCc().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("C");
                vm.setContent(singleChoice.getCc());
                items.add(vm);
            }
            if (singleChoice.getCd().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("D");
                vm.setContent(singleChoice.getCd());
                items.add(vm);
            }
            if (singleChoice.getCe().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("E");
                vm.setContent(singleChoice.getCe());
                items.add(vm);
            }
            if (singleChoice.getCf().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("F");
                vm.setContent(singleChoice.getCf());
                items.add(vm);
            }
            if (singleChoice.getCg().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("G");
                vm.setContent(singleChoice.getCg());
                items.add(vm);
            }
            if (singleChoice.getCh().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("H");
                vm.setContent(singleChoice.getCh());
                items.add(vm);
            }
        } catch (Exception e) {
            System.out.println("解析单选ing");
        }
        model.setItems(items);
        return model;
    }

    public QuestionEditRequestVM transTo(MutiChoice mutiChoice) {
        QuestionEditRequestVM model = new QuestionEditRequestVM();
        Subject subject = new Subject();
        subject.setLevelName(mutiChoice.getGradeLevel());
        subject.setName(mutiChoice.getSubjectId());
        subject = subjectMapper.getBySubject(subject);//补全subject
        model.setQuestionType(2);
        model.setGradeLevel(subject.getLevel());
        model.setSubjectId(subject.getId());
        model.setTitle(mutiChoice.getTitle());
        model.setAnalyze(mutiChoice.getAnalyze());
        String[] array = mutiChoice.getCorrect().split("");
        ArrayList<String> alist = new ArrayList<String>();
        alist.addAll(Arrays.asList(array));
        model.setCorrectArray(alist);
        model.setScore(mutiChoice.getScore());
        model.setDifficult(mutiChoice.getDifficult());
        List<QuestionEditItemVM> items = new ArrayList<QuestionEditItemVM>();
        QuestionEditItemVM vm;
        try {
            if (mutiChoice.getCa().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("A");
                vm.setContent(mutiChoice.getCa());
                items.add(vm);
            }
            if (mutiChoice.getCb().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("B");
                vm.setContent(mutiChoice.getCb());
                items.add(vm);
            }
            if (mutiChoice.getCc().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("C");
                vm.setContent(mutiChoice.getCc());
                items.add(vm);
            }
            if (mutiChoice.getCd().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("D");
                vm.setContent(mutiChoice.getCd());
                items.add(vm);
            }
            if (mutiChoice.getCe().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("E");
                vm.setContent(mutiChoice.getCe());
                items.add(vm);
            }
            if (mutiChoice.getCf().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("F");
                vm.setContent(mutiChoice.getCf());
                items.add(vm);
            }
            if (mutiChoice.getCg().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("G");
                vm.setContent(mutiChoice.getCg());
                items.add(vm);
            }
            if (mutiChoice.getCh().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("H");
                vm.setContent(mutiChoice.getCh());
                items.add(vm);
            }
        } catch (Exception e) {
            System.out.println("解析多选题ing");
        }
        model.setItems(items);
        return model;
    }

    public QuestionEditRequestVM transTo(TrueFalse trueFalse) {
        QuestionEditRequestVM model = new QuestionEditRequestVM();
        Subject subject = new Subject();
        subject.setLevelName(trueFalse.getGradeLevel());
        subject.setName(trueFalse.getSubjectId());
        subject = subjectMapper.getBySubject(subject);//补全subject
        model.setQuestionType(3);
        model.setGradeLevel(subject.getLevel());
        model.setSubjectId(subject.getId());
        model.setTitle(trueFalse.getTitle());
        List<QuestionEditItemVM> items = new ArrayList<QuestionEditItemVM>();
        QuestionEditItemVM vm;
        vm = new QuestionEditItemVM();
        vm.setPrefix("A");
        vm.setContent(trueFalse.getCa());
        items.add(vm);
        vm = new QuestionEditItemVM();
        vm.setPrefix("B");
        vm.setContent(trueFalse.getCb());
        items.add(vm);
        model.setItems(items);
        model.setAnalyze(trueFalse.getAnalyze());
        model.setCorrect(trueFalse.getCorrect());
        model.setScore(trueFalse.getScore());
        model.setDifficult(trueFalse.getDifficult());
        return model;
    }

    public QuestionEditRequestVM transTo(GapFilling gapFilling) {
        QuestionEditRequestVM model = new QuestionEditRequestVM();
        Subject subject = new Subject   ();
        subject.setLevelName(gapFilling.getGradeLevel());
        subject.setName(gapFilling.getSubjectId());
        subject = subjectMapper.getBySubject(subject);//补全subject
        model.setQuestionType(4);
        model.setGradeLevel(subject.getLevel());
        model.setSubjectId(subject.getId());
        model.setTitle(gapReplace(gapFilling.getTitle()));
        model.setAnalyze(gapFilling.getAnalyze());
        model.setDifficult(gapFilling.getDifficult());
        List<QuestionEditItemVM> items = new ArrayList<QuestionEditItemVM>();
        QuestionEditItemVM vm;
        try {
            if (gapFilling.getK1().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("1");
                vm.setContent(gapFilling.getK1());
                vm.setScore(gapFilling.getScore());
                items.add(vm);
            }
            if (gapFilling.getK2().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("2");
                vm.setContent(gapFilling.getK2());
                vm.setScore(gapFilling.getScore());
                items.add(vm);
            }
            if (gapFilling.getK3().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("3");
                vm.setContent(gapFilling.getK3());
                vm.setScore(gapFilling.getScore());
                items.add(vm);
            }
            if (gapFilling.getK4().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("4");
                vm.setContent(gapFilling.getK4());
                vm.setScore(gapFilling.getScore());
                items.add(vm);
            }
            if (gapFilling.getK5().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("5");
                vm.setContent(gapFilling.getK5());
                vm.setScore(gapFilling.getScore());
                items.add(vm);
            }
            if (gapFilling.getK6().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("6");
                vm.setContent(gapFilling.getK6());
                vm.setScore(gapFilling.getScore());
                items.add(vm);
            }
            if (gapFilling.getK7().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("7");
                vm.setContent(gapFilling.getK7());
                vm.setScore(gapFilling.getScore());
                items.add(vm);
            }
            if (gapFilling.getK8().trim().length() > 0) {
                vm = new QuestionEditItemVM();
                vm.setPrefix("8");
                vm.setContent(gapFilling.getK8());
                vm.setScore(gapFilling.getScore());
                items.add(vm);
            }
        } catch (Exception e) {
            System.out.println("解析填空题ing");
        }
        model.setScore(String.valueOf(items.size() * Float.parseFloat(gapFilling.getScore())));
        model.setItems(items);
        return model;
    }

    public QuestionEditRequestVM transTo(ShortAnswer shortAnswer) {
        QuestionEditRequestVM model = new QuestionEditRequestVM();
        Subject subject = new Subject();
        subject.setLevelName(shortAnswer.getGradeLevel());
        subject.setName(shortAnswer.getSubjectId());
        subject = subjectMapper.getBySubject(subject);//补全subject
        model.setQuestionType(5);
        model.setGradeLevel(subject.getLevel());
        model.setSubjectId(subject.getId());
        model.setTitle(shortAnswer.getTitle());
        model.setAnalyze(shortAnswer.getAnalyze());
        model.setDifficult(shortAnswer.getDifficult());
        model.setCorrect(shortAnswer.getCorrect());
        model.setScore(shortAnswer.getScore());
        model.setItems(new ArrayList<QuestionEditItemVM>());
        return model;
    }


}
