package com.portfolio.reservation.exception.operation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotFoundDateOperationException extends CustomException {

    public NotFoundDateOperationException() {
        super(ErrorCode.NOT_FOUND_DATEOPERATION);
    }
}
