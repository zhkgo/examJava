package com.zhk.examonline.repository;

import com.zhk.examonline.domain.Subject;
import com.zhk.examonline.viewmodel.admin.education.SubjectPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubjectMapper extends BaseMapper<Subject> {
    int deleteByPrimaryKey(Integer id);

    int insert(Subject record);

    int insertSelective(Subject record);

    Subject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Subject record);

    int updateByPrimaryKey(Subject record);

    List<Subject> getSubjectByLevel(Integer level);

    List<Subject> allSubject();


    Subject getBySubject(Subject subject);

    List<Subject> page(SubjectPageRequestVM requestVM);
}
