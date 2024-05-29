package com.portfolio.reservation.exception.operation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class ExceedDateOperationDateException extends CustomException {

    public ExceedDateOperationDateException() {
        super(ErrorCode.EXCEED_OPERATION_DATE);
    }
}
