package com.performity.useradmin.users;

import com.performity.useradmin.keycloak.KeycloakService;
import com.performity.useradmin.users.exceptions.UserExistsException;
import com.performity.useradmin.users.exceptions.UserNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Service
public class UsersServiceImpl implements UsersService {
  @Autowired
  KeycloakService keycloakService;
  @Autowired
  private UsersRepository usersRepository;

  //@Autowired
  //private TenantIdentifierResolver currentTenant;

  public List<User> allUsers() {
    return usersRepository.findAll();
  }

  private User findByEmail(String email) {
    // currentTenant.setCurrentTenant("<tenant_name>");
    return usersRepository.findByEmail(email);
  }

  public User getUserDetails(String email) {
    User userData = findByEmail(email);
    if (userData == null) {
      throw new UserNotFoundException();
    }
    return userData;
  }

  public User createUser(User newUser) {
    User existingUser = findByEmail(newUser.getEmail());
    if (existingUser != null) {
      throw new UserExistsException();
    }

    keycloakService.addUser(newUser);
    usersRepository.save(newUser);

    return newUser;
  }

  public User updateByEmail(String email, User newUser) {
    User existingUser = getUserDetails(email);
    if (existingUser == null) {
      throw new UserNotFoundException();
    }
    if (!email.equals(newUser.getEmail()) && findByEmail(newUser.getEmail()) != null) {
      throw new UserExistsException();
    }
    newUser.setId(existingUser.getId());
    newUser.setDateCreated(existingUser.getDateCreated());
    keycloakService.updateUser(email, newUser);
    return usersRepository.save(newUser);
  }

  // TODO - Patch request

  public void deleteByEmail(String email) {
    getUserDetails(email);
    usersRepository.deleteByEmail(email);
    keycloakService.deleteUser(email);
  }

  // TODO - delete multiple
}
