package com.zhk.examonline.viewmodel.student.user;


import com.zhk.examonline.domain.User;
import com.zhk.examonline.utility.DateTimeUtil;
import com.zhk.examonline.viewmodel.BaseVM;
import lombok.Data;


@Data
public class UserResponseVM extends BaseVM {

    private Integer id;

    private String userUuid;

    private String userName;

    private String realName;

    private Integer age;

    private String imagePath;

    private Integer role;

    private Integer sex;

    private String birthDay;

    private String phone;

    private String lastActiveTime;

    private String createTime;

    private String modifyTime;

    private Integer status;

    private Integer userLevel;

    public static UserResponseVM from(User user) {
        UserResponseVM vm = modelMapper.map(user, UserResponseVM.class);
        vm.setBirthDay(DateTimeUtil.dateFormat(user.getBirthDay()));
        vm.setLastActiveTime(DateTimeUtil.dateFormat(user.getLastActiveTime()));
        vm.setCreateTime(DateTimeUtil.dateFormat(user.getCreateTime()));
        vm.setModifyTime(DateTimeUtil.dateFormat(user.getModifyTime()));
        return vm;
    }
}
