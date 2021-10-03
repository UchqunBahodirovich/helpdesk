package com.example.sdc.config;

import java.util.Optional;

        import io.swagger.v3.oas.models.OpenAPI;
        import io.swagger.v3.oas.models.info.Info;
        import org.springdoc.core.SpringDocConfigProperties;
        import org.springdoc.core.SpringDocConfiguration;
        import org.springframework.context.annotation.Bean;
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Digital Bank API").description(
                        "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.").version("v1"))
                .openapi("3.0.2");

    }

    @Bean
    SpringDocConfiguration springDocConfiguration() {
        return new SpringDocConfiguration();
    }

    @Bean
    SpringDocConfigProperties springDocConfigProperties() {
        return new SpringDocConfigProperties();
    }
}

