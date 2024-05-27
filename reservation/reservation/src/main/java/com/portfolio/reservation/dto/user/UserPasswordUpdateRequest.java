package com.portfolio.reservation.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserPasswordUpdateRequest {

    @NotNull(message = "변경전 비밀번호는 필수값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자, 최대 20자여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "비밀번호는 최소 하나의 대문자, 소문자, 숫자 및 특수 문자를 포함해야 합니다."
    )
    private String checkPassword;

    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자, 최대 20자여야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "비밀번호는 최소 하나의 대문자, 소문자, 숫자 및 특수 문자를 포함해야 합니다."
    )
    private String updatePassword;
}
