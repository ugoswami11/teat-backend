package com.teat.teat_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI teatOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TEAT - Test Evidence & Action Tracker API")
                        .description("API to manage test runs, test cases, executions, and action items")
                        .version("v1"));
    }
}
