package com.portfolio.reservation.exception.reservation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotFoundReservationException extends CustomException {

    public NotFoundReservationException() {
        super(ErrorCode.NOT_FOUND_RESERVATION);
    }
}
