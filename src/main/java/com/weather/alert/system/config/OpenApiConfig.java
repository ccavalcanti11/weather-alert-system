package com.weather.alert.system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI weatherAlertSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Weather Alert System API")
                        .description("Real-time weather alert notification system")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Weather Alert System")
                                .email("support@weatheralert.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server")));
    }
}
