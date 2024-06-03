package com.portfolio.reservation.dto.operation;

import com.portfolio.reservation.domain.schedule.type.SameDayApprovalType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScheduleSameDayRequest {

    @Schema(description = "당일 예약 요청 가능 여부", example = "POSSIBLE or NONE or ONE ...")
    @NotNull
    private SameDayApprovalType sameDayRequestApproval;

    @Schema(description = "당일 예약 취소 가능 여부", example = "")
    @NotNull
    private SameDayApprovalType sameDayCancelApproval;
}
