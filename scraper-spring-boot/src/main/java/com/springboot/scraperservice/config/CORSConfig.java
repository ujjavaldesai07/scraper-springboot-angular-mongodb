package com.springboot.scraperservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS are set to client host only. so no one can access this APIs other then
 * the client website.
 */
@Configuration
@EnableWebMvc
public class CORSConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(System.getenv("CLIENT_URL"))
                .allowedMethods("GET");
    }
}