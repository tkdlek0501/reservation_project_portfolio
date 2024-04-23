package com.portfolio.reservation.filter;

import com.portfolio.reservation.auth.CustomUserPrincipal;
import com.portfolio.reservation.domain.user.User;
import com.portfolio.reservation.repository.UserRepository;
import com.portfolio.reservation.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository usersRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private final String NO_CHECK_URL = "/login";

    /**
     * RefreshToken 이 오는 경우 -> 유효하면 AccessToken 재발급후, 필터 진행 X, 바로 튕기기
     *
     * RefreshToken은 없고 AccessToken만 있는 경우 -> 유저정보 저장후 필터 계속 진행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL) || request.getRequestURI().contains("/h2-console")) {
            filterChain.doFilter(request, response);
            return; // 로그인 url 은 여기 필터에서 제외
        }

        String refreshToken = jwtService
                .extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);


        if(refreshToken != null){
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .flatMap(accessToken -> jwtService.extractUsername(accessToken)
                .flatMap(usersRepository::findOneByUsername)).ifPresent(this::saveAuthentication);

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(User user) {
        CustomUserPrincipal userDetails = new CustomUserPrincipal(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();//5
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        usersRepository.findByRefreshToken(refreshToken).ifPresent(
                users -> jwtService.sendAccessToken(response, jwtService.createAccessToken(users.getUsername()))
        );


    }
}
