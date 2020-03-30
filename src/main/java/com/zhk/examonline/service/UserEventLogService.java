package com.zhk.examonline.service;

import com.github.pagehelper.PageInfo;
import com.zhk.examonline.domain.UserEventLog;
import com.zhk.examonline.viewmodel.admin.user.UserEventPageRequestVM;

import java.util.List;

public interface UserEventLogService extends BaseService<UserEventLog> {

    List<UserEventLog> getUserEventLogByUserId(Integer id);

    PageInfo<UserEventLog> page(UserEventPageRequestVM requestVM);

    List<Integer> selectMothCount();
}
