package com.portfolio.reservation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeInfoDto {

    // 예약 시간 고유 key
    private Long timeId;

    // mm:dd 형식의 시간 표현
    private String time;

    // 가능여부
    private boolean available;

    // 예약가능 잔여 인원
    private int maxPerson;

    public static TimeInfoDto of(TimeTableDto timeTableDto) {

        return TimeInfoDto.builder()
                .timeId(timeTableDto.getTimeTable().getId())
                .time(timeTableDto.getTimeTable().getTime().toString())
                .available(timeTableDto.getTimeTable().isAvailable())
                .maxPerson(Math.toIntExact(timeTableDto.getMaxPerson()))
                .build();
    }
}
