package com.portfolio.reservation.exception.timeoperation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotFoundTimeOperationException extends CustomException {

    public NotFoundTimeOperationException() {
        super(ErrorCode.NOT_FOUND_TIME_OPERATION);
    }
}
