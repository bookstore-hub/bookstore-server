package com.rdv.server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@OpenAPIDefinition(servers = { @Server(url = "https://"), @Server(url = "http://localhost:8080")})
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
    @Bean
    public OpenAPI rdvOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("RDV - Server")
                        .description("Service that exposes the RDV API via REST")
                        .version("1.0"))
                .servers(List.of(new io.swagger.v3.oas.models.servers.Server()
                        .url("http://localhost:8080")
                        .description("Development")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

}