package com.performity.useradmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.performity.useradmin.utils.JsonSchemaValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/useradmin/v1/users")
public class UserController {

    @Autowired
    private UsersService usersService;

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) throws AccessDeniedException {
        checkAdminPermission(request.getHeader("userRoles"));
        return new ResponseEntity<List<User>>(usersService.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByEmail(HttpServletRequest request, @PathVariable("id") String email) throws AccessDeniedException {
        // Allow user to read their own data, but only admins get read other users data.
        if (!email.equals(request.getHeader("userEmail"))) {
            checkAdminPermission(request.getHeader("userRoles"));
        }
        return new ResponseEntity<>(usersService.getUserDetails(email), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(HttpServletRequest request, @RequestBody String payload) throws AccessDeniedException, JsonProcessingException {
        checkAdminPermission(request.getHeader("userRoles"));
        JsonSchemaValidator.validate("model/user.schema.json", payload);
        return new ResponseEntity<User>(usersService.createUser(payload), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(HttpServletRequest request, @PathVariable("id") String email, String payload) throws AccessDeniedException, JsonProcessingException {
        checkAdminPermission(request.getHeader("userRoles"));
        JsonSchemaValidator.validate("model/user.schema.json", payload);
        return new ResponseEntity<>(usersService.updateByEmail(email, payload), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(HttpServletRequest request, @PathVariable("id") String email) throws AccessDeniedException {
        checkAdminPermission(request.getHeader("userRoles"));
        usersService.deleteByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean checkAdminPermission(String roles) throws AccessDeniedException {
        String[] userRoles = roles.split(", ");
        boolean isAdmin = Arrays.stream(userRoles).anyMatch(x -> "Admin".equals(x));
        if (!isAdmin) {
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }
}
