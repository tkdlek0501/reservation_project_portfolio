package com.portfolio.reservation.exception.store;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class AlreadyExistsStoreException extends CustomException {

    public AlreadyExistsStoreException() {
        super(ErrorCode.ALREADY_EXISTS_STORE);
    }
}
