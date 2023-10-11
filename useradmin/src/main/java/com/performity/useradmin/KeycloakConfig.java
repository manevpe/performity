package com.performity.useradmin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "keycloak.client")
public class KeycloakConfig {
    private String serverUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String userName;
    private String password;
}
