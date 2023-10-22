package com.performity.useradmin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
// 403
public class AccessDeniedException extends RuntimeException {
  public AccessDeniedException(String errorMessage) {
    super(errorMessage);
  }
}
