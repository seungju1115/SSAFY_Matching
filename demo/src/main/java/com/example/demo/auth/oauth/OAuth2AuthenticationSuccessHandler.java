package com.example.demo.auth.oauth;

import com.example.demo.auth.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 사용자 이메일 추출 (provider 마다 다를 수 있음)
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        String jwt = jwtUtil.generateToken(email, Map.of("name", name));
        System.out.println("JWT: " + jwt);
        // body에 담는 방법 주석처리
          // 응답 타입 및 인코딩 설정
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        // JSON 형식으로 응답 생성
//        String json = String.format("{\"accessToken\": \"%s\"}", jwt);
//
//        // 응답 출력
//        response.getWriter().write(json);
//        response.getWriter().flush();
        
//        // 헤더에 담기
//        response.setHeader("Authorization", "Bearer " + jwt);

        // 토큰 전송 테스트용 코드, 토큰을 쿼리 스트링에 담아서 리다이렉트
        String redirectUrl = "http://localhost:5173/oauth-success?token=" + jwt;
        response.sendRedirect(redirectUrl);
    }
}
