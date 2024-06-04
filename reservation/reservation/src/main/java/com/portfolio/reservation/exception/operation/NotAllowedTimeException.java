package com.portfolio.reservation.exception.operation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotAllowedTimeException extends CustomException {

    public NotAllowedTimeException() {
        super(ErrorCode.NOT_ALLOWED_TIME);
    }
}
