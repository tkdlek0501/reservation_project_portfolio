package com.portfolio.reservation.exception.schedule;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotFoundScheduleException extends CustomException {

    public NotFoundScheduleException() {
        super(ErrorCode.NOT_FOUND_SCHEDULE);
    }
}
