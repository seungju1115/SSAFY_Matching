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
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Profile("local")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomOAuth2UserService oauth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CorsConfigurationSource corsConfigurationSource;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable());

        http.cors(c -> c.configurationSource(corsConfigurationSource));

        http.authorizeHttpRequests(
                c ->
                        c.requestMatchers("/error", "/users/profile", "/users/login", "/login/oauth2/code/**", "/h2-console/**"
                                        , "/team", "/team/search", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/profile").permitAll()
                                .anyRequest().authenticated());

        CustomAuthorizationRequestResolver customResolver = 
                new CustomAuthorizationRequestResolver(clientRegistrationRepository, "/users/login");

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
