package com.booking.bookingapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Booking Service API", version = "v1"),
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT",
        scheme = "bearer")
public class OpenApiConfig {
    @Value("${site.url}")
    private String siteUrl;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public OpenAPI myOpenApi() {
        Server server = new Server();
        server.setUrl(siteUrl + contextPath);
        server.setDescription("Server URL");

        return new OpenAPI().servers(List.of(server));
    }
}
