package com.rtrevorrow.user.authentication;

import com.rtrevorrow.user.model.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {
    public static UserDetails getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        } else {
            log.warn("No instance of UserDetails in principal");
            return null;
        }
    }
}
