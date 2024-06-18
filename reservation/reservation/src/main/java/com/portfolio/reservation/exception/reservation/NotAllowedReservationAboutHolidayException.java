package com.portfolio.reservation.exception.reservation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotAllowedReservationAboutHolidayException extends CustomException {

    public NotAllowedReservationAboutHolidayException() {
        super(ErrorCode.NOT_ALLOWED_RESERVATION_ABOUT_HOLIDAY);
    }
}
