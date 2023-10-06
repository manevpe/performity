package com.vacationplanner.useradmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public List<User> allUsers() {
        return usersRepository.findAll();
    }

    public User createUser(Map<String, Object> user) {
        // TODO - validation
        User newUser = new User(
                user.get("firstName").toString(),
                user.get("lastName").toString(),
                user.get("email").toString(),
                (List) user.get("teams"),
                0
        );
        usersRepository.save(newUser);

        return newUser;
    }
}
