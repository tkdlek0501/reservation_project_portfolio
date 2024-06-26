package com.portfolio.reservation.domain.schedule.type;

import com.portfolio.reservation.exception.InvalidEnumValueException;
import lombok.Getter;

@Getter
public enum SameDayApprovalType {

    POSSIBLE("가능", 99), NONE("불가", 0), ONE("1시간 전까지", 1), TWO("2시간 전까지", 2), THREE("3시간 전까지", 3);

    private String value;

    private int hour;

    SameDayApprovalType(String value, int hour) {
        this.value = value;
        this.hour = hour;
    }

    public static SameDayApprovalType convertStringToEnum(String value) throws InvalidEnumValueException {
        try {
            return SameDayApprovalType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumValueException(value);
        }
    }
}
