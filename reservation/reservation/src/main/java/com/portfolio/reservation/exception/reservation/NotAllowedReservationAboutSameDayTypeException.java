package com.portfolio.reservation.exception.reservation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotAllowedReservationAboutSameDayTypeException extends CustomException {

    public NotAllowedReservationAboutSameDayTypeException(int hour) {
        super(ErrorCode.NOT_ALLOWED_RESERVATION_ABOUT_SAME_DAY_TYPE.getCode(), hour + ErrorCode.NOT_ALLOWED_RESERVATION_ABOUT_SAME_DAY_TYPE.getMsg());
    }
}
