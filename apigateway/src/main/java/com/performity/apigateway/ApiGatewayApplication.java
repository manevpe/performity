package com.performity.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@SpringBootApplication
@RestController
public class ApiGatewayApplication {
  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayApplication.class, args);
  }

  @GetMapping("/actuator/health")
  public String getHealth() {
    return "Good";
  }

}
