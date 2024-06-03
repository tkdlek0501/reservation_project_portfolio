package com.portfolio.reservation.exception.operation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

import java.io.NotSerializableException;

public class NotSearchTimeTableException extends CustomException {

    public NotSearchTimeTableException() {
        super(ErrorCode.NOT_SEARCH_TIME_TABLE);
    }
}
