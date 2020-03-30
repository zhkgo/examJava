package com.zhk.examonline.repository;


import com.zhk.examonline.domain.Message;
import com.zhk.examonline.viewmodel.admin.message.MessagePageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<Message> page(MessagePageRequestVM requestVM);

    List<Message> selectByIds(List<Integer> ids);

    int readAdd(Integer id);
}
