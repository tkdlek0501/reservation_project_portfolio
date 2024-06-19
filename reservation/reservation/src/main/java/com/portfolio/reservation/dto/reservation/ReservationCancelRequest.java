package com.portfolio.reservation.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReservationCancelRequest {

    @NotNull
    @Schema(name = "reserveId", description = "예약 key", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234" )
    private Long reservationId;

    @NotNull
    @NotBlank
    @Schema(name ="cancelReason", description = "취소 사유 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 200, example = "급하게 일정이 변경되어 방문하지 못하게 되었습니다.")
    private String cancelReason;
}
