package com.zhk.examonline.configuration.spring.mvc;

import com.zhk.examonline.configuration.property.SystemConfig;
import com.zhk.examonline.configuration.spring.interceptor.WebContextInterceptor;
import com.zhk.examonline.configuration.spring.wx.TokenHandlerInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;




@Configuration
@AllArgsConstructor
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    private final TokenHandlerInterceptor tokenHandlerInterceptor;
    private final SystemConfig systemConfig;
    private final WebContextInterceptor webContextInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webContextInterceptor);
        List<String> securityIgnoreUrls = systemConfig.getWx().getSecurityIgnoreUrls();
        String[] ignores = new String[securityIgnoreUrls.size()];
        registry.addInterceptor(tokenHandlerInterceptor)
                .addPathPatterns("/api/wx/**")
                .excludePathPatterns(securityIgnoreUrls.toArray(ignores));
        super.addInterceptors(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*");
        super.addCorsMappings(registry);
    }

}
