package com.portfolio.reservation.exception.operation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class OverlapTimeOperationException extends CustomException {

    public OverlapTimeOperationException() {
        super(ErrorCode.OVERLAP_TIME_OPERATION);
    }
}
