package com.performity.useradmin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Petar Manev",
            email = "manevpe@gmail.com"
        //  url = "https://"
        ),
        description = "Open API documentation for Performity app - Useradmin service",
        title = "REST API docs - Performity - Useradmin service",
        version = "1.0.0"
    //  license = @License(
    //      name = "License name",
    //      url = "https://"
    //  )
    )
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {
}
