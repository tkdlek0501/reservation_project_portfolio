package com.portfolio.reservation.exception.timetable;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotFoundTimeTableException extends CustomException {

    public NotFoundTimeTableException() {
        super(ErrorCode.NOT_FOUND_TIME_TABLE);
    }
}
