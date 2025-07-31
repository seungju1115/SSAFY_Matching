package com.example.demo.auth.service;

import com.example.demo.auth.dao.UserRepository;
import com.example.demo.auth.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository; // 회원 DB 연동 예시

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 구글로부터 받은 사용자 정보
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        System.out.println(oauth2User);
        // 커스텀 OAuth2User 반환 가능
        return oauth2User;
    }
}
