package com.portfolio.reservation.dto.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TimeTablesRequest {

    @Schema(description = "dataTableId")
    private Long dateTableId;

    @Schema(description = "예약 받기 사용 여부", example = "true")
    private boolean dailyAvailable;

    @Schema(description = "시작 시간")
    private LocalTime startTime;

    @Schema(description = "종료 시간")
    private LocalTime endTime;

    @Schema(description = "하루 가능 총인원")
    private int maxPersonOfDay;

    @Schema(description = "시간별 설정")
    private Boolean isHourlySetting;

    @Schema(description = "시간대별 설정")
    private List<TimeTableRequest> timeTables = new ArrayList<>();
}
