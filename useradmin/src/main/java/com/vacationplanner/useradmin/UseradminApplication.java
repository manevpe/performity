package com.vacationplanner.useradmin;

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

//    @Autowired
//    private WebClient.Builder webClientBuilder;

//    public Mono<String> doOtherStuff() {
//        return webClientBuilder.build().get().uri("http://stores/stores")
//                .retrieve().bodyToMono(String.class);
//    }
}
