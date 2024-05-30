package com.portfolio.reservation.dto.operation;

import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableWithDateTableDto {

    // dateTable
    private Long dateTableId;

    private TimeUnitType timeUnit;

    private LocalDate date;

    private int maxPersonOfDay;

    private Boolean dateAvailable;

    private Boolean isHourlySetting;

    // timeTable
    private Long timeTableId;

    private LocalTime time;

    private int maxPersonOfTime;

    private Boolean timeAvailable;
}
