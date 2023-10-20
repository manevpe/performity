package com.performity.useradmin.keycloak;

import org.keycloak.representations.idm.CredentialRepresentation;

public class KeycloakCredentials {

  private KeycloakCredentials() {
    throw new IllegalStateException("Utility class");
  }

  public static CredentialRepresentation createPasswordCredentials(String password) {
    CredentialRepresentation passwordCredentials = new CredentialRepresentation();
    passwordCredentials.setTemporary(false);
    passwordCredentials.setType(CredentialRepresentation.PASSWORD);
    passwordCredentials.setValue(password);
    return passwordCredentials;
  }
}
