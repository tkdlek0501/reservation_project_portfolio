package com.portfolio.reservation.dto.schedule;

import com.portfolio.reservation.domain.schedule.type.TimeUnitType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateOperationRequest {

    @NotNull
    @Schema(description = "시작일")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "종료일")
    private LocalDate endDate;

    @NotNull
    @Schema(description = "시간 단위")
    private TimeUnitType timeUnit;

    @Schema(description = "시간 기준 운영 시간")
    @Valid
    private List<TimeOperationRequest> timeOperationRequests = new ArrayList<>();
}
