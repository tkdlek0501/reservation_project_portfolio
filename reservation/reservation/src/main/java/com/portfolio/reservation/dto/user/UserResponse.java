package com.portfolio.reservation.dto.user;

import com.portfolio.reservation.domain.user.AuthorityType;
import com.portfolio.reservation.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String username;

    private String nickname;

    private AuthorityType authority;

    public static UserResponse of(User user) {

        return UserResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .authority(user.getAuthority())
                .build();

    }
}
