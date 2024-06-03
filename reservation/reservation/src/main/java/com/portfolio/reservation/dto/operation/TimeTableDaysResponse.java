package com.portfolio.reservation.dto.operation;

import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableDaysResponse {

    private TimeUnitType timeUnit;

    private Long dateTableId;

    private LocalDate date;

    private int maxPersonOfDay; // 하루 가능 총인원

    private Boolean dailyAvailable; // 예약 받기 사용/ 미사용

    private Boolean isHourlySetting;

    private LocalTime startTime; // start 시간

    private LocalTime endTime; // end 시간

    private List<TimeTableResponse> timeTables = new ArrayList<>();

//    public static TimeTableDaysResponse of(DateTable dateTable, List<TimeTable> timeTables) {
//
//        LocalTime minTime = makeStartDateOfDateTable(timeTables);
////        LocalTime maxTime = makeEndDateOfDateTable(timeTables, dateTable.getDateOperation().getTimeUnit());
//
//        return TimeTableDaysResponse.builder()
////                .timeUnit(dateTable.getScheduleOperation().getTimeUnit())
//                .dateTableId(dateTable.getId())
//                .date(dateTable.getDate())
//                .maxPersonOfDay(dateTable.getMaxPerson())
//                .dailyAvailable(dateTable.isAvailable())
//                .isHourlySetting(dateTable.isHourlySetting())
//                .startTime(minTime)
////                .endTime(maxTime)
//                .timeTables(timeTables.stream().map(TimeTableResponse::of).collect(Collectors.toList()))
//                .build();
//    }

//    public static List<TimeTableDaysResponse> of(List<TimeTable> timeTables) {
//
//        Map<Long, List<TimeTable>> map = timeTables.stream().collect(Collectors.groupingBy(TimeTable::getDateTableId));
//
//        List<TimeTableDaysResponse> responses = new ArrayList<>();
//        map.forEach((key, value) -> {
//            responses.add(TimeTableDaysResponse.of(key, value));
//        });
//
//        return responses;
//    }
}
