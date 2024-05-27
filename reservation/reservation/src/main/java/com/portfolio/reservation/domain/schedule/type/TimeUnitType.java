package com.portfolio.reservation.domain.schedule.type;

import lombok.Getter;

@Getter
public enum TimeUnitType {

    HALF("30분"), HOUR("1시간");

    private String value;

    TimeUnitType(String value){
        this.value = value;
    }
}
