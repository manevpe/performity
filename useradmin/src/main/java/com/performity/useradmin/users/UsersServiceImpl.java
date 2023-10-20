package com.performity.useradmin.users;

import com.performity.useradmin.keycloak.KeycloakService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    getUserDetails(email);
    // When changing email, we need to first delete the old entry
    // TODO - this is not ideal - what would be a better approach?
    if (!email.equals(newUser.getEmail())) {
      deleteByEmail(email);
    }
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
