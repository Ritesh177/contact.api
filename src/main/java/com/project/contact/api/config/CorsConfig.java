package com.project.contact.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("http://localhost:3000") // Allow only your frontend domain
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD") // Allow specific HTTP methods
                .allowCredentials(true) // Allow cookies to be sent with the request
                .maxAge(3600); // Cache preflight response for 1 hour
    }
}
