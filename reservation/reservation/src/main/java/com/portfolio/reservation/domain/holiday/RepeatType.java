package com.portfolio.reservation.domain.holiday;

import lombok.Getter;

@Getter
public enum RepeatType {

    WEEKLY("매주"), BIWEEKLY("격주"), MONTHLY("매달");

    private String value;

    RepeatType(String value) {
        this.value = value;
    }
}
