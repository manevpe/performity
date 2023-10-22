package com.performity.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Configuration
public class GatewayConfig {

  @Autowired
  AuthenticationFilter filter;

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("useradmin-route", r -> r.path("/useradmin/**")
            .filters(f -> f.filter(filter))
            .uri("lb://useradmin"))
        //  .route("auth-service", r -> r.path("/auth/**")
        //  .filters(f -> f.filter(filter))
        //  .uri("lb://auth-service"))

        .build();
  }

}
