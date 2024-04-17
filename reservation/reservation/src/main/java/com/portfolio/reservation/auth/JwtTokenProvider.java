package com.portfolio.reservation.auth;

import com.portfolio.reservation.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean {

    // TODO: JwtService로 대체해서 해당 class 삭제 필요

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long tokenValidatyInMilliseconds;
    private Key key;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public JwtTokenProvider(
            // TODO: properties 설정
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds
    ) {
        this.secret = secret;
        this.tokenValidatyInMilliseconds = tokenValidityInSeconds * 1000;
    }

    // 빈이 생성되고 주입을 받은 후에 secret값을 Base64 Decode해서 key 변수에 할당하기 위해
    @Override
    public void afterPropertiesSet() {
        // *
        byte[] keyBytes = Decoders.BASE64.decode(secret);

//        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {

        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        // 토큰 expire 시간 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidatyInMilliseconds);

        return Jwts.builder()
//                .setSubject(customUserPrincipal.getId().toString())
                .signWith(key, SignatureAlgorithm.HS512) // secret 값, 암호화 알고리즘 세팅
                .setExpiration(validity) // expire 시간 설정
                .setClaims(makeClaimContents(customUserPrincipal))
                .compact();
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

    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails user = userDetailsService.loadUserByUserId(Long.valueOf(claims.get("id").toString()));

        return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    }

    // 토큰의 유효성 검증을 수행
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

