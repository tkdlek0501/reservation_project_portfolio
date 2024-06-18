package com.portfolio.reservation.exception.reservation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotAllowedReservationAboutRequestMinPersonException extends CustomException {

    public NotAllowedReservationAboutRequestMinPersonException() {
        super(ErrorCode.NOT_ALLOWED_RESERVATION_ABOUT_REQUEST_MIN_PERSON);
    }
}
