package com.zhk.examonline;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhk.examonline.domain.User;
import com.zhk.examonline.repository.UserMapper;
import com.zhk.examonline.viewmodel.admin.user.UserPageRequestVM;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;
    @Test
    public void testStudentPage(){
        UserPageRequestVM vm=new UserPageRequestVM();
        vm.setRole(1);
        vm.setPageIndex(1);
        vm.setPageSize(10);
        List<User> users=userMapper.userPage(vm);
        log.info("getUsers {},firstUser is {}",users.size(),users.get(0).toString());
        PageInfo<User> pages=PageHelper.startPage(1,10).doSelectPageInfo(()->{userMapper.userPage(vm);});
        log.info(pages.toString());

    }
}
