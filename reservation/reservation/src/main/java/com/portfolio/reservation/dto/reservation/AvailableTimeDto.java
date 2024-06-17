package com.portfolio.reservation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeDto {

    private List<TimeInfoDto> timeTable;

    private int requestMaxPerson; // 매장 예약 요청 가능 최대 인원수

    public static AvailableTimeDto of(List<TimeTableDto> times, int requestMaxPerson) {

        List<TimeInfoDto> timeTables = times.stream()
                .map(TimeInfoDto::of)
                .collect(Collectors.toList());

        return AvailableTimeDto.builder()
                .timeTable(timeTables)
                .requestMaxPerson(requestMaxPerson)
                .build();
    }
}
