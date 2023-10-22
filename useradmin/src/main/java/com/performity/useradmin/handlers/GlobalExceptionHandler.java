package com.performity.useradmin.handlers;

import com.performity.useradmin.exceptions.AccessDeniedException;
import com.performity.useradmin.exceptions.HttpErrorMessage;
import com.performity.useradmin.exceptions.InvalidRequestDataException;
import com.performity.useradmin.users.exceptions.UserExistsException;
import com.performity.useradmin.users.exceptions.UserNotFoundException;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  public HttpErrorMessage accessDeniedException(AccessDeniedException exception,
                                                WebRequest request) {
    return new HttpErrorMessage(
        HttpStatus.FORBIDDEN.value(),
        new Date(),
        exception.getMessage(),
        request.getDescription(false));
  }

  @ExceptionHandler(InvalidRequestDataException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public HttpErrorMessage invalidRequestDataException(InvalidRequestDataException exception,
                                                      WebRequest request) {
    return new HttpErrorMessage(
        HttpStatus.BAD_REQUEST.value(),
        new Date(),
        exception.getMessage(),
        request.getDescription(false));
  }

  @ExceptionHandler(UserExistsException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public HttpErrorMessage userExistsException(UserExistsException exception, WebRequest request) {
    return new HttpErrorMessage(
        HttpStatus.CONFLICT.value(),
        new Date(),
        exception.getMessage(),
        request.getDescription(false));
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public HttpErrorMessage userNotFoundException(UserNotFoundException exception,
                                                WebRequest request) {
    return new HttpErrorMessage(
        HttpStatus.NOT_FOUND.value(),
        new Date(),
        exception.getMessage(),
        request.getDescription(false));
  }
}
