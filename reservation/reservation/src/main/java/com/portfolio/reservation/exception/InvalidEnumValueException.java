package com.portfolio.reservation.exception;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class InvalidEnumValueException extends CustomException {

    public InvalidEnumValueException(String value) {
        super(ErrorCode.INVALID_ENUM_VALUE.getCode(), ErrorCode.INVALID_ENUM_VALUE.getMsg() + value);
    }
}
