package com.performity.useradmin.users;

import java.util.List;

public interface UsersService {
    List<User> allUsers();
    User getUserDetails(String email);
    User createUser(User newUser);
    User updateByEmail(String email, User newUser);
    void deleteByEmail(String email);
}
