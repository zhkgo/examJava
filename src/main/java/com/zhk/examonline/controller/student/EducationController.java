package com.zhk.examonline.controller.student;


import com.zhk.examonline.base.BaseApiController;
import com.zhk.examonline.base.RestResponse;
import com.zhk.examonline.domain.Subject;
import com.zhk.examonline.domain.User;
import com.zhk.examonline.service.SubjectService;
import com.zhk.examonline.viewmodel.student.education.SubjectEditRequestVM;
import com.zhk.examonline.viewmodel.student.education.SubjectVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController("StudentEducationController")
@RequestMapping(value = "/api/student/education")
@AllArgsConstructor
public class EducationController extends BaseApiController {

    private final SubjectService subjectService;

    @RequestMapping(value = "/subject/list", method = RequestMethod.POST)
    public RestResponse<List<SubjectVM>> list() {
        User user = getCurrentUser();
        List<Subject> subjects = subjectService.getSubjectByLevel(user.getUserLevel());
        List<SubjectVM> subjectVMS = subjects.stream().map(d -> {
            SubjectVM subjectVM = modelMapper.map(d, SubjectVM.class);
            subjectVM.setId(String.valueOf(d.getId()));
            return subjectVM;
        }).collect(Collectors.toList());
        return RestResponse.ok(subjectVMS);
    }

    @RequestMapping(value = "/subject/select/{id}", method = RequestMethod.POST)
    public RestResponse<SubjectEditRequestVM> select(@PathVariable Integer id) {
        Subject subject = subjectService.selectById(id);
        SubjectEditRequestVM vm = modelMapper.map(subject, SubjectEditRequestVM.class);
        return RestResponse.ok(vm);
    }

}
