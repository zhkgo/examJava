package com.zhk.examonline.viewmodel.admin.user;

import com.zhk.examonline.base.BasePage;
import lombok.Data;



@Data
public class UserPageRequestVM extends BasePage {

    private String userName;
    private Integer role;

}
