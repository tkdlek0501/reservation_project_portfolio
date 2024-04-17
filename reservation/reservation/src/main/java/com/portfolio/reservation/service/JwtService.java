package com.portfolio.reservation.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.reservation.auth.CustomUserPrincipal;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class JwtService implements InitializingBean  {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private long accessTokenValidityInSeconds;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenValidityInSeconds;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERNAME_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        // *
        byte[] keyBytes = Decoders.BASE64.decode(secret);

//        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private Map<String, Object> makeClaimContents(CustomUserPrincipal principal) {
        Map<String, Object> claims = new HashMap<>();

//        claims.put("id", principal.getId());
        claims.put("username", principal.getUsername());
        claims.put("nickname", principal.getNickname());
        claims.put("type", principal.getAuthorities());

        Map<String, Object> store = new HashMap<>();
//        store.put("id", principal.getStoreId());
        claims.put("store", store);

        return claims;
    }

    public String createAccessToken(Authentication authentication) {

        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000))
                .setClaims(makeClaimContents(customUserPrincipal))
                .signWith(key, SignatureAlgorithm.HS512) // secret 값, 암호화 알고리즘 세팅
                .compact();
    }

    public String createRefreshToken() {

        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds  * 1000))
                .signWith(key, SignatureAlgorithm.HS512) // secret 값, 암호화 알고리즘 세팅
                .compact();
    }

    public void updateRefreshToken(String username, String refreshToken) {
        userRepository.findOneByUsername(username)
                .ifPresentOrElse(
                        users -> users.updateRefreshToken(refreshToken),
                        () -> new Exception("회원 조회 실패")
                );
    }

    public void destroyRefreshToken(String username) {
        userRepository.findOneByUsername(username)
                .ifPresentOrElse(
                        users -> users.destroyRefreshToken(),
                        () -> new Exception("회원 조회 실패")
                );
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);

    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader)).filter(
                accessToken -> accessToken.startsWith(BEARER)
        ).map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Authentication getAuthentication(String accessToken) {
//        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return Optional.empty();
//        }

        User user = userRepository.findById(Long.valueOf(claims.get("id").toString())).orElseThrow(() -> new UsernameNotFoundException("데이터베이스에서 찾을 수 없습니다."));
        UserDetails userDetails = CustomUserPrincipal.of(user);

        return new UsernamePasswordAuthenticationToken(user, accessToken, userDetails.getAuthorities());
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
