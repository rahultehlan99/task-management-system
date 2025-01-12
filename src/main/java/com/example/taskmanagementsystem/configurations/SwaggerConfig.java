package com.example.taskmanagementsystem.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("AuthorizationHeader"))
                .components(new Components()
                        .addSecuritySchemes("AuthorizationHeader",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .name("Authorization")
                                        .in(SecurityScheme.In.HEADER)))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Task Management System")
                        .description("API with JWT Authorization header")
                        .version("1.0.0"));
    }
}

