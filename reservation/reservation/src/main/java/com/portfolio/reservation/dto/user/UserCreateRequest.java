package com.portfolio.reservation.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.reservation.domain.user.AuthorityType;
import com.portfolio.reservation.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {

    private String username; // 아이디

    private String password; // 비밀번호

    private String nickname; // 닉네임

    private AuthorityType authority; // 권한

    public static User toEntity(UserCreateRequest request, PasswordEncoder passwordEncoder) {

        return User.builder()
                .username(request.getUsername())
                .password(User.encodePassword(request.getPassword(), passwordEncoder))
                .nickname(request.getNickname())
                .authority(request.getAuthority())
                .build();
    }
}
