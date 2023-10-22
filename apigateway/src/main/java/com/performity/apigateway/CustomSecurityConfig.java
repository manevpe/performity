package com.performity.apigateway;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {
  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  private String jwkSetUri;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(
            authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange()
                .authenticated())
        .oauth2ResourceServer(oauth2ResourceServer ->
            oauth2ResourceServer
                .jwt(jwt ->
                    jwt
                        .jwkSetUri(jwkSetUri)
                )
        );

    return http.build();
  }

}
