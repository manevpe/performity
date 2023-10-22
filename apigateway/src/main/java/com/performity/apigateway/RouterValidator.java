package com.performity.apigateway;

import java.util.List;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@UtilityClass
public class RouterValidator {

  public static final List<String> openApiEndpoints = List.of(
      "/actuator/health"
  );

  public static final Predicate<ServerHttpRequest> isSecured =
      request -> openApiEndpoints
          .stream()
          .noneMatch(uri -> request.getURI().getPath().contains(uri));

}