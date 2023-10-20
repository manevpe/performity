package com.performity.apigateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();

    if (RouterValidator.isSecured.test(request)) {
      if (this.isAuthMissing(request)) {
        return this.onError(exchange, "Authorization header is missing in request",
            HttpStatus.UNAUTHORIZED);
      }

      final String token = this.getAuthHeader(request);

      try {
        if (jwtUtil.isInvalid(token)) {
          return this.onError(exchange, "Authorization header is invalid",
              HttpStatus.UNAUTHORIZED);
        }
      } catch (Exception e) {
        throw new JwtException(e.getMessage());
      }

      try {
        this.populateRequestWithHeaders(exchange, token);
      } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
        throw new RuntimeException(e);
      }
    }
    return chain.filter(exchange);
  }


  private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
    logger.error(err);
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    return response.setComplete();
  }

  private String getAuthHeader(ServerHttpRequest request) {
    return request.getHeaders().getOrEmpty("Authorization").get(0);
  }

  private boolean isAuthMissing(ServerHttpRequest request) {
    return !request.getHeaders().containsKey("Authorization");
  }

  private void populateRequestWithHeaders(ServerWebExchange exchange, String token)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    token = token.replace("Bearer ", "").trim();

    Claims claims = jwtUtil.getAllClaimsFromToken(token);
    exchange.getRequest().mutate()
        .header("userEmail", claims.get("email").toString())
        //TODO - use proper json mapper
        .header("userRoles", claims.get("realm_access").toString()
            .replace("{roles=[", "").replace("]}", ""))
        .build();
  }
}
