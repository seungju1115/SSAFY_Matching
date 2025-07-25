package com.example.demo.auth.config;

import com.example.demo.auth.oauth.OAuth2AuthenticationFailureHandler;
import com.example.demo.auth.oauth.OAuth2AuthenticationSuccessHandler;
import com.example.demo.auth.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    private final JwtAuthenticationFilter jwtFilter;
    private final CustomOAuth2UserService oauth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    public SecurityConfig(CustomOAuth2UserService oauth2UserService,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler/*,
                          JwtAuthenticationFilter jwtFilter*/) {
        this.oauth2UserService = oauth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
//        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (필요에 따라)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/users/login").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                         .userInfoEndpoint(userInfo -> userInfo
                             .userService(oauth2UserService) // 사용자 정보 후처리 필요 시 주입
                         ).successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(oAuth2AuthenticationFailureHandler)
                );
            // JWT 인증 필터를 OAuth2 로그인 필터 앞이나 후에 추가, user 스키마가 완성되면 작성
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
