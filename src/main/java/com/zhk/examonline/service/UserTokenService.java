package com.zhk.examonline.service;


import com.zhk.examonline.domain.User;
import com.zhk.examonline.domain.UserToken;

public interface UserTokenService extends BaseService<UserToken> {

    UserToken bind(User user);

    UserToken checkBind(String openId);

    UserToken getToken(String token);

    UserToken insertUserToken(User user);

    void unBind(UserToken userToken);
}
