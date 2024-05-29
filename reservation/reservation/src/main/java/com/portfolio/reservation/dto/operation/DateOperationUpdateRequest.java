package com.portfolio.reservation.dto.operation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DateOperationUpdateRequest {

    @Schema(description = "dateOperationId", example = "1")
    private Long dateOperationId;

    @Schema(description = "시작일", example = "YYYYMMdd")
    private LocalDate startDate; // YYYYMMdd

    @Schema(description = "종료일", example = "YYYYMMdd")
    private LocalDate endDate;

    @Schema(description = "시간 기준 운영 시간")
    private List<TimeOperationUpdateRequest> timeOperations = new ArrayList<>();
}
