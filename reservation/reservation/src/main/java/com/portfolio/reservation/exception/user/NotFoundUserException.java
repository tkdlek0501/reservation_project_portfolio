package com.portfolio.reservation.exception.user;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotFoundUserException extends CustomException {

    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
