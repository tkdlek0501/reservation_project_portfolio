package com.portfolio.reservation.dto.operation;

import com.portfolio.reservation.domain.timetable.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableResponse {

    private Long timeTableId;

    private LocalTime time;

    private int maxPersonOfTime;

    private Boolean available;

    public static TimeTableResponse of(TimeTableWithDateTableDto dto) {

        return TimeTableResponse.builder()
                .timeTableId(dto.getTimeTableId())
                .time(dto.getTime())
                .maxPersonOfTime(dto.getMaxPersonOfTime())
                .available(dto.getTimeAvailable())
                .build();
    }
}
