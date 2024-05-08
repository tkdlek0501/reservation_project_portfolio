package com.portfolio.reservation.dto.user;

import lombok.Getter;

@Getter
public class UserPasswordUpdateRequest {

    private String checkPassword;

    private String updatePassword;
}
