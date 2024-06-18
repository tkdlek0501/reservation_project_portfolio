package com.portfolio.reservation.exception.reservation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotAllowedReservationAboutRequestMaxPersonException extends CustomException {

    public NotAllowedReservationAboutRequestMaxPersonException() {
        super(ErrorCode.NOT_ALLOWED_RESERVATION_ABOUT_REQUEST_MAX_PERSON);
    }
}
