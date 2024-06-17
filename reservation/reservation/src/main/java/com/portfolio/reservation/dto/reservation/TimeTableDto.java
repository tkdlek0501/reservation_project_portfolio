package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.timetable.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableDto {


    private TimeTable timeTable;

    private Long maxPerson;

    public static TimeTableDto of(TimeTable timeTable, Long maxPerson) {

        return TimeTableDto.builder()
                .timeTable(timeTable)
                .maxPerson(maxPerson)
                .build();
    }
}
