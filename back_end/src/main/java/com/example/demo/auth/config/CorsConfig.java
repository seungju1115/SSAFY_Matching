package com.example.demo.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Profile("local")
@Configuration
public class CorsConfig {
    @Value("${front.url}")
    private String frontUrl;
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 1. 특정 출처(Origin)를 명시적으로 허용합니다.
        config.setAllowedOrigins(List.of(frontUrl));

        // 2. 허용할 HTTP 메소드를 지정합니다.
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 3. 허용할 헤더를 지정합니다.
        config.setAllowedHeaders(List.of("*"));

        // 4. Credentials(인증정보)를 허용합니다. (가장 중요!)
        config.setAllowCredentials(true);

        return request -> config;
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("*"));
//        config.setAllowedMethods(List.of("*"));
//        config.setAllowedHeaders(List.of("*"));
//
//        return request -> config;
    }
}
