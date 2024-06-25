package com.portfolio.reservation.exception.reservation;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotAllowedConfirmChangeException extends CustomException {

    public NotAllowedConfirmChangeException() {
        super(ErrorCode.NOT_ALLOWED_CONFIRM_CHANGE);
    }
}
