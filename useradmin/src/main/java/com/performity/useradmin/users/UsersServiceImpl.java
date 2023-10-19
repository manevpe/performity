package com.performity.useradmin.users;

import com.performity.useradmin.keycloak.KeycloakService;
import com.performity.useradmin.utils.TenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    KeycloakService keycloakService;

    @Autowired
    private TenantIdentifierResolver currentTenant;

    public List<User> allUsers() {
        return usersRepository.findAll();
    }

    private User findByEmail(String email) {
        // currentTenant.setCurrentTenant("<tenant_name>");
        User userData = usersRepository.findByEmail(email);
        return userData;
    }

    public User getUserDetails(String email) {
        User userData = findByEmail(email);
        if (userData == null) {
            throw new UserNotFoundException();
        }
        return userData;
    }

    public User createUser(User newUser) {
        User existingUser = findByEmail(newUser.getEmail().toString());
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

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="User with that email was not found.")  // 404
class UserNotFoundException extends RuntimeException {}

@ResponseStatus(value=HttpStatus.CONFLICT, reason="User already exists.")  // 409
class UserExistsException extends RuntimeException {}
