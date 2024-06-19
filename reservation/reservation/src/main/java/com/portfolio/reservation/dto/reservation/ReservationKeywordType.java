package com.portfolio.reservation.dto.reservation;

import lombok.Getter;

@Getter
public enum ReservationKeywordType {

    NAME("예약자명"), NO("예약번호");

    private String value;

    ReservationKeywordType(String value){
        this.value = value;
    }
}
