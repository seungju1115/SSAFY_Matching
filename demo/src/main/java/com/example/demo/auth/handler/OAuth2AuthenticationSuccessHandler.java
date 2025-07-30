package com.example.demo.auth.handler;

import com.example.demo.auth.dao.UserRepository;
import com.example.demo.auth.entity.User;
import com.example.demo.auth.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${front.url}")
    private String url;

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 사용자 이메일 추출 (provider 마다 다를 수 있음)
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email").toString();
        String username = oAuth2User.getAttribute("name").toString();

        Optional<User> user = userRepository.findByEmail(email);

        String jwt = jwtUtil.generateToken(email, Map.of("name", username, "role", "student"));
//        System.out.println("JWT: " + jwt);

        // Cookie에 담기
//        Cookie cookie = new Cookie("jwt", jwt);
//        cookie.setHttpOnly(true);

        // 토큰 전송 테스트용 코드, 토큰을 쿼리 스트링에 담아서 리다이렉트
        if (user.isEmpty()) {
            getRedirectStrategy().sendRedirect(request, response, url + "/signup");
        } else {
            getRedirectStrategy().sendRedirect(request, response, url + "/main?token=" + jwt);
        }
    }
}
