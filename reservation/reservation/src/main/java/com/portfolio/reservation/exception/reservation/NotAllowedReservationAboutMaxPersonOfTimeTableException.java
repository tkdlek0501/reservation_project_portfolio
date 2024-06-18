package com.portfolio.reservation.exception.reservation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotAllowedReservationAboutMaxPersonOfTimeTableException extends CustomException {

    public NotAllowedReservationAboutMaxPersonOfTimeTableException() {
        super(ErrorCode.NOT_ALLOWED_RESERVATION_ABOUT_MAX_PERSON_TIME_TABLE);
    }
}
