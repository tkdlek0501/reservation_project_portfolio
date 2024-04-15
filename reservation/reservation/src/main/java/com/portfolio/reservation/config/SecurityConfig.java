package com.portfolio.reservation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable() // token을 사용하는 방식이기 때문에 csrf를 disable
                .cors().disable()
                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 권한별 접근 설정
                .and()
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(
                                        "/health",
                                        "/swagger-ui/**",
                                        "/swagger-resources/**",
                                        "/v3/api-docs/**",
                                        "/v1/user/signup",
                                        "/v1/auth/authenticate")
                                .permitAll()
                                .anyRequest().authenticated()
                )

                // JwtFilter 설정
//                .apply(new JwtSecurityConfig(tokenProvider)) // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용

//                .and()
                .build();
    }
}
