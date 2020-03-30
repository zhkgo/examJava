package com.zhk.examonline.configuration.spring.security;


import com.zhk.examonline.context.WebContext;
import com.zhk.examonline.domain.enums.RoleEnum;
import com.zhk.examonline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Description :  验证通过之后,第二、三...请求，会调用此类
 */

@Component
public class RestDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final WebContext webContext;

    @Autowired
    public RestDetailsServiceImpl(UserService userService, WebContext webContext) {
        this.userService = userService;
        this.webContext = webContext;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.zhk.examonline.domain.User user = userService.getUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username  not found.");
        }

        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(RoleEnum.fromCode(user.getRole()).getRoleName()));

        webContext.setCurrentUser(user);
        User authUser = new User(user.getUserName(), user.getPassword(), grantedAuthorities);
        return authUser;
    }
}
