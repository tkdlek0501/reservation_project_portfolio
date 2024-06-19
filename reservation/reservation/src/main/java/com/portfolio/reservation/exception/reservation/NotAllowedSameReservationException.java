package com.portfolio.reservation.exception.reservation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotAllowedSameReservationException extends CustomException {

    public NotAllowedSameReservationException() {
        super(ErrorCode.NOT_ALLOWED_SAME_RESERVATION);
    }
}
