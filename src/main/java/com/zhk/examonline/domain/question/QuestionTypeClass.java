package com.zhk.examonline.domain.question;

import com.zhk.examonline.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class QuestionTypeClass {
    Integer qType;
    List<Question> questionList;
    public QuestionTypeClass(int qType,List<Question> questions){
        this.qType=qType;
        this.questionList=new ArrayList<Question>();
        for(Question question:questions){
            this.addItem(question);
        }
    }
    public boolean addItem(Question question){
        if(question.getQuestionType()!=qType)
            return false;
        questionList.add(question);
        return true;
    }
    public int size(){
        if(questionList==null)return 0;
        return questionList.size();
    }
}
