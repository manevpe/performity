package com.performity.useradmin.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User is not authorized for this action.")
// 403
public class AccessDeniedException extends RuntimeException {
  public AccessDeniedException(String errorMessage) {
    super(errorMessage);
  }
}
