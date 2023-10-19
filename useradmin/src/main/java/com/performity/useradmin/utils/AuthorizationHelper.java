package com.performity.useradmin.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AuthorizationHelper {
    public boolean checkAdminPermission(String roles) throws AccessDeniedException {
        if (roles == null) {
            throw new AccessDeniedException("Access denied");
        }
        String[] userRoles = roles.split(",");
        boolean isAdmin = Arrays.stream(userRoles).anyMatch(x -> "Admin".equals(x.trim()));
        if (!isAdmin) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
}
