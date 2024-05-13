package com.portfolio.reservation.exception.errorcode;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ALREADY_EXISTS_USER("US001", "이미 해당 아이디의 회원이 있습니다."),
    NOT_LOGIN_USER("US002", "로그인 정보가 없습니다. 다시 로그인 해주세요."),
    NOT_MATCHED_PASSWORD("US003", "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER("US004", "회원이 조회되지 않습니다.")
    ;

    private final String code;

    private final String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
