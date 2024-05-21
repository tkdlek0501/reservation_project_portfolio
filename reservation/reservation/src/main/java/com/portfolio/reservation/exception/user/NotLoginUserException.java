package com.portfolio.reservation.exception.user;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotLoginUserException extends CustomException {

    public NotLoginUserException() {
        super(ErrorCode.NOT_LOGIN_USER);
    }
}
