package com.portfolio.reservation.domain.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.reservation.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(length = 50, unique = true)
    private String username; // 아이디

    @JsonIgnore
    @Column(length = 100)
    private String password; // 비밀번호

    @Column(length = 50)
    private String nickname; // 닉네임

    @Enumerated(value = EnumType.STRING)
    private AuthorityType authority; // 권한

    // jwt 토큰 추가
    @Column(length = 1000)
    private String refreshToken;

    private LocalDateTime expiredAt;

    /* 정보 수정 */
    // 패스워드 수정
    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    // 닉네임 수정
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 회원 권한 수정
    public void updateAuthority(AuthorityType authorityType) {
        this.authority = authorityType;
    }

    // 토큰 업데이트 및 삭제
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }

    // 패스워드 암호화
    public static String encodePassword(String password, PasswordEncoder passwordEncoder){
        return passwordEncoder.encode(password);
    }

    // 비밀번호 확인; 입력 받은 비밀번호와 실제 비밀번호 일치 여부를 판단
    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassowrd) {
        return passwordEncoder.matches(checkPassowrd, this.password);
    }

    // 회원 가입시 user의 권한 설정
    public void addUserAuthority() {
        this.authority = AuthorityType.USER;
    }

    // 회원 탈퇴
    public void expire() {
        this.expiredAt = LocalDateTime.now();
    }
}

