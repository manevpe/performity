package com.vacationplanner.useradmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UsersService usersService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(usersService.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("id") String email) {
        Optional<User> userData = usersService.findByEmail(email);
        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<User>(usersService.createUser(payload), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateTutorial(@PathVariable("id") String email, @RequestBody User user) {
        // TODO - move this business logic into the service and handle exceptions here
        // TODO - generic exception handler for all methods?
        Optional<User> userData = usersService.findByEmail(email);
        if (userData.isPresent()) {
            return new ResponseEntity<>(usersService.updateByEmail(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") String email) {
        Optional<User> userData = usersService.findByEmail(email);
        if (userData.isPresent()) {
            usersService.deleteByEmail(email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
