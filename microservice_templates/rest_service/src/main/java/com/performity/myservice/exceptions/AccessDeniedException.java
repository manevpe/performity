package com.performity.myservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
// 403
public class AccessDeniedException extends RuntimeException {
  public AccessDeniedException(String errorMessage) {
    super(errorMessage);
  }
}
