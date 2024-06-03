package com.portfolio.reservation.dto.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TimeTableRequest {

    @Schema(description = "timeTableId")
    private Long timeTableId;

    @Schema(description = "활성 여부", example = "true")
    private boolean available;

    @Schema(description = "시간당 최대 인원수", example = "20")
    private int maxPersonOfTime;
}
