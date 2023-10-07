package com.vacationplanner.useradmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public List<User> allUsers() {
        return usersRepository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public User createUser(Map<String, Object> user) {
        // TODO - validation.
        // TODO - return 409 on duplicate email
        User newUser = new User(
                user.get("email").toString(),
                user.get("firstName").toString(),
                user.get("lastName").toString(),
                (List) user.get("teams"),
                0
        );
        usersRepository.save(newUser);

        return newUser;
    }

    public User updateByEmail(User newUser) {
        // TODO - when trying to change email, a new entry is created instead.
        return usersRepository.save(newUser);
    }

    public void deleteByEmail(String email) {
        // TODO - when not found, return an error
        usersRepository.deleteByEmail(email);
    }

    // TODO - delete multiple
}
