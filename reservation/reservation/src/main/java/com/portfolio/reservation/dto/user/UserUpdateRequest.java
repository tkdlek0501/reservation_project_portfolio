package com.portfolio.reservation.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

    @Size(min = 4, max = 16, message = "닉네임은 최소 4자, 최대 16자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "닉네임은 영문자, 숫자, 언더스코어(_) 및 하이픈(-)만 포함할 수 있습니다.")
    private String nickname; // 닉네임

    /*
    TODO: 생각해보기)
     null 가능성이 있는 필드는 커스텀 getter 로 optional 로 만드는 방법
        vs. 그냥 코드상에서 null 처리?
    */
    public Optional<String> getNickname() {
        return Optional.ofNullable(this.nickname);
    }
}
