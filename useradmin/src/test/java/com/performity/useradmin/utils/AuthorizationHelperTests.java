package com.performity.useradmin.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AuthorizationHelperTests {

    private AuthorizationHelper authorizationHelper = new AuthorizationHelper();

    @Test
    public void AuthorizationHelper_Check_Admin_Permission() {
        Boolean valid = authorizationHelper.checkAdminPermission("User, Admin, Manager");
        Assertions.assertThat(valid).isTrue();
    }

    @Test
    public void AuthorizationHelper_Check_Admin_Permission_Fail() {
        assertThatThrownBy(() -> {
            authorizationHelper.checkAdminPermission("User, NonAdmin, Manager");
        }).isInstanceOf(AccessDeniedException.class);
        assertThatThrownBy(() -> {
            authorizationHelper.checkAdminPermission(null);
        }).isInstanceOf(AccessDeniedException.class);
    }
}
