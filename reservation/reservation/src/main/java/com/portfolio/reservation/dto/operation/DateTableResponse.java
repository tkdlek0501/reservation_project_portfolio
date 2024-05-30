package com.portfolio.reservation.dto.operation;

import com.portfolio.reservation.domain.reservation.Reservation;
import com.portfolio.reservation.domain.reservation.ReservationStatus;
import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import com.portfolio.reservation.domain.timetable.DateTable;
import com.portfolio.reservation.domain.timetable.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateTableResponse {

    private TimeUnitType timeUnit;

    private Long dateTableId;

    private Boolean isHoliday;

    private LocalDate date;

    private int currentPersons;

    private int maxPersonOfDay;

    private Boolean available;

    private Boolean isHourlySetting;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<TimeTableResponse> timeTables = new ArrayList<>();

    public static DateTableResponse of(Long dateTableId, List<TimeTableWithDateTableDto> dtos, Map<Long, List<Reservation>> reservationMap) {

        int currentPersons = 0;
        TimeUnitType timeUnit = TimeUnitType.HOUR;
        LocalDate date = null;
        int maxPersonOfDay = 0;
        Boolean dailyAvailable = null;
        Boolean isHourlySetting = null;

        for (TimeTableWithDateTableDto dto : dtos) {
            currentPersons += reservationMap.get(dto.getTimeTableId())
                    .stream()
                    .mapToInt(Reservation::getPersons)
                    .sum();

            timeUnit = dto.getTimeUnit();
            date = dto.getDate();
            maxPersonOfDay = dto.getMaxPersonOfDay();
            dailyAvailable = dto.getDateAvailable();
            isHourlySetting = dto.getIsHourlySetting();
        }

        LocalTime minTime = makeStartDateOfDateTable(dtos);
        LocalTime maxTime = makeEndDateOfDateTable(dtos, timeUnit);

        return DateTableResponse.builder()
                .timeUnit(timeUnit)
                .dateTableId(dateTableId)
                .isHoliday(false)
                .date(date)
                .currentPersons(currentPersons)
                .maxPersonOfDay(maxPersonOfDay)
                .available(dailyAvailable)
                .isHourlySetting(isHourlySetting)
                .timeTables(dtos.stream()
                        .map(TimeTableResponse::of)
                        .sorted(Comparator.comparing(TimeTableResponse::getTime))
                        .collect(Collectors.toList()))
                .startTime(minTime)
                .endTime(maxTime)
                .build();
    }

    public void changeIsHoliday(Boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    private static LocalTime makeStartDateOfDateTable(List<TimeTableWithDateTableDto> dtos) {

        return dtos
                .stream()
                .min(Comparator.comparing(TimeTableWithDateTableDto::getTime))
                .map(TimeTableWithDateTableDto::getTime)
                .orElse(null);
    }

    private static LocalTime makeEndDateOfDateTable(List<TimeTableWithDateTableDto> dtos,
                                                   TimeUnitType timeUnit) {

        return dtos
                .stream()
                .max(Comparator.comparing(TimeTableWithDateTableDto::getTime))
                .map(tt -> switch (timeUnit) {
                    case HALF -> tt.getTime().plusMinutes(30);
                    case HOUR -> tt.getTime().plusHours(1);
                })
                .orElse(null);
    }
}
