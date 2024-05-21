package com.portfolio.reservation.exception.store;

import com.portfolio.reservation.common.CustomException;
import com.portfolio.reservation.exception.errorcode.ErrorCode;

public class NotFoundStoreException extends CustomException {

    public NotFoundStoreException() {
        super(ErrorCode.NOT_FOUND_STORE);
    }
}
