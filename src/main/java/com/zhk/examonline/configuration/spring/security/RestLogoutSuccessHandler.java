package com.zhk.examonline.configuration.spring.security;


import com.zhk.examonline.base.SystemCode;
import com.zhk.examonline.domain.UserEventLog;
import com.zhk.examonline.event.UserEvent;
import com.zhk.examonline.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 用户登出
 *
 */
@Component
@AllArgsConstructor
public class RestLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        com.zhk.examonline.domain.User user = userService.getUserByUserName(springUser.getUsername());
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), user.getRealName(), new Date());
        userEventLog.setContent(user.getUserName() + " 登出了在线答题系统");
        eventPublisher.publishEvent(new UserEvent(userEventLog));
        RestUtil.response(response, SystemCode.OK);
    }
}
