package com.performity.useradmin.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// TODO - define a global error handler class with unified error messaging
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid request data.")  // 400
public class InvalidRequestDataException extends RuntimeException {
  public InvalidRequestDataException(String errorMessage) {
    super(errorMessage);
  }
}
