package com.portfolio.reservation.exception.operation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class OverlapDatesOperationException extends CustomException {

    public OverlapDatesOperationException() {
        super(ErrorCode.OVERLAP_DATEOPERATION);
    }
}
