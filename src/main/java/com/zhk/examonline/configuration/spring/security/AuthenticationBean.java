package com.zhk.examonline.configuration.spring.security;

import lombok.Data;

@Data
public class AuthenticationBean {
    private String userName;
    private String password;
    private boolean remember;
}
