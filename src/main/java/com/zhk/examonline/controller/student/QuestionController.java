package com.zhk.examonline.controller.student;

import com.zhk.examonline.base.BaseApiController;
import com.zhk.examonline.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("StudentQuestionController")
@RequestMapping(value = "/api/student/question")
@AllArgsConstructor
public class QuestionController extends BaseApiController {

    private final QuestionService questionService;


}
