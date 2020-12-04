package com.springboot.scraperservice.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * Swagger is used to refer the APIs usage and test out the APIs. Swagger must always run in dev env.
 */

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@Profile({"dev"})
public class SwaggerConfig {

    @Bean
    public Docket api() {

        // build the swagger settings.
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.springboot.scraperservice.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {

        Contact contact = new Contact("Ujjaval Desai", "", "");

        return new ApiInfo(
                "Scraper REST Services API Documentation",
                "Scraper APIs for data access.",
                "1.0",
                "",
                contact,
                "License",
                "",
                new ArrayList<>()
        );
    }
}
