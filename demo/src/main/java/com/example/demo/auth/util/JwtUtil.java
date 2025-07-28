package com.example.demo.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // 비밀 키 (HS256 사용)
    private Key secretKey;

    // 토큰 유효 시간 (예: 1시간)
    private long accessTokenValidity;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        accessTokenValidity = jwtExpiration;
    }
    /**
     * JWT 토큰 생성
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setSubject(subject) // 사용자 email
                .addClaims(claims)   // 추가 클레임 name
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에서 클레임 추출
     */
    public Claims parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token) // 예외 발생 가능
                .getBody();
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 사용자 ID(혹은 subject) 추출
     */
    public String getSubject(String token) {
        return parseToken(token).getSubject();
    }

    // 핵심: 토큰에서 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        String email = getSubject(token);
        GrantedAuthority a = new SimpleGrantedAuthority("student");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                email,
                null,
                List.of(a));
        return auth;
    }
}
