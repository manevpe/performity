package com.performity.useradmin.users;

import java.util.List;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
public interface UsersService {
  List<User> allUsers();

  User getUserDetails(String email);

  User createUser(User newUser);

  User updateByEmail(String email, User newUser);

  void deleteByEmail(String email);
}
