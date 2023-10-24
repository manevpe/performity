package com.performity.myservice.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("User with that email was not found!");
  }
}
