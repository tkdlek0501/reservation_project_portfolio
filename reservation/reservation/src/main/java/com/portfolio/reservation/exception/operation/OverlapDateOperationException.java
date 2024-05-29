package com.portfolio.reservation.exception.operation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class OverlapDateOperationException extends CustomException {

    public OverlapDateOperationException() {
        super(ErrorCode.OVERLAP_DATEOPERATION);
    }
}
