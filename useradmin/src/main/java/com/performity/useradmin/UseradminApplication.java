package com.performity.useradmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
//@EnableDiscoveryClient
public class UseradminApplication {

    public static void main(String[] args) {
        SpringApplication.run(UseradminApplication.class, args);
    }

    // Consul health check
    @GetMapping("/actuator/health")
    public String getHealth() {
        return "Good";
    }

}
