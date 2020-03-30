package com.zhk.examonline.service.impl;


import com.zhk.examonline.configuration.property.SystemConfig;
import com.zhk.examonline.domain.User;
import com.zhk.examonline.service.AuthenticationService;
import com.zhk.examonline.service.UserService;
import com.zhk.examonline.utility.RsaUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserService userService;

    private final SystemConfig systemConfig;


    /**
     * @param username username
     * @param password password
     * @return boolean
     */
    @Override
    public boolean authUser(String username, String password) {
        User user = userService.getUserByUserName(username);
        return authUser(user, username, password);
    }


    @Override
    public boolean authUser(User user, String username, String password) {
        if (user == null) {
            return false;
        }
        String encodePwd = user.getPassword();
        if (null == encodePwd || encodePwd.length() == 0) {
            return false;
        }
        password=pwdDecode(password);
        String pwd1=pwdDecode(encodePwd);
        return pwd1.equals(password);
    }

    @Override
    public String pwdEncode(String password) {
        return RsaUtil.rsaEncode(systemConfig.getPwdKey().getPublicKey(), password);
    }

    @Override
    public String pwdDecode(String encodePwd) {
        return RsaUtil.rsaDecode(systemConfig.getPwdKey().getPrivateKey(), encodePwd);
    }


}
