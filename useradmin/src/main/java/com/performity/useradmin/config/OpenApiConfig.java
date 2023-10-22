package com.performity.useradmin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * @author Petar Manev - <a href="https://github.com/manevpe">GitHub</a>
 */
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
public class OpenApiConfig {
}
