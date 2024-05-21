package com.portfolio.reservation.exception.user;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotMatchedPasswordException extends CustomException {

    public NotMatchedPasswordException() {
        super(ErrorCode.NOT_MATCHED_PASSWORD);
    }
}
