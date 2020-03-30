package com.zhk.examonline.viewmodel.admin.message;

import com.zhk.examonline.base.BasePage;
import lombok.Data;

@Data
public class MessagePageRequestVM extends BasePage {
    private String sendUserName;
}
