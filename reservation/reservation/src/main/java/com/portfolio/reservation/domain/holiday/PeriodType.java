package com.portfolio.reservation.domain.holiday;

public enum PeriodType {

    DAY("특정 일자"), PERIOD("기간");

    private String value;

    PeriodType(String value) {
        this.value = value;
    }
}
