package com.portfolio.reservation.common;

import com.portfolio.reservation.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomException extends RuntimeException {

    private String code;
    private String message;

    public CustomException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public CustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
