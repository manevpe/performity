package com.performity.useradmin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestDataException extends RuntimeException {
  public InvalidRequestDataException(String errorMessage) {
    super(errorMessage);
  }
}
