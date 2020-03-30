package com.zhk.examonline.viewmodel.admin.user;

import com.zhk.examonline.base.BasePage;
import lombok.Data;


@Data
public class UserEventPageRequestVM extends BasePage {

    private Integer userId;

    private String userName;

}
