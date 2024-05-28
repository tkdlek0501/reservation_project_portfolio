package com.portfolio.reservation.dto.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeOperationRequest {

    @Schema(description = "시작 시간", example = "HH:mm:ss")
    private LocalTime startTime;

    @Schema(description = "종료 시간", example = "HH:mm:ss")
    private LocalTime endTime;

    @Schema(description = "시간 동안 최대 인원", example = "50")
    private int maxPerson;
}
