package com.portfolio.reservation.exception.reservation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotAllowedReservationAboutSameTimeException extends CustomException {

    public NotAllowedReservationAboutSameTimeException() {
        super(ErrorCode.NOT_ALLOWED_RESERVATION_ABOUT_SAME_TIME);
    }
}
