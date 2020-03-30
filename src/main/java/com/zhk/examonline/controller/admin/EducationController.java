package com.zhk.examonline.controller.admin;


import com.zhk.examonline.base.BaseApiController;
import com.zhk.examonline.base.RestResponse;
import com.zhk.examonline.domain.Subject;
import com.zhk.examonline.service.SubjectService;
import com.zhk.examonline.utility.PageInfoHelper;
import com.zhk.examonline.viewmodel.admin.education.SubjectEditRequestVM;
import com.zhk.examonline.viewmodel.admin.education.SubjectPageRequestVM;
import com.zhk.examonline.viewmodel.admin.education.SubjectResponseVM;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("AdminEducationController")
@RequestMapping(value = "/api/admin/education")
@AllArgsConstructor
public class EducationController extends BaseApiController {

    private final SubjectService subjectService;

    @RequestMapping(value = "/subject/list", method = RequestMethod.POST)
    public RestResponse<List<Subject>> list() {
        List<Subject> subjects = subjectService.allSubject();
        return RestResponse.ok(subjects);
    }

    @RequestMapping(value = "/subject/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<SubjectResponseVM>> pageList(@RequestBody SubjectPageRequestVM model) {
        PageInfo<Subject> pageInfo = subjectService.page(model);
        PageInfo<SubjectResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> {
            return modelMapper.map(e, SubjectResponseVM.class);
        });
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/subject/edit", method = RequestMethod.POST)
    public RestResponse<SubjectEditRequestVM> edit(@RequestBody @Valid SubjectEditRequestVM model) {
        Subject subject = modelMapper.map(model, Subject.class);
        if (model.getId() == null) {
            subjectService.insertByFilter(subject);
        } else {
            subjectService.updateByIdFilter(subject);
        }
        return RestResponse.ok(model);
    }

    @RequestMapping(value = "/subject/select/{id}", method = RequestMethod.POST)
    public RestResponse<SubjectEditRequestVM> select(@PathVariable Integer id) {
        Subject subject = subjectService.selectById(id);
        SubjectEditRequestVM vm = modelMapper.map(subject, SubjectEditRequestVM.class);
        return RestResponse.ok(vm);
    }

}
