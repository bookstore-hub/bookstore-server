package com.rdv.server.config;

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
                .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info().description("REST APIs exposed for RDV's core logic")))
                .packagesToScan("com.rdv.server.core.controller")
                .build();

    }

    @Bean
    GroupedOpenApi authenticationOpenAPI() {
        return GroupedOpenApi.builder()
                .group("Authentication logic")
                .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info().description("REST APIs exposed for RDV's authentication logic")))
                .packagesToScan("com.rdv.server.authentication.controller")
                .build();

    }

    @Bean
    GroupedOpenApi accountManagementOpenAPI() {
        return GroupedOpenApi.builder()
                .group("Account management logic")
                .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info().description("REST APIs exposed for RDV's account management logic")))
                .packagesToScan("com.rdv.server.account.controller")
                .build();
    }

    @Bean
    GroupedOpenApi fileStorageOpenAPI() {
        return GroupedOpenApi.builder()
                .group("File storage logic")
                .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info().description("REST APIs exposed for RDV's file storage logic")))
                .packagesToScan("com.rdv.server.storage.controller")
                .build();

    }

    @Bean
    GroupedOpenApi chatOpenAPI() {
        return GroupedOpenApi.builder()
                .group("Chat logic")
                .addOpenApiCustomizer(openApi -> openApi.setInfo(new Info().description("REST APIs exposed for RDV's chat logic")))
                .packagesToScan("com.rdv.server.chat.controller")
                .build();

    }

}