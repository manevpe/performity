package com.performity.useradmin;

import lombok.AllArgsConstructor;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class KeycloakService {

    static Keycloak keycloak = null;

    public void addUser(User user) {
//        CredentialRepresentation credential = KeycloakCredentials
//                .createPasswordCredentials(user.getPassword());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        //userRepresentation.setCredentials(Collections.singletonList(credential));
        userRepresentation.setEnabled(true);

        UsersResource instance = getInstance();
        instance.create(userRepresentation);
    }

    public UserRepresentation getUser(String email) {
        UsersResource usersResource = getInstance();
        List<UserRepresentation> user = usersResource.searchByEmail(email, true);
        return user.get(0);
    }

    public void updateUser(String email, User user) {
//        CredentialRepresentation credential = KeycloakCredentials
//                .createPasswordCredentials(user.getPassword());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
//        userRepresentation.setCredentials(Collections.singletonList(credential));

        String userId = getUser(email).getId();
        UsersResource usersResource = getInstance();
        usersResource.get(userId).update(userRepresentation);
    }

    public void deleteUser(String email) {
        String userId = getUser(email).getId();
        UsersResource usersResource = getInstance();
        usersResource.get(userId).remove();
    }


    public void sendVerificationLink(String email) {
        String userId = getUser(email).getId();
        UsersResource usersResource = getInstance();
        usersResource.get(userId).sendVerifyEmail();
    }

    public void sendResetPassword(String email) {
        String userId = getUser(email).getId();
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }

    @Autowired
    KeycloakConfig keycloakConfig;

    private void buildInstance() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakConfig.getServerUrl())
                // TODO - allow user to select a particular domain
                // realm(.keycloakConfig.getRealm())
                .realm("master")
                //.grantType(OAuth2Constants.PASSWORD)
                //.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .username(keycloakConfig.getUserName())
                .password(keycloakConfig.getPassword())
                .clientId(keycloakConfig.getClientId())
                .clientSecret(keycloakConfig.getClientSecret())
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build()
                )
                .build();
    }

    public UsersResource getInstance() {
        buildInstance();
        return keycloak.realm(keycloakConfig.getRealm()).users();
    }

}
