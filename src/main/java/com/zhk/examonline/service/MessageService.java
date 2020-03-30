package com.zhk.examonline.service;


import com.github.pagehelper.PageInfo;
import com.zhk.examonline.domain.Message;
import com.zhk.examonline.domain.MessageUser;
import com.zhk.examonline.viewmodel.admin.message.MessagePageRequestVM;
import com.zhk.examonline.viewmodel.student.user.MessageRequestVM;

import java.util.List;

public interface MessageService {
    List<Message> selectMessageByIds(List<Integer> ids);

    PageInfo<MessageUser> studentPage(MessageRequestVM requestVM);

    PageInfo<Message> page(MessagePageRequestVM requestVM);

    List<MessageUser> selectByMessageIds(List<Integer> ids);

    void sendMessage(Message message, List<MessageUser> messageUsers);


    Message messageDetail(Integer id);
    Integer unReadCount(Integer userId);
    void read(Integer id);
}
