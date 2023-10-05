package com.vacationplanner.useradmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
//@EnableDiscoveryClient
public class UseradminApplication {

    public static void main(String[] args) {
        SpringApplication.run(UseradminApplication.class, args);
    }

    @GetMapping("/actuator/health")
    public String getHealth() {
        return "Good";
    }

    @GetMapping
    public  String hello() {
        return "Hello World!";
    }

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<String> doOtherStuff() {
        return webClientBuilder.build().get().uri("http://stores/stores")
                .retrieve().bodyToMono(String.class);
    }
}
