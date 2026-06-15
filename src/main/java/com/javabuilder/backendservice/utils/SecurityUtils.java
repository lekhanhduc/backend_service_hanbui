package com.javabuilder.backendservice.utils;

import com.javabuilder.backendservice.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static List<String> getAuthorities(User user) {
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }
}
