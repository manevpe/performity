package com.performity.useradmin.utils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AuthorizationHelperTests {

  private final AuthorizationHelper authorizationHelper = new AuthorizationHelper();

  @Test
  void authorizationHelper_Check_Admin_Permission() {
    Boolean valid = authorizationHelper.checkAdminPermission("User, Admin, Manager");
    Assertions.assertThat(valid).isTrue();
  }

  @Test
  void authorizationHelper_Check_Admin_Permission_Fail() {
    assertThatThrownBy(() ->
        authorizationHelper.checkAdminPermission("User, NonAdmin, Manager")
    ).isInstanceOf(AccessDeniedException.class);
    assertThatThrownBy(() ->
        authorizationHelper.checkAdminPermission(null)
    ).isInstanceOf(AccessDeniedException.class);
  }
}
