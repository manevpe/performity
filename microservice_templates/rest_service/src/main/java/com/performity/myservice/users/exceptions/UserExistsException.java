package com.performity.myservice.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {
  public UserExistsException() {
    super("User already exists!");
  }
}
