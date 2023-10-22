package com.performity.useradmin.exceptions;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Data
@AllArgsConstructor
public class HttpErrorMessage {
  private int statusCode;
  private Date timestamp;
  private String message;
  private String path;
}