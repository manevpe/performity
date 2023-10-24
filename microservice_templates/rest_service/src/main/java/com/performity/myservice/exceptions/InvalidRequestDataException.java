package com.performity.myservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestDataException extends RuntimeException {
  public InvalidRequestDataException(String errorMessage) {
    super(errorMessage);
  }
}
