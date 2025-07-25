package com.example.demo.auth.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

//    private final UserRepository userRepository; // 회원 DB 연동 예시
//
//    public CustomOAuth2UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 구글로부터 받은 사용자 정보
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 예: 이메일 정보 추출
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        // 사용자 DB에 등록되어 있는지 확인하고 자동 회원가입 or 갱신
//        User user = userRepository.findByEmail(email)
//                .orElseGet(() -> userRepository.save(new User(email)));

        System.out.println(oauth2User);
        // 커스텀 OAuth2User 반환 가능
        return oauth2User;
    }
}
