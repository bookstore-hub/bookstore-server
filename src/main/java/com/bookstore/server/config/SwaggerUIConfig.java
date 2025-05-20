package com.bookstore.server.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerUIConfig {

    @Bean
    GroupedOpenApi coreOpenAPI() {
        return GroupedOpenApi.builder()
                .group("Core logic")
                .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info().description("REST APIs exposed for Bookstore's core logic")))
                .packagesToScan("com.bookstore.server.core.controller")
                .build();

    }

}