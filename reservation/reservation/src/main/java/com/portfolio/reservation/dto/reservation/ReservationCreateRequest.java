package com.portfolio.reservation.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReservationCreateRequest {

    @NotNull
    @Schema(name="timeId", description = "예약시간 key", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    private Long timeId;

    @NotNull
    @Min(1)
    @Max(100)
    @Schema(name = "personCount", description = "인원수", requiredMode = Schema.RequiredMode.REQUIRED, example = "1" )
    private int personCount;

    @Schema(name = "reserveRequest", description = "예약 요청사항", example = "창가자리로 부탁드립니다." )
    private String reserveRequest;
}
