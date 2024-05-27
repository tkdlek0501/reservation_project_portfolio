package com.portfolio.reservation.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.reservation.domain.user.AuthorityType;
import com.portfolio.reservation.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {

    @Setter
    @NotNull(message = "아이디는 필수값입니다.")
    @Size(min = 4, max = 16, message = "아이디는 최소 4자, 최대 16자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문자와 숫자만 포함할 수 있습니다.")
    private String username; // 아이디

    @Setter
    @NotNull(message = "비밀번호는 필수값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자, 최대 20자여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "비밀번호는 최소 하나의 대문자, 소문자, 숫자 및 특수 문자를 포함해야 합니다."
    )
    private String password; // 비밀번호

    @Setter
    @NotNull(message = "닉네임은 필수값입니다.")
    @Size(min = 4, max = 16, message = "닉네임은 최소 4자, 최대 16자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "닉네임은 영문자, 숫자, 언더스코어(_) 및 하이픈(-)만 포함할 수 있습니다.")
    private String nickname; // 닉네임

    @Setter
    @NotNull(message = "유저 권한이 설정되지 않았습니다.")
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
