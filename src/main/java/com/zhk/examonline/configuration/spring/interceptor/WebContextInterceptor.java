package com.zhk.examonline.configuration.spring.interceptor;


import com.zhk.examonline.context.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class WebContextInterceptor implements HandlerInterceptor {

    @Autowired
    protected WebContext webContext;

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        webContext.clean();
    }
}