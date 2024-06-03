package com.portfolio.reservation.exception.operation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotFoundDateTableException extends CustomException {

    public NotFoundDateTableException() {
        super(ErrorCode.NOT_FOUND_DATETABLE);
    }
}
