package com.rtrevorrow.user.authentication;

import com.rtrevorrow.user.model.User;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServerAuthenticationManager {

    public void processPolicy(User user) throws AuthenticationException {

        System.out.println("processPolicy");
    }

    @Transactional
    public void processAuthenticationSuccess() {
        System.out.println("success");
    }

    @Transactional
    public void processAuthenticationFailure() {
        System.out.println("failure");
    }
}
