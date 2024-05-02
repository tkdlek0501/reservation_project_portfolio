package com.portfolio.reservation.dto.user;

import com.portfolio.reservation.domain.user.User;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

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
