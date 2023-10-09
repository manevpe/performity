package com.vacationplanner.apigateway;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            //.antMatchers("/index.html", "/**.js", "/**.css", "/").permitAll()
            .authorizeExchange(
                    authorizeExchangeSpec -> authorizeExchangeSpec.anyExchange()
                            .authenticated())
            .oauth2ResourceServer((oauth2ResourceServer) ->
                oauth2ResourceServer
                    .jwt((jwt) ->
                        jwt
                            // TODO - remove hardcoded value
                            .jwkSetUri("http://localhost:8180/realms/dundermifflin/protocol/openid-connect/certs")
                    )
            );

        //.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        //.addFilterAfter(createPolicyEnforcerFilter(), BearerTokenAuthenticationFilter.class);

        return http.build();
    }
}
