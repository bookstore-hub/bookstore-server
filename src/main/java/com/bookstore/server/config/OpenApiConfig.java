package com.bookstore.server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8080")})
public class OpenApiConfig {
    @Bean
    public OpenAPI bookstoreOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Bookstore - Server")
                        .description("Service that exposes the Bookstore API via REST")
                        .version("1.0"))
                .servers(List.of(new io.swagger.v3.oas.models.servers.Server()
                        .url("http://localhost:8080")
                        .description("Development")));
    }

}