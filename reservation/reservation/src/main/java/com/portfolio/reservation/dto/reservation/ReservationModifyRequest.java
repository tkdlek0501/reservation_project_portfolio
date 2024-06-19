package com.portfolio.reservation.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReservationModifyRequest {

    @NotNull
    @Schema(name = "reservationId", description = "예약 key", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234" )
    private Long reservationId;

    @NotNull
    @Schema(name="timeId", description = "예약시간 key", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    private Long timeId;

    @NotNull
    @Min(1)
    @Max(100)
    @Schema(name = "personCount", description = "예약인원", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    private Integer personCount;


    @Schema(name ="reserveRequest", description = "예약 요청사항", requiredMode = Schema.RequiredMode.NOT_REQUIRED, maxLength = 200, example = "안쪽 창문자리로 부탁드립니다.")
    private String reserveRequest;
}
