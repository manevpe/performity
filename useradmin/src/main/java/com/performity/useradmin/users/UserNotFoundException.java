package com.performity.useradmin.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User with that email was not found.")
class UserNotFoundException extends RuntimeException {
}
