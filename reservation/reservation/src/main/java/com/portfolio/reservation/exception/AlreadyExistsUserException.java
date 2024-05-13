package com.portfolio.reservation.exception;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;
import lombok.NoArgsConstructor;

public class AlreadyExistsUserException extends CustomException {

    public AlreadyExistsUserException() {
        super(ErrorCode.ALREADY_EXISTS_USER);
    }
}
