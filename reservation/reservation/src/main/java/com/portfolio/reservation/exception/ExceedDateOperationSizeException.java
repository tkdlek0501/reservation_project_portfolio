package com.portfolio.reservation.exception;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class ExceedDateOperationSizeException extends CustomException {

    public ExceedDateOperationSizeException() {
        super(ErrorCode.EXCEED_DATEOPERATION_SIZE);
    }
}
