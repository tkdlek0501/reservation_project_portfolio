package com.portfolio.reservation.dto.operation;

import com.portfolio.reservation.domain.schedule.TimeOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeOperationResponse {

    private Long timeOperationId;

    private LocalTime startTime;

    private LocalTime endTime;

    private int maxPerson;

    public static TimeOperationResponse of(TimeOperation time) {

        return TimeOperationResponse.builder()
                .timeOperationId(time.getId())
                .startTime(time.getStartTime())
                .endTime(time.getEndTime())
                .maxPerson(time.getMaxPerson())
                .build();
    }
}
