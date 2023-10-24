package com.performity.myservice;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@SpringBootApplication
@RestController
//@EnableDiscoveryClient
public class MyserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MyserviceApplication.class, args);
  }

  // Consul health check
  @Hidden
  @GetMapping("/actuator/health")
  public String getHealth() {
    return "Good";
  }

}
