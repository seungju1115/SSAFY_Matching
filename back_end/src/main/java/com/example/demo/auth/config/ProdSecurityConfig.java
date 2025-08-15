package com.example.demo.auth.config;

import com.example.demo.auth.filter.JwtFilter;
import com.example.demo.auth.handler.OAuth2AuthenticationFailureHandler;
import com.example.demo.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.example.demo.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Profile("prod")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ProdSecurityConfig {


    @Value("${front.url}")
    private String frontUrl;

    private final JwtFilter jwtFilter;
    private final CustomOAuth2UserService oauth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
//    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable());

//        http.cors(c -> c.configurationSource(corsConfigurationSource));
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of(frontUrl)); // 프론트엔드 Origin 명시적 허용
            config.setAllowedMethods(List.of("*"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        }));

        http.authorizeHttpRequests(
                c ->
                        c.requestMatchers("/error", "/users/login",
                                        "/login/oauth2/code/**", "/h2-console/**", "/ws-chat/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/team", "/team/search", "/users/profile", "/hello","/users/profile/waiting", "/cache/**", "/dashboard/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/profile","/users/profile/search").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/cache/**").permitAll()
                                .requestMatchers("/chatroom/**", "/ws-chat/**").authenticated()
                                .anyRequest().authenticated());

        http.oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorization -> authorization
                        .baseUri("/users/login"))
                .redirectionEndpoint(redirection -> redirection
                        .baseUri("/login/oauth2/code/*"))
                .userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
        );

        // h2-console 이용을 위한 설정
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable()));

        // JWT 인증 필터를 OAuth2 로그인 필터 앞이나 앞에 추가
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
