package com.portfolio.reservation.domain.schedule.type;

import lombok.Getter;

@Getter
public enum RegularHolidayUnitType {

    WEEKLY("매주"), BIWEEKLY("격주"), MONTHLY("매달");

    private String value;

    RegularHolidayUnitType(String value) {
        this.value = value;
    }
}
